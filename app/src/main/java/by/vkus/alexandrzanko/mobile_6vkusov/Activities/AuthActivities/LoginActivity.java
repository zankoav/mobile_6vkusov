package by.vkus.alexandrzanko.mobile_6vkusov.Activities.AuthActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import by.vkus.alexandrzanko.mobile_6vkusov.LocalStorage;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements LoadJson {

    private final String TAG = this.getClass().getSimpleName();
    static final private int RESET_PASSWORD = 1;
    static final private int REGISTRATION_USER = 2;


    private EditText email,password;
    private TextView message;
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
            String status = "error";
            try {
                status = obj.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (status.equals("successful")) {
                LocalStorage store = Singleton.currentState().getStore();

                try {
                    JSONObject user = obj.getJSONObject("message");
                    store.setStringValueStorage(store.APP_PROFILE,user.toString());
                    Singleton.currentState().initStore(null);
                    loginBtn.setEnabled(true);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_server), Toast.LENGTH_SHORT);
            toast.show();
            loginBtn.setEnabled(true);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESET_PASSWORD) {
            if (resultCode == RESULT_OK) {
                String answer = data.getStringExtra(RememberActivity.RESET);
                message.setText("На почту " + answer + " отправлено письмо для смены пароля");
            }
        }

        if (requestCode == REGISTRATION_USER) {
            if (resultCode == RESULT_OK) {
                String answer = data.getStringExtra(RegistrationActivity.REGISTRATION);
                message.setText(answer);
            }
        }
    }

    private void addToolBarToScreen() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(by.vkus.alexandrzanko.mobile_6vkusov.R.string.login_title);
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
        message = (TextView)findViewById(R.id.tv_social);
        email = (EditText)findViewById(R.id.et_name);
        password = (EditText)findViewById(R.id.et_surname);
        loginBtn = (Button)findViewById(R.id.btn_save);
    }


    public void loginPressed(View view) {
        loginBtn.setEnabled(false);
        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        if(!Validation.email(email)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_email), Toast.LENGTH_SHORT);
            toast.show();
            loginBtn.setEnabled(true);
            return;
        }else if(!Validation.minLength(password,6)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_min_password_length), Toast.LENGTH_SHORT);
            toast.show();
            loginBtn.setEnabled(true);
            return;
        }else if(!Validation.maxLength(password,128)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_max_password_length), Toast.LENGTH_SHORT);
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
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_server), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        String url = this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.api_login);
        (new JsonHelperLoad(url,params,this, TAG)).execute();
    }

    public void goToRegistration(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        this.startActivityForResult(intent, REGISTRATION_USER);
    }

    public void rememberPressed(View view) {
        Intent intent = new Intent(this, RememberActivity.class);
        this.startActivityForResult(intent, RESET_PASSWORD);
    }
}
