package com.example.alexandrzanko.mobile_6vkusov.Activities.Restaurant;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alexandrzanko.mobile_6vkusov.Activities.Trash.RestaurantsActivity;
import com.example.alexandrzanko.mobile_6vkusov.Fragments.CommentsFragment;
import com.example.alexandrzanko.mobile_6vkusov.Fragments.InfoFragment;
import com.example.alexandrzanko.mobile_6vkusov.Fragments.MenusFragment;
import com.example.alexandrzanko.mobile_6vkusov.Fragments.ViewPageAdapter;
import com.example.alexandrzanko.mobile_6vkusov.Models.Restaurant;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Singleton;
import com.squareup.picasso.Picasso;

public class RestaurantActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Restaurant restaurant;
    private Singleton singleton;

    private MenusFragment menusFragment;
    private InfoFragment infoFragment;
    private CommentsFragment commentsFragment;

    private TextView workingTime, nameRest, kitchenType, minDeliveryPrice, deliveryTime, deliveryPrice, commentsLike,commentsDislike;
    private ImageView imgView;
    private RelativeLayout notificationCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Log.i(TAG, "onCreate: ");
        this.singleton = Singleton.currentState();
        String slug = getIntent().getStringExtra(RestaurantsActivity.EXTRA_RESTAURANT);
        Log.i(TAG, slug);
        this.restaurant = singleton.getStore().getRestaurantBySlug(slug);
        Log.i(TAG, restaurant.get_name());
        loadHeaderRestaurant();
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout= (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        addToolBarToScreen();
    }

    private void loadHeaderRestaurant() {
        workingTime = (TextView)findViewById(R.id.rest_working_time_text);
        workingTime.setText(restaurant.get_working_time());
        nameRest = (TextView)findViewById(R.id.rest_name);
        nameRest.setText(restaurant.get_name());
        imgView = (ImageView)findViewById(R.id.restaurant_icon);

        Picasso.with(this)
                .load(restaurant.get_iconURL())
                .placeholder(R.drawable.ic_thumbs_up) //показываем что-то, пока не загрузится указанная картинка
                .error(R.drawable.ic_thumb_down) // показываем что-то, если не удалось скачать картинку
                .into(imgView);

        kitchenType = (TextView)findViewById(R.id.rest_kitchen_type);
        kitchenType.setText(join(", ",restaurant.get_kitchens()));
        minDeliveryPrice = (TextView)findViewById(R.id.rest_order_price);
        String text = (String) getResources().getText(R.string.rest_min_delivery_price);
        minDeliveryPrice.setText(text + " " + new Double(restaurant.get_minimal_price()).toString() + " руб");
        deliveryTime = (TextView)findViewById(R.id.rest_delivery_time);
        deliveryTime.setText(restaurant.get_delivery_time() + " мин.");
        deliveryPrice = (TextView)findViewById(R.id.rest_delivery_price);
        deliveryPrice.setText(getResources().getText(R.string.rest_delivery_price_free));
        commentsLike = (TextView)findViewById(R.id.restaurant_likes);
        commentsDislike = (TextView)findViewById(R.id.restaurant_dislikes);
        commentsLike.setText(restaurant.get_comments().get("likes") + "");
        commentsDislike.setText(restaurant.get_comments().get("dislikes") + "");
    }

    private void addToolBarToScreen() {
        toolbar = (Toolbar)findViewById(R.id.toolbar_actionbar_restaurant);
        toolbar.setTitle(R.string.restaurants_title);
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


    private void setupViewPager(ViewPager viewPager){
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());

        menusFragment = new MenusFragment();
        commentsFragment = new CommentsFragment();
        infoFragment = new InfoFragment();

        adapter.addFragment(menusFragment, "Меню");
        adapter.addFragment(infoFragment, "Инфо");
        adapter.addFragment(commentsFragment, "Отзывы");
        viewPager.setAdapter(adapter);

    }

    public Restaurant getRestaurant(){
        return restaurant;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.shopping_cart);
        MenuItemCompat.setActionView(item, R.layout.menu_basket_icon);
        notificationCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        notificationCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: basket button");
//                Intent intent = new Intent(RestaurantActivity.this, BasketActivity.class);
//                startActivityForResult(intent,1);
            }
        });
        this.updateCountBasket();
        return super.onCreateOptionsMenu(menu);
    }

    private void updateCountBasket(){
        TextView countProducts = (TextView) notificationCount.findViewById(R.id.notif_count);
        int count = 1;//this.singleton.getUser().getBasket().getCountProducts();
        if (count>0){
            countProducts.setText(count + "");
            countProducts.setVisibility(View.VISIBLE);
        }else{
            countProducts.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public String join(String join, String... strings) {
        if (strings == null || strings.length == 0) {
            return "";
        } else if (strings.length == 1) {
            return strings[0];
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(strings[0]);
            for (int i = 1; i < strings.length; i++) {
                sb.append(join).append(strings[i]);
            }
            return sb.toString();
        }
    }
}
