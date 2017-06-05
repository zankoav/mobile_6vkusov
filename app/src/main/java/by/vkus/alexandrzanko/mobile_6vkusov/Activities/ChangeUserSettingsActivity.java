package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.content.Context;
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

import org.json.JSONException;
import org.json.JSONObject;

import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;

public class ChangeUserSettingsActivity extends AppCompatActivity implements LoadJson {

    private final String TAG = this.getClass().getSimpleName();

    private EditText firstName,lastName;
    private Button changeBtn;
    private JSONObject profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_settings);
        profile = Singleton.currentState().getUser().getProfile();
        addToolBarToScreen();
        initFields();
    }

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        if (obj != null) {
            try {
                String status = obj.getString("status");
                if (status.equals("successful")) {
                    Toast toast = Toast.makeText(getApplicationContext(),obj.getString("message"), Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),obj.getString("message"), Toast.LENGTH_SHORT);
                    toast.show();
                }
                changeBtn.setEnabled(true);
            } catch (JSONException e) {
                Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_server), Toast.LENGTH_SHORT);
                toast.show();
                changeBtn.setEnabled(true);
                e.printStackTrace();
            }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_server), Toast.LENGTH_SHORT);
            toast.show();
            changeBtn.setEnabled(true);
        }

    }

    private void addToolBarToScreen() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Редактирование профиля");
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
        firstName = (EditText)findViewById(R.id.et_name);
        lastName = (EditText)findViewById(R.id.et_surname);
        changeBtn = (Button)findViewById(R.id.btn_save);
        try {
            String fName = profile.getString("firstName");
            String fSurname = profile.getString("lastName");
            if (fName != "null"){
                firstName.setText(fName);
            }

            if (fSurname != "null"){
                lastName.setText(fSurname);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void savePressed(View view) {
        changeBtn.setEnabled(false);
        String name = this.firstName.getText().toString().trim();
        String surname = this.lastName.getText().toString().trim();

        if(!Validation.minLength(name,2)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_min_name_length), Toast.LENGTH_SHORT);
            toast.show();
            changeBtn.setEnabled(true);
            return;
        }else if(!Validation.maxLength(name,128)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_max_password_length), Toast.LENGTH_SHORT);
            toast.show();
            changeBtn.setEnabled(true);
            return;
        }if(!Validation.minLength(surname,2)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_min_name_length), Toast.LENGTH_SHORT);
            toast.show();
            changeBtn.setEnabled(true);
            return;
        }else if(!Validation.maxLength(surname,128)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_max_password_length), Toast.LENGTH_SHORT);
            toast.show();
            changeBtn.setEnabled(true);
            return;
        }else{
            String session = null;
            try {
                session = profile.getString("session");
                sendHashToTheServer(name,surname,session);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendHashToTheServer(String name, String surname, String session){
        JSONObject params = new JSONObject();
        try {
            params.put("name", name);
            params.put("session", session);
            params.put("surname", surname);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_server), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        Log.i(TAG, "sendHashToTheServer: " + params.toString());
        String url = this.getResources().getString(R.string.api_change_data);
        (new JsonHelperLoad(url,params,this, TAG)).execute();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }
}
