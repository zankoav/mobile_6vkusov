package com.example.alexandrzanko.mobile_6vkusov;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Main activity");
        Intent intent = new Intent(this,RegistrationActivity.class);
        this.startActivity(intent);
    }
}
