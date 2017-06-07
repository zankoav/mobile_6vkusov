package by.vkus.alexandrzanko.mobile_6vkusov.Activities.AuthActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import by.vkus.alexandrzanko.mobile_6vkusov.Activities.MainActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Activities.RestaurantsCardActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.LocalStorage;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.InetConnection;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class RememberActivity extends AppCompatActivity implements LoadJson {

    private final String TAG = this.getClass().getSimpleName();
    public final static String RESET = "com.netbix.alexandrzanko.mobile_6vkusov.RESET";

    private EditText email;
    private Button button;
    private ImageView imageLogo;
    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.activity_remember);
        addToolBarToScreen();
        initFields();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(this.getCurrentFocus() != null){
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        Log.i(TAG,obj.toString());

        if (obj != null) {
            try {
                String status = obj.getString("status");
                if (status.equals("successful")){
                    showAlert("Ссылка для изменения пароля отправлена на Ваш email",true);
                    email.setText("");
                }else if(status.equals("error")){
                    showAlert(obj.getString("message"),false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showAlert(this.getResources().getString(R.string.error_server),false);
            }
        }else{
            showAlert(this.getResources().getString(R.string.error_server),false);
        }
        showInterface();
    }

    private void addToolBarToScreen() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(by.vkus.alexandrzanko.mobile_6vkusov.R.string.remember_title);
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
        email = (EditText)findViewById(R.id.et_name);
        button =  (Button) findViewById(R.id.btn_save);
        imageLogo = (ImageView) findViewById(R.id.iv_circle);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.android_rotate_animation);
        anim.setRepeatCount(Animation.INFINITE);
    }

    public void sendPressed(View view) {
        String email = this.email.getText().toString().trim();

        if(!Validation.email(email)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_email), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else{
            sendHashToTheServer(email);
        }
    }

    private void hideInterface(){
        imageLogo.startAnimation(anim);
        this.email.setVisibility(View.INVISIBLE);
        this.button.setVisibility(View.INVISIBLE);
        this.button.setEnabled(false);
    }

    private void showInterface(){
        imageLogo.clearAnimation();
        this.email.setVisibility(View.VISIBLE);
        this.button.setVisibility(View.VISIBLE);
        this.button.setEnabled(true);
    }

    private void sendHashToTheServer(String email){
        hideInterface();
        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            String url = this.getResources().getString(R.string.api_reset_password);
            if (InetConnection.isNetworkAvailable(this)){
                (new JsonHelperLoad(url,params,this, TAG)).execute();
            }else{
                Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_net), Toast.LENGTH_SHORT);
                toast.show();
                showInterface();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showInterface();
            showAlert(this.getResources().getString(R.string.error_server),false);
        }
    }


    private void showAlert(String message, boolean status){
        String type = status ? "Успешно!" : "Ошибка!";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(type);
        builder.setMessage(message);
        builder.setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
