package by.vkus.alexandrzanko.mobile_6vkusov.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by alexandrzanko on 5/2/17.
 */

public class GetPointsFragment extends Fragment implements LoadJson {

    private final String TAG = this.getClass().getSimpleName();
    private EditText email;
    private Button btn;
    private TextView text;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.fragment_get_points_layout, container, false);
        email = (EditText)rootView.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.et_name);
        btn = (Button)rootView.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFriends();
            }
        });
        text = (TextView)rootView.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.call_friend);
        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        if (obj != null) {
            String status = "error";
            try {
                status = obj.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (status.equals("successful")) {
                text.setText("Вашему другу на почту " + email.getText() + " отправлено приглошение для регистрации");
                btn.setEnabled(true);
            }else{
                Toast toast = null;
                try {
                    String msg = obj.getString("message");
                    toast = Toast.makeText(getContext(),msg, Toast.LENGTH_SHORT);
                    toast.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                btn.setEnabled(true);
            }
        }else{
            Toast toast = Toast.makeText(getContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_server), Toast.LENGTH_SHORT);
            toast.show();
            btn.setEnabled(true);
        }

    }

    private void getFriends() {
        btn.setEnabled(false);
        String email = this.email.getText().toString().trim();
        if(Validation.email(email)){
            JSONObject params = new JSONObject();
            try {
                params.put("email", email);
                JSONObject userProfileJson = Singleton.currentState().getUser().getProfile();
                String session = userProfileJson.getString("session");
                params.put("session", session);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
            String url = this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.api_invite);
            (new JsonHelperLoad(url,params, this, null)).execute();
        }else{
            Toast toast = Toast.makeText(this.getContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_email), Toast.LENGTH_SHORT);
            toast.show();
            btn.setEnabled(true);
        }
    }
}
