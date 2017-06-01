package by.vkus.alexandrzanko.mobile_6vkusov.Activities.AuthActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class RememberActivity extends AppCompatActivity implements LoadJson {

    private final String TAG = this.getClass().getSimpleName();
    public final static String RESET = "com.netbix.alexandrzanko.mobile_6vkusov.RESET";

    private EditText email;

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
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        Log.i(TAG,obj.toString());
        if (obj != null) {
            try {
                String status = obj.getString("status");
                if (status.equals("successful")){
                    Intent answerIntent = new Intent();
                    answerIntent.putExtra(RESET, obj.getString("email"));
                    setResult(RESULT_OK, answerIntent);
                    finish();
                }else if(status.equals("error")){
                    Toast toast = Toast.makeText(getApplicationContext(),obj.getString("message"), Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_server), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void addToolBarToScreen() {
        Toolbar toolbar = (Toolbar)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.toolbar);
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
        email = (EditText)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.et_email);
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

    private void sendHashToTheServer(String email){
        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_server), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        String url = this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.api_reset_password);
        (new JsonHelperLoad(url,params,this, TAG)).execute();
    }

}
