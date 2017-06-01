package by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by alexandrzanko on 2/22/17.
 */

public class JsonHelperLoad extends AsyncTask<Void, Void, JSONObject> {

    private final String TAG = this.getClass().getSimpleName();
    private final String KEY = "252cbf79f74f36e0df806817847a0e1b";


    private String url, name;
    private JSONObject params;
    private LoadJson act;

    public JsonHelperLoad(String url, JSONObject params, LoadJson act, String sessionName) {
        this.url = url;
        this.params = params;
        try {
            this.params.put("key",KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.act = act;
        this.name = sessionName;
        try {
            initFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(JSONObject obj) {
        super.onPostExecute(obj);
        if (act != null) {
            act.loadComplete(obj, this.name);
        }
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        try {
            JSONObject obj = openConnection();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject openConnection() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        JSONObject res = null;
        try {
            URL url = new URL(this.url);
            HttpsURLConnection client = (HttpsURLConnection) url.openConnection();
            client.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            client.setRequestMethod("POST");
            client.setDoInput(true);

            if (this.params != null) {
                client.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream(), "UTF-8");
                String output = this.params.toString();
                writer.write(output);
                writer.flush();
                writer.close();
            }

            client.connect();
            InputStream is = null;
            if (client.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                is = client.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                res = new JSONObject(result.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    private void initFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        tmf.init((KeyStore) null);
        TrustManager[] trustManagers = tmf.getTrustManagers();
        final X509TrustManager origTrustmanager = (X509TrustManager) trustManagers[0];
        TrustManager[] wrappedTrustManagers = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return origTrustmanager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        origTrustmanager.checkClientTrusted(certs, authType);
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            origTrustmanager.checkServerTrusted(certs, authType);
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }
                    }
                }
        };
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, wrappedTrustManagers, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
}