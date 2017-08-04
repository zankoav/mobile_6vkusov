package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.OrderFragment;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.ProfileFragment;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.ViewPageAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseMenuActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Singleton singleton;

    private ProfileFragment profileFragment;
    private OrderFragment orderFragment;

    private TextView userName, userEmail, userPhone;
    private CircleImageView userIcon;
    private Button btnChangeInfo, btnCallFriends;


    @Override
    protected void onResume() {
        super.onResume();
        userName.setText(singleton.getIUser().getFirstName());
        userEmail.setText(singleton.getIUser().getEmail());
        userPhone.setText(
                "+375"+
                        singleton.getIUser().getPhoneCode()+
                        singleton.getIUser().getPhoneNumber()
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews(this.getString(R.string.rest_menu));

        this.singleton = Singleton.currentState();

        userName = (TextView)findViewById(R.id.user_name);
        userEmail = (TextView)findViewById(R.id.user_email);
        userPhone = (TextView)findViewById(R.id.user_phone);
        userIcon = (CircleImageView)findViewById(R.id.user_icon_view);

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout= (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        btnChangeInfo = (Button)findViewById(R.id.user_change_data);
        btnCallFriends = (Button)findViewById(R.id.user_call_friends);

        buttonUnderlineText(btnChangeInfo);
        buttonUnderlineText(btnCallFriends);


        String avatarUrl = ApiController.BASE_URL +
                Singleton.currentState().getSettingsApp().getImage_path().getUser() +
                singleton.getIUser().getAvatar();
        Picasso.with(this)
                .load(avatarUrl)
                .placeholder(R.drawable.user) //показываем что-то, пока не загрузится указанная картинка
                .error(R.drawable.user) // показываем что-то, если не удалось скачать картинку
                .into(userIcon);

        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());

        profileFragment = new ProfileFragment();
        //orderFragment = new OrderFragment();

        adapter.addFragment(profileFragment, "Предложения");
       // adapter.addFragment(orderFragment, "Заказы");

        viewPager.setAdapter(adapter);
    }

    private void buttonUnderlineText(Button button){
        button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public void callFriend(View view) {
        Intent intent = new Intent(this, UserCallFriendsActivity.class);
        startActivity(intent);
    }

    public void changeSettings(View view) {
        Intent intent = new Intent(this, ChangeUserSettingsActivity.class);
        startActivity(intent);
    }
}
