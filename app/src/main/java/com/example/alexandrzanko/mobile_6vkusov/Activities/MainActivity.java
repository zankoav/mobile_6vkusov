package com.example.alexandrzanko.mobile_6vkusov.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alexandrzanko.mobile_6vkusov.Activities.AuthActivities.LoginActivity;
import com.example.alexandrzanko.mobile_6vkusov.LocalStorage;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Singleton;
import com.example.alexandrzanko.mobile_6vkusov.Users.STATUS;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private Singleton singleton = Singleton.currentState();
    private LinearLayout generalMenu, registerMenu;
    private TextView loginOrProfile;
    private ImageView userLogo;
    public ImageView lunchScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generalMenu = (LinearLayout)findViewById(R.id.generalMenu);
        registerMenu = (LinearLayout)findViewById(R.id.registerMenu);
        loginOrProfile = (TextView) findViewById(R.id.btn_login);
        userLogo = (ImageView)findViewById(R.id.iv_user);
        lunchScreen = (ImageView)findViewById(R.id.iv_lunchScreen);
        singleton.initStore(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lunchScreen.setVisibility(View.GONE);
    }

    public void loadComplete(){
        generalMenu.setVisibility(singleton.getUser().getStatus() == STATUS.GENERAL ? View.VISIBLE:View.GONE);
        registerMenu.setVisibility(singleton.getUser().getStatus() == STATUS.REGISTER ? View.VISIBLE:View.INVISIBLE);
        if (singleton.getUser().getStatus() == STATUS.GENERAL ){
            userLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_avatar));
            loginOrProfile.setText("Войти");
        }else{
            String email = null;
            try {
                email = singleton.getUser().getProfile().getString("email");
                loginOrProfile.setText(email);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(this, CategoriesActivity.class);
        this.startActivity(intent);
    }

    public void foodMenuPressed(View view) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        this.startActivity(intent);
    }

    public void loginButtonClick(View view) {
        STATUS status = singleton.getUser().getStatus();
        Intent intent = status == STATUS.GENERAL? new Intent(this, LoginActivity.class): new Intent(this,ProfileActivity.class);
        this.startActivity(intent);
    }

    public void exitMenuPressed(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вы хотите выйдти?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                LocalStorage store = singleton.getStore();
                store.clearKeyStorage(store.APP_PROFILE);
                lunchScreen.setVisibility(View.VISIBLE);
                singleton.initStore(MainActivity.this);
            }
        });
        builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        builder.setCancelable(true);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {

            }
        });
        builder.show();
    }
}
