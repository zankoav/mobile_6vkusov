package com.example.alexandrzanko.mobile_6vkusov.Activities;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alexandrzanko.mobile_6vkusov.Fragments.GetPointsFragment;
import com.example.alexandrzanko.mobile_6vkusov.Fragments.OrderFragment;
import com.example.alexandrzanko.mobile_6vkusov.Fragments.ProfileFragment;
import com.example.alexandrzanko.mobile_6vkusov.Fragments.SettingsFragment;
import com.example.alexandrzanko.mobile_6vkusov.Fragments.ViewPageAdapter;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Singleton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {


    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle, mNameUser, subTitle, bonusCount;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ProfileFragment profileFragment;
    private OrderFragment orderFragment;
    private GetPointsFragment getPointsFragment;
    private SettingsFragment settingsFragment;


    private CircleImageView circleImageView;

    private final String TAG = this.getClass().getSimpleName();
    private Singleton singleton = Singleton.currentState();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindActivity();
        mAppBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        mToolbar.setNavigationIcon(R.drawable.ic_menu);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }

        );

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout= (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        setupViewPager(viewPager);

        JSONObject profile = singleton.getUser().getProfile();
        try {
            String name = profile.getString("firstName");
            String email = profile.getString("email");
            String url =  this.getResources().getString(R.string.api_base) + profile.getString("img_path")+"/"+profile.getString("avatar");
            mTitle.setText(name);
            mNameUser.setText(name);
            subTitle.setText(email);
            Integer bonuses = singleton.getUser().getPoints();
            bonusCount.setText(bonuses.toString() + " баллов");
            Picasso.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_thumbs_up) //показываем что-то, пока не загрузится указанная картинка
                    .error(R.drawable.ic_thumb_down) // показываем что-то, если не удалось скачать картинку
                    .into(circleImageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());

        profileFragment = new ProfileFragment();
        orderFragment = new OrderFragment();
        getPointsFragment = new GetPointsFragment();
        settingsFragment = new SettingsFragment();

        adapter.addFragment(profileFragment, "Предложения");
        adapter.addFragment(orderFragment, "Заказы");
        adapter.addFragment(getPointsFragment, "Получить бонусы");
        adapter.addFragment(settingsFragment, "Настройки");

        viewPager.setAdapter(adapter);
    }

    private void bindActivity() {
        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mNameUser       = (TextView) findViewById(R.id.user_first_name);
        subTitle        = (TextView) findViewById(R.id.user_email_small);
        bonusCount      = (TextView) findViewById(R.id.bonus_count);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
        circleImageView = (CircleImageView)findViewById(R.id.user_icon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage > 0.0 && percentage < 1.0) {
            AppBarLayout.LayoutParams layoutParams = new AppBarLayout.LayoutParams(
                    AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, percentage < 0.77 ? 0 : 100, 0, 0);
            tabLayout.setLayoutParams(layoutParams);
        }

        Log.i(TAG, "onOffsetChanged: percentage = "  + percentage);
        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
