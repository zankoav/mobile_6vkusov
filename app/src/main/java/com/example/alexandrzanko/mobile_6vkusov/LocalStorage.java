package com.example.alexandrzanko.mobile_6vkusov;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.alexandrzanko.mobile_6vkusov.Activities.MainActivity;
import com.example.alexandrzanko.mobile_6vkusov.Users.General;
import com.example.alexandrzanko.mobile_6vkusov.Users.Register;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexandrzanko on 3/1/17.
 */

public class LocalStorage implements LoadJson{

    private final String TAG = this.getClass().getSimpleName();

    private final String APP_STORE = "store";

    public final String APP_CATEGORIES = "categories";
    public final String APP_RESTAURANTS = "restaurants";
    public final String APP_PROFILE = "profile";

    private SharedPreferences store;
    private Context context;

    public LocalStorage(Context context){
        this.store = context.getSharedPreferences(APP_STORE, Context.MODE_PRIVATE);
        this.context = context;
        Singleton.currentState().setUser(null);
        clearKeyStorage(APP_CATEGORIES);
        clearKeyStorage(APP_RESTAURANTS);
        String urlCat = context.getResources().getString(R.string.api_categories);
        new JsonHelperLoad(urlCat, null, this, APP_CATEGORIES).execute();

        JSONObject paramsRestaurants = new JSONObject();
        try {
            paramsRestaurants.put("slug", "all");
            String urlRest = context.getResources().getString(R.string.api_restaurants);
            new JsonHelperLoad(urlRest, paramsRestaurants, this, APP_RESTAURANTS).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String userProfile = getStringValueStorage(APP_PROFILE);

        if(userProfile != null){
            JSONObject params = new JSONObject();
            try {
                JSONObject userProfileJson = new JSONObject(userProfile);
                String session = userProfileJson.getString("session");
                params.put("session", session);
                String url = context.getResources().getString(R.string.api_user);
                new JsonHelperLoad(url, params, this, APP_PROFILE).execute();
            } catch (JSONException e) {
                Singleton.currentState().setUser(new General());
                Log.i(TAG,"initial General User Exception");
                e.printStackTrace();
            }
        }else{
            Singleton.currentState().setUser(new General());
        }
    }

    public String getStringValueStorage(String key) {
        if(this.store.contains(key)) {
            return this.store.getString(key, "");
        }else{
            return null;
        }
    }

    public void setStringValueStorage(String key, String value){
        SharedPreferences.Editor editor = this.store.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void clearKeyStorage(String key) {
        SharedPreferences.Editor editor = this.store.edit();
        editor.remove(key);
        editor.apply();
    }


    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        if(obj != null){
            setStringValueStorage(sessionName, obj.toString());
            Log.i(TAG,obj.toString());
            if (sessionName == APP_PROFILE) {
                Singleton.currentState().setUser(new Register());
            }
            if(Singleton.currentState().getUser() != null &&
               getStringValueStorage(APP_CATEGORIES) != null &&
               getStringValueStorage(APP_RESTAURANTS) != null)
            {
                Log.i(TAG, "loadComplete: ");
                ((MainActivity)context).loadComplete();
            }
        }else{
            Log.i(TAG,"Error connection");
        }
    }

}
