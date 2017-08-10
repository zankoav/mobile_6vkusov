package by.vkus.alexandrzanko.mobile_6vkusov.Activities.ProfileActivities;

import android.content.Intent;
import android.graphics.Paint;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import com.squareup.picasso.Picasso;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.BaseMenuActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.OrderFragment;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.ProfileFragmentV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.ViewPageAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivityV2 extends BaseMenuActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SingletonV2 singletonV2;

    private ProfileFragmentV2 profileFragmentV2;
    private OrderFragment orderFragment;

    private TextView userName, userEmail, userPhone;
    private CircleImageView userIcon;
    private Button btnChangeInfo, btnCallFriends;


    @Override
    protected void onResume() {
        super.onResume();

        userName.setText(singletonV2.getIUser().getFirstName());
        userEmail.setText(singletonV2.getIUser().getEmail());
        if(singletonV2.getIUser().getPhoneNumber() != null){
            userPhone.setText(
                    "+375"+
                            singletonV2.getIUser().getPhoneCode()+
                            singletonV2.getIUser().getPhoneNumber()
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews(this.getString(R.string.rest_menu));

        this.singletonV2 = SingletonV2.currentState();

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

        Log.i(TAG, "onCreate: " + singletonV2.getIUser().getAvatar());
        Picasso.with(this)
                .load(singletonV2.getIUser().getAvatar())
                .placeholder(R.drawable.user) //показываем что-то, пока не загрузится указанная картинка
                .error(R.drawable.user) // показываем что-то, если не удалось скачать картинку
                .into(userIcon);

        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());

        profileFragmentV2 = new ProfileFragmentV2();
        adapter.addFragment(profileFragmentV2, "Предложения");

        //orderFragment = new OrderFragment();
       // adapter.addFragment(orderFragment, "Заказы");

        viewPager.setAdapter(adapter);
    }

    private void buttonUnderlineText(Button button){
        button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public void callFriend(View view) {
        Intent intent = new Intent(this, UserCallFriendsActivityV2.class);
        startActivity(intent);
    }

    public void changeSettings(View view) {
        Intent intent = new Intent(this, ChangeUserSettingsActivityV2.class);
        startActivity(intent);
    }
}
