package com.example.alexandrzanko.mobile_6vkusov;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements LoadJson {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {

    }
}
