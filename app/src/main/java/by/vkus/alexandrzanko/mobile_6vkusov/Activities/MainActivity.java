package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.AuthActivities.LoginActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.LocalStorage;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import de.hdodenhof.circleimageview.CircleImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private Singleton singleton = Singleton.currentState();
    private LinearLayout generalMenu, registerMenu;
    private TextView loginOrProfile;
    private CircleImageView userLogo;
    public ImageView lunchScreen, logoView, logoViewCircle;
    private Animation anim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generalMenu = (LinearLayout)findViewById(R.id.generalMenu);
        registerMenu = (LinearLayout)findViewById(R.id.registerMenu);
        loginOrProfile = (TextView) findViewById(R.id.btn_save);
        userLogo = (CircleImageView)findViewById(R.id.iv_user);
        lunchScreen = (ImageView)findViewById(R.id.iv_lunchScreen);
        logoView = (ImageView)findViewById(R.id.search_view);
        logoViewCircle = (ImageView)findViewById(R.id.search_view_circle);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.android_rotate_animation);
        anim.setRepeatCount(10);
        singleton.initStore(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lunchScreen.setVisibility(View.GONE);
        logoViewCircle.setVisibility(View.GONE);
        logoView.setVisibility(View.GONE);
    }

    public void loadComplete(){
        generalMenu.setVisibility(singleton.getUser().getStatus() == STATUS.GENERAL ? View.VISIBLE:View.GONE);
        registerMenu.setVisibility(singleton.getUser().getStatus() == STATUS.REGISTER ? View.VISIBLE:View.INVISIBLE);
        if (singleton.getUser().getStatus() == STATUS.GENERAL ){
            userLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.user));
            loginOrProfile.setText("Войти");
        }else{
            String email = null;
            try {
                JSONObject userJson = singleton.getUser().getProfile();
                email = userJson.getString("email");
                loginOrProfile.setText(email);
                String urlIcon = this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.api_base) + userJson.getString("img_path")+"/"+ userJson.getString("avatar");
                Picasso.with(this)
                        .load(urlIcon)
                        .placeholder(R.drawable.user) //показываем что-то, пока не загрузится указанная картинка
                        .error(R.drawable.user) // показываем что-то, если не удалось скачать картинку
                        .into(userLogo);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(this, CategoriesActivity.class);
        this.startActivity(intent);
    }

    public void foodMenuPressed(View view) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        this.startActivity(intent);
    }

    public void loginButtonClick(View view) {
        STATUS status = singleton.getUser().getStatus();
        Intent intent = status == STATUS.GENERAL? new Intent(this, LoginActivity.class): new Intent(this,ProfileActivity.class);
        this.startActivity(intent);
    }

    public void exitMenuPressed(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вы хотите выйдти?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                LocalStorage store = singleton.getStore();
                store.clearKeyStorage(store.APP_PROFILE);
                lunchScreen.setVisibility(View.VISIBLE);
                logoViewCircle.setVisibility(View.VISIBLE);
                showAnimation();
                logoView.setVisibility(View.VISIBLE);
                singleton.initStore(MainActivity.this);
            }
        });
        builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        builder.setCancelable(true);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {

            }
        });
        builder.show();
    }

    public void showAnimation(){
        logoViewCircle.startAnimation(anim);
    }

    public void bonusProgramPressed(View view) {
        Log.i(TAG, "bonusProgramPressed");
    }

    public void favoriteRestaurants(View view) {
        Log.i(TAG, "favoriteRestaurants");
        Intent intent = new Intent(MainActivity.this, RestaurantsCardActivity.class);
        intent.putExtra("favorite", true);
        startActivity(intent);

    }
}
