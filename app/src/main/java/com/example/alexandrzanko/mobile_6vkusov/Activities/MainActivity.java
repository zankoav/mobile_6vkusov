package com.example.alexandrzanko.mobile_6vkusov.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.alexandrzanko.mobile_6vkusov.Activities.AuthActivities.RegistrationActivity;
import com.example.alexandrzanko.mobile_6vkusov.R;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,RegistrationActivity.class);
        this.startActivity(intent);
    }
}
