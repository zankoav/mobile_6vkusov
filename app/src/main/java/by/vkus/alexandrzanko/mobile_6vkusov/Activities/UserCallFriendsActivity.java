package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.content.Context;
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

import org.json.JSONException;
import org.json.JSONObject;

import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;

public class UserCallFriendsActivity extends AppCompatActivity implements LoadJson {

    private final String TAG = this.getClass().getSimpleName();
    private EditText email;
    private Button btn;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_call_friends);
        addToolBarToScreen();
        email = (EditText)findViewById(R.id.et_name);
        btn = (Button)findViewById(R.id.btn_call);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFriends();
            }
        });
        text = (TextView)findViewById(R.id.call_friend);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }

    private void addToolBarToScreen() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Приглосить друга");
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

    private void getFriends() {
        btn.setEnabled(false);
        String email = this.email.getText().toString().trim();
        if(Validation.email(email)){
            JSONObject params = new JSONObject();
            try {
                params.put("email", email);
                JSONObject userProfileJson = Singleton.currentState().getUser().getProfile();
                String session = userProfileJson.getString("session");
                params.put("session", session);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
            String url = this.getResources().getString(R.string.api_invite);
            (new JsonHelperLoad(url,params, this, null)).execute();
        }else{
            Toast toast = Toast.makeText(this,this.getResources().getString(R.string.error_email), Toast.LENGTH_SHORT);
            toast.show();
            btn.setEnabled(true);
        }
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
                text.setText("Вашему другу на почту " + email.getText() + " отправлено приглошение для регистрации");
                email.setText("");
                btn.setEnabled(true);
            }else{
                Toast toast = null;
                try {
                    String msg = obj.getString("message");
                    toast = Toast.makeText(this,msg, Toast.LENGTH_SHORT);
                    toast.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                btn.setEnabled(true);
            }
        }else{
            Toast toast = Toast.makeText(this,this.getResources().getString(R.string.error_server), Toast.LENGTH_SHORT);
            toast.show();
            btn.setEnabled(true);
        }

    }
}
