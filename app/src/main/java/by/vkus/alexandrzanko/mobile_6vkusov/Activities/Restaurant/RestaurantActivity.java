package by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.BasketActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Activities.RestaurantsCardActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.CommentsFragment;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.InfoFragment;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.MenusFragment;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.ViewPageAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MRestaurant;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Product;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.ProductItem;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Restaurant;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Variant;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.BasketViewInterface;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.andremion.counterfab.CounterFab;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RestaurantActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private MRestaurant mRestaurant;
    private CounterFab fab;


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String slug;

    private MenusFragment menusFragment;
    private InfoFragment infoFragment;
    private CommentsFragment commentsFragment;

    private TextView workingTime, nameRest, kitchenType, minDeliveryPrice, deliveryTime, commentsLike,commentsDislike;
    private ImageView imgView;
    private Button favorite;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        slug = getIntent().getStringExtra(RestaurantsCardActivity.EXTRA_RESTAURANT);
        fab = (CounterFab) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Singleton.currentState().getIUser().getBasket().getCountItems();
                if (count>0){
                    Intent intent = new Intent(RestaurantActivity.this, BasketActivity.class);
                    startActivity(intent);
                }else{
                    Snackbar.make(view, "Ваша корзина пуста", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });
        ApiController.getApi().getRestaurantBySlug(slug).enqueue(new Callback<MRestaurant>() {
            @Override
            public void onResponse(Call<MRestaurant> call, Response<MRestaurant> response) {
                Log.i(TAG, "onResponse: " + response.code());
                mRestaurant = response.body();
                if (mRestaurant != null){
                    Log.i(TAG, "onResponse: restName " + mRestaurant.getName());
                }

                workingTime = (TextView)findViewById(R.id.rest_working_time_text);
                workingTime.setText(mRestaurant.getWorking_time());
                nameRest = (TextView)findViewById(R.id.user_name);
                nameRest.setText(mRestaurant.getName());
                imgView = (ImageView)findViewById(R.id.restaurant_icon);

                Picasso.with(RestaurantActivity.this)
                        .load(mRestaurant.getLogo())
                        .placeholder(R.drawable.rest_icon) //показываем что-то, пока не загрузится указанная картинка
                        .error(R.drawable.rest_icon) // показываем что-то, если не удалось скачать картинку
                        .into(imgView);

                kitchenType = (TextView)findViewById(R.id.user_email);
                kitchenType.setText(mRestaurant.getKitchens());
                minDeliveryPrice = (TextView)findViewById(R.id.rest_order_price);
                String price = Validation.twoNumbersAfterAfterPoint(mRestaurant.getMinimal_price());
                minDeliveryPrice.setText(price + " руб");
                deliveryTime = (TextView)findViewById(R.id.rest_working_time_text);
                deliveryTime.setText(mRestaurant.getDelivery_time());
                commentsLike = (TextView)findViewById(R.id.restaurant_likes);
                commentsDislike = (TextView)findViewById(R.id.restaurant_dislikes);
                commentsLike.setText(mRestaurant.getLikes() + "");
                commentsDislike.setText(mRestaurant.getDislikes() + "");
            }

            @Override
            public void onFailure(Call<MRestaurant> call, Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout= (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        addToolBarToScreen();
        setupViewPager(viewPager);

    }

    @Override
    public void onResume() {
        super.onResume();
        int count = Singleton.currentState().getIUser().getBasket().getCountItems();
        fab.setCount(count);
    }

    private void addToolBarToScreen() {
        toolbar = (Toolbar)findViewById(R.id.toolbar_actionbar_restaurant);
        toolbar.setTitle(by.vkus.alexandrzanko.mobile_6vkusov.R.string.restaurants_title);
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
        menusFragment.setSlug(slug);

        infoFragment = new InfoFragment();
        infoFragment.setSlug(slug);

        commentsFragment = new CommentsFragment();
        commentsFragment.setSlug(slug);

        adapter.addFragment(menusFragment, "Меню");
        adapter.addFragment(infoFragment, "Инфо");
        adapter.addFragment(commentsFragment, "Отзывы");
        viewPager.setAdapter(adapter);
    }

    public void favoriteSend(View view) {
        Log.i(TAG, "favoriteSend: ");
    }
}
