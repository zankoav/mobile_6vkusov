package by.vkus.alexandrzanko.mobile_6vkusov.Activities.AuthActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity implements LoadJson {

    public final static String REGISTRATION = "com.netbix.alexandrzanko.mobile_6vkusov.REGISTRATION";

    private final String TAG = this.getClass().getSimpleName();
    private EditText email,password, confirmPassword, firstName, promoCode;
    private CheckBox checkBoxLicense, checkBoxNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.activity_registration);
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
        if(obj != null){
            try {
                String status = obj.getString("status");
                if (status.equals("successful")){
                    Intent answerIntent = new Intent();
                    answerIntent.putExtra(REGISTRATION, "Вы успешно зарегистрированы, Вам на почту " + email.getText().toString() + " отправлено письмо для активации аккаунта");
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
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_server), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void registerPressed(View view) {

        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        String confirmPassword = this.confirmPassword.getText().toString().trim();
        String firstName = this.firstName.getText().toString().trim();
        String promoCode = this.promoCode.getText().toString().trim();
        int news = this.checkBoxNews.isChecked() ? 1 :2;
        //int license = this.checkBoxLicense.isChecked() ? 1 :2;

        if(!Validation.minLength(firstName,2)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_min_name_length), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else if(!Validation.nameLiterals(firstName)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_name_only_literals), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else if(!Validation.email(email)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_email), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else if(!Validation.minLength(password,6)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_min_password_length), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else if(!password.equals(confirmPassword)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_not_equals_passwords), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else if(!Validation.maxLength(firstName,128)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_max_name_length), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else if(!Validation.maxLength(password,128)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_max_password_length), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else if(promoCode.length() != 0 && !Validation.nameLiteralsAndNumbers(promoCode)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_promo_code_symbols_and_numbers), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else if(!Validation.maxLength(promoCode,128)){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_max_promo_code), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else if(!this.checkBoxLicense.isChecked()){
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_license_agree), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }else{
            sendHashToTheServer(email,password,firstName,promoCode,news);
        }
    }

    private void sendHashToTheServer(String email, String password, String firstName, String promoCode, int news){
        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            params.put("password", password);
            params.put("name", firstName);
            if(!promoCode.equals("")){
                params.put("promo", promoCode);
            }
            params.put("news", news);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getApplicationContext(),this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.error_server), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        String url = this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.api_registration);
        (new JsonHelperLoad(url,params,this, TAG)).execute();
    }

    private void addToolBarToScreen() {
        Toolbar toolbar = (Toolbar)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.toolbar);
        toolbar.setTitle(by.vkus.alexandrzanko.mobile_6vkusov.R.string.reg_title);
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
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x/4;
        int height = size.y;
        if(height <= 900) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, width);
            RelativeLayout relative_form = (RelativeLayout) findViewById(R.id.relative_form);
            ImageView logo = (ImageView) findViewById(R.id.iv_profile);
            logo.setMaxHeight(width);
            logo.setMaxWidth(width);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            relative_form.setGravity(Gravity.CENTER_HORIZONTAL);
            relative_form.updateViewLayout(logo, layoutParams);
        }

        email = (EditText)findViewById(R.id.et_email);
        password = (EditText)findViewById(R.id.et_password);
        confirmPassword = (EditText)findViewById(R.id.et_confirm_password);
        firstName = (EditText)findViewById(R.id.et_first_name);
        promoCode = (EditText)findViewById(R.id.et_promo);
        checkBoxLicense = (CheckBox)findViewById(R.id.check_box_license);
        checkBoxNews = (CheckBox)findViewById(R.id.check_box_news);
    }

    public void backButtonClick(View view) {
        this.finish();
    }

    public void licenseClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.reg_license_url)));
        startActivity(browserIntent);
    }
}
