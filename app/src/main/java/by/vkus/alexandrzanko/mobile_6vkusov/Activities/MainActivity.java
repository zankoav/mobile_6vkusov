package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;


public class MainActivity extends AppCompatActivity {

    private Singleton singleton = Singleton.currentState();
    public ImageView lunchScreen, logoView, logoViewCircle;
    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lunchScreen = (ImageView)findViewById(R.id.iv_lunchScreen);
        logoView = (ImageView)findViewById(R.id.search_view);
        logoViewCircle = (ImageView)findViewById(R.id.search_view_circle);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.android_rotate_animation);
        anim.setRepeatCount(10);
        singleton.initStore(this);
        showAnimation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        lunchScreen.setVisibility(View.GONE);
        logoViewCircle.setVisibility(View.GONE);
        logoView.setVisibility(View.GONE);
    }

    public void loadComplete(){
        Intent intent = new Intent(this, CategoriesActivity.class);
        this.startActivity(intent);
        this.finish();
    }


    public void showAnimation(){
        logoViewCircle.startAnimation(anim);
    }

}
