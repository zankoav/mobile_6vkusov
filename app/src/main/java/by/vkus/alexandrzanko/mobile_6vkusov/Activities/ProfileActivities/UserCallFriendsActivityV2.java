package by.vkus.alexandrzanko.mobile_6vkusov.Activities.ProfileActivities;

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
import android.widget.TextView;
import android.widget.Toast;

import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCallFriendsActivityV2 extends AppCompatActivity {

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
        String emailFriend = this.email.getText().toString().trim();
        if(Validation.email(emailFriend)){
            ApiController.getApi().callFriend(SingletonV2.currentState().getIUser().getSession(),emailFriend).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i(TAG, "onResponse: " + response.code());
                    if (response.code() == 392){
                        text.setText("Вашему другу на почту " + email.getText() + " отправлено приглошение для регистрации");
                        email.setText("");
                    }else if(response.code() == 395){
                        Toast toast = Toast.makeText(UserCallFriendsActivityV2.this,UserCallFriendsActivityV2.this.getResources().getString(R.string.error_server), Toast.LENGTH_SHORT);
                        toast.show();
                    }else if(response.code() == 394){
                        Toast toast = Toast.makeText(UserCallFriendsActivityV2.this,UserCallFriendsActivityV2.this.getResources().getString(R.string.error_friend_exists), Toast.LENGTH_SHORT);
                        toast.show();
                    }else if(response.code() == 393){
                        Toast toast = Toast.makeText(UserCallFriendsActivityV2.this,UserCallFriendsActivityV2.this.getResources().getString(R.string.error_friend_limit), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    btn.setEnabled(true);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i(TAG, "onFailure: ");
                    Toast toast = Toast.makeText(UserCallFriendsActivityV2.this,UserCallFriendsActivityV2.this.getResources().getString(R.string.error_server), Toast.LENGTH_SHORT);
                    toast.show();
                    btn.setEnabled(true);
                }
            });
        }else{
            Toast toast = Toast.makeText(this,this.getResources().getString(R.string.error_email), Toast.LENGTH_SHORT);
            toast.show();
            btn.setEnabled(true);
        }
    }

}
