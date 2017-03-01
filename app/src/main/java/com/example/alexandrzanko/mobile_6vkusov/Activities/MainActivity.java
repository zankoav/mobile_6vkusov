package com.example.alexandrzanko.mobile_6vkusov.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Singleton;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private Singleton singleton = Singleton.currentState();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "MainActivity Loading ... ");
        singleton.initStore(this);
    }

    public void loadComplete(){
        Log.i(TAG, "Complete Load ");
        Intent intent = new Intent(this, CategoriesActivity.class);
        this.startActivity(intent);
    }
}
