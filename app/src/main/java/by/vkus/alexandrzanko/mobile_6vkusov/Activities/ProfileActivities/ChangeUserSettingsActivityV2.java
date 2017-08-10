package by.vkus.alexandrzanko.mobile_6vkusov.Activities.ProfileActivities;

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
import android.widget.Toast;

import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.UserRegister;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeUserSettingsActivityV2 extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private EditText firstName,lastName;
    private Button changeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_settings);
        addToolBarToScreen();
        initFields();
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
        firstName.setText(SingletonV2.currentState().getIUser().getFirstName());
        lastName.setText(SingletonV2.currentState().getIUser().getLastName());
    }

    public void savePressed(View view) {
        changeBtn.setEnabled(false);
        String first_name = this.firstName.getText().toString().trim();
        String last_name = this.lastName.getText().toString().trim();

        if(!Validation.minLength(first_name,2)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_min_name_length), Toast.LENGTH_SHORT);
            toast.show();
            changeBtn.setEnabled(true);
            return;
        }else if(!Validation.maxLength(first_name,128)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_max_password_length), Toast.LENGTH_SHORT);
            toast.show();
            changeBtn.setEnabled(true);
            return;
        }if(!Validation.minLength(last_name,2)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_min_name_length), Toast.LENGTH_SHORT);
            toast.show();
            changeBtn.setEnabled(true);
            return;
        }else if(!Validation.maxLength(last_name,128)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.error_max_password_length), Toast.LENGTH_SHORT);
            toast.show();
            changeBtn.setEnabled(true);
            return;
        }else{
            String session = SingletonV2.currentState().getIUser().getSession();
            ApiController.getApi().setUserProfile(session,first_name,last_name).enqueue(new Callback<UserRegister>() {

                @Override
                public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {
                    if(response.code() == 200){
                        IUser user = response.body();
                        SingletonV2.currentState().getIUser().setFirst_name(user.getFirstName());
                        SingletonV2.currentState().getIUser().setLast_name(user.getLastName());
                        Toast.makeText(getApplicationContext(),ChangeUserSettingsActivityV2.this.getResources().getString(R.string.change_user_profile_success), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),ChangeUserSettingsActivityV2.this.getResources().getString(R.string.error_server), Toast.LENGTH_SHORT).show();
                    }
                    changeBtn.setEnabled(true);
                }

                @Override
                public void onFailure(Call<UserRegister> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),ChangeUserSettingsActivityV2.this.getResources().getString(R.string.error_server), Toast.LENGTH_SHORT).show();
                    changeBtn.setEnabled(true);
                }

            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }
}
