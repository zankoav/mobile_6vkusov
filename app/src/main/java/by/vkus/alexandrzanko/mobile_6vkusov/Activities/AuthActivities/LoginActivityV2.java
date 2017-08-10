package by.vkus.alexandrzanko.mobile_6vkusov.Activities.AuthActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKList;
import by.vkus.alexandrzanko.mobile_6vkusov.Activities.BaseMenuActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Activities.MainActivityV2;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.UserRegister;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivityV2 extends BaseMenuActivity implements Callback<UserRegister> {

    static final private int RESET_PASSWORD = 1;
    static final private int REGISTRATION_USER = 2;

    private CallbackManager callbackManager;

    private EditText email,password;
    private TextView message, tvOr;
    private Button loginBtn, btnRemember, btnMaybeReg;
    private ImageButton vkBtn, fbBtn;
    private ImageView circleView;
    private Animation anim;
    private LoginButton fbButton;
    private VKCallback<VKAccessToken> vkToken;

    private LinearLayout llSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews(this.getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.login_title));
        hideBasket();
        initFields();
        fbButton = initFaceBookApi();
        vkToken = initVkToken();
    }

    private VKCallback<VKAccessToken> initVkToken() {
        return new VKCallback<VKAccessToken>() {

            @Override
            public void onResult(VKAccessToken res) {
                final String email = res.email;
                VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "first_name, last_name, sex, email, photo_100"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        VKList list =  (VKList) response.parsedModel;
                        JSONObject user = list.get(0).fields;
                        try {
                            user.put("email",email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Call<UserRegister> userCall = ApiController.getApi().loginVk(user.toString());
                        userCall.enqueue(LoginActivityV2.this);
                        VKSdk.logout();
                    }


                    @Override
                    public void onError(VKError error) {
                        Log.i(TAG, "onError: ");
                    }
                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                        Log.i(TAG, "attemptFailed: ");
                    }
                });
            }

            @Override
            public void onError(VKError error) {
                Log.i("MainActivityV2", "onResult: Error");
            }
        };
    }

    private LoginButton initFaceBookApi(){
        callbackManager = CallbackManager.Factory.create();
        LoginButton button = new LoginButton(this);

        // Callback registration
        button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Call<UserRegister> userCall = ApiController.getApi().loginFacebook(object.toString());
                        userCall.enqueue(LoginActivityV2.this);
                        LoginManager.getInstance().logOut();
                    }

                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, email, gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.i(TAG, "onCancel: ");
            }
        });
        return button;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, vkToken)) {
            super.onActivityResult(requestCode, resultCode, data);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);

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
        showInterface();
    }

    private void initFields(){
        llSocial = (LinearLayout)findViewById(R.id.ll_social);
        vkBtn = (ImageButton)findViewById(R.id.vk_btn);
        fbBtn = (ImageButton)findViewById(R.id.fb_btn);
        tvOr = (TextView)findViewById(R.id.tv_or);
        message = (TextView)findViewById(R.id.tv_social);
        email = (EditText)findViewById(R.id.et_name);
        password = (EditText)findViewById(R.id.et_surname);
        loginBtn = (Button)findViewById(R.id.btn_save);
        btnRemember = (Button)findViewById(R.id.btn_remember);
        btnMaybeReg = (Button)findViewById(R.id.btn_maybe_reg);
        circleView = (ImageView)findViewById(R.id.iv_circle_view);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.android_rotate_animation);
        anim.setRepeatCount(10);
    }

    private void hideInterface(){
        circleView.startAnimation(anim);
        this.email.setVisibility(View.INVISIBLE);
        this.password.setVisibility(View.INVISIBLE);
        this.loginBtn.setVisibility(View.INVISIBLE);
        this.loginBtn.setEnabled(false);
        this.btnRemember.setVisibility(View.INVISIBLE);
        this.btnRemember.setEnabled(false);
        this.btnMaybeReg.setVisibility(View.INVISIBLE);
        this.btnMaybeReg.setEnabled(false);
        this.message.setVisibility(View.INVISIBLE);
        this.tvOr.setVisibility(View.INVISIBLE);
        this.llSocial.setVisibility(View.INVISIBLE);
        this.vkBtn.setEnabled(false);
        this.fbBtn.setEnabled(false);
    }

    private void showInterface(){
        circleView.clearAnimation();
        this.email.setVisibility(View.VISIBLE);
        this.password.setVisibility(View.VISIBLE);
        this.loginBtn.setVisibility(View.VISIBLE);
        this.loginBtn.setEnabled(true);
        this.btnRemember.setVisibility(View.VISIBLE);
        this.btnRemember.setEnabled(true);
        this.btnMaybeReg.setVisibility(View.VISIBLE);
        this.btnMaybeReg.setEnabled(true);
        this.message.setVisibility(View.VISIBLE);
        this.tvOr.setVisibility(View.VISIBLE);
        this.llSocial.setVisibility(View.VISIBLE);
        this.vkBtn.setEnabled(true);
        this.fbBtn.setEnabled(true);
    }

    public void loginPressed(View view) {
        loginBtn.setEnabled(false);
        this.btnRemember.setEnabled(false);
        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        if(!Validation.email(email)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_email), Toast.LENGTH_SHORT);
            toast.show();
            this.btnRemember.setEnabled(true);
            loginBtn.setEnabled(true);
            return;
        }else if(!Validation.minLength(password,6)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_min_password_length), Toast.LENGTH_SHORT);
            toast.show();
            this.btnRemember.setEnabled(true);
            loginBtn.setEnabled(true);
            return;
        }else if(!Validation.maxLength(password,128)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_max_password_length), Toast.LENGTH_SHORT);
            toast.show();
            this.btnRemember.setEnabled(true);
            loginBtn.setEnabled(true);
            return;
        }else{
            Call<UserRegister> userCall = ApiController.getApi().login(email,password);
            userCall.enqueue(LoginActivityV2.this);
        }
    }

    public void goToRegistration(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        this.startActivityForResult(intent, REGISTRATION_USER);
    }

    public void rememberPressed(View view) {
        Intent intent = new Intent(this, RememberActivity.class);
        this.startActivityForResult(intent, RESET_PASSWORD);
    }

    public void faceBookLogin(View view) {
        fbButton.callOnClick();
        hideInterface();
    }

    public void vkLogin(View view) {
        VKSdk.login(this, VKScope.EMAIL);
        hideInterface();
    }

    @Override
    public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {
        if (response.code() == 200){
            IUser user = response.body();
            SingletonV2.currentState().getSessionStoreV2().setStringValueStorage(SingletonV2.currentState().getSessionStoreV2().USER_SESSION,user.getSession());
            Intent intent = new Intent(this, MainActivityV2.class);
            this.startActivity(intent);
            finish();
        }else if(response.code() == 399){
            Toast.makeText(LoginActivityV2.this, "Такого пользователя не существует", Toast.LENGTH_LONG).show();
        }else if(response.code() == 398){
            Toast.makeText(LoginActivityV2.this, "Не верно введен пароль или email", Toast.LENGTH_LONG).show();
        }else if(response.code() == 397){
            Toast.makeText(LoginActivityV2.this, " Необходимо авторизоваться на почте", Toast.LENGTH_LONG).show();
        }
        showInterface();
    }

    @Override
    public void onFailure(Call<UserRegister> call, Throwable t) {
        Toast.makeText(LoginActivityV2.this, "Ошибка соединения", Toast.LENGTH_SHORT).show();
        showInterface();
    }
}
