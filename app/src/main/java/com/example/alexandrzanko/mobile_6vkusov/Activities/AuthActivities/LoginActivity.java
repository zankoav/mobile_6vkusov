package com.example.alexandrzanko.mobile_6vkusov.Activities.AuthActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alexandrzanko.mobile_6vkusov.LocalStorage;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Singleton;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements LoadJson {

    private final String TAG = this.getClass().getSimpleName();
    private EditText email,password;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addToolBarToScreen();
        initFields();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        if (obj != null) {
            int code = 0;
            try {
                code = obj.getInt("code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (code == 1) {
                LocalStorage store = Singleton.currentState().getStore();
                store.setStringValueStorage(store.APP_PROFILE,obj.toString());
                Singleton.currentState().initStore(null);
                loginBtn.setEnabled(true);
                finish();
            }else{
                Toast toast = null;
                try {
                    toast = Toast.makeText(getApplicationContext(),obj.getString("message"), Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                toast.show();
                loginBtn.setEnabled(true);
            }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_server), Toast.LENGTH_SHORT);
            toast.show();
            loginBtn.setEnabled(true);
        }

    }

    private void addToolBarToScreen() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.login_title);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initFields(){
        email = (EditText)findViewById(R.id.et_email);
        password = (EditText)findViewById(R.id.et_password);
        loginBtn = (Button)findViewById(R.id.btn_login);
    }


    public void loginPressed(View view) {
        loginBtn.setEnabled(false);
        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        if(!Validation.email(email)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_email), Toast.LENGTH_SHORT);
            toast.show();
            loginBtn.setEnabled(true);
            return;
        }else if(!Validation.minLength(password,6)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_min_password_length), Toast.LENGTH_SHORT);
            toast.show();
            loginBtn.setEnabled(true);
            return;
        }if(!Validation.maxLength(password,128)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_max_password_length), Toast.LENGTH_SHORT);
            toast.show();
            loginBtn.setEnabled(true);
            return;
        }else{
            sendHashToTheServer(email,password);
        }
    }

    private void sendHashToTheServer(String email, String password){
        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_server), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        String url = this.getResources().getString(R.string.api_login);
        (new JsonHelperLoad(url,params,this, TAG)).execute();
    }

    public void goToRegistration(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        this.startActivity(intent);
    }

    public void rememberPressed(View view) {
        Intent intent = new Intent(this, RememberActivity.class);
        this.startActivity(intent);
    }
}
