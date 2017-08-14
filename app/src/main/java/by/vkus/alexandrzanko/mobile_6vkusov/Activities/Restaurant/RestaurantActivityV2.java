package by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.BasketActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.SessionStoreV2;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Trash.RestaurantsCardActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.CommentsFragmentV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.InfoFragmentV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.MenusFragmentV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.ViewPageAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MRestaurant;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class RestaurantActivityV2 extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private MRestaurant mRestaurant;
    private CounterFab fab;


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String slug;

    private MenusFragmentV2 menusFragmentV2;
    private InfoFragmentV2 infoFragmentV2;
    private CommentsFragmentV2 commentsFragmentV2;

    private TextView workingTime, nameRest, kitchenType, minDeliveryPrice, deliveryTime, commentsLike,commentsDislike;
    private ImageView imgView;
    private Button favorite;
    private boolean favoriteStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        slug = getIntent().getStringExtra(RestaurantsCardActivity.EXTRA_RESTAURANT);
        fab = (CounterFab) findViewById(R.id.fab);
        favorite = (Button)findViewById(R.id.favorite);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fab.getCount()>0){
                    Intent intent = new Intent(RestaurantActivityV2.this, BasketActivity.class);
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
                mRestaurant = response.body();
                workingTime = (TextView)findViewById(R.id.rest_working_time_text);
                workingTime.setText(mRestaurant.getWorking_time());
                nameRest = (TextView)findViewById(R.id.user_name);
                nameRest.setText(mRestaurant.getName());
                imgView = (ImageView)findViewById(R.id.restaurant_icon);

                Glide.with(RestaurantActivityV2.this)
                        .load(mRestaurant.getLogo())
                        .into(imgView);

//                Picasso.with(RestaurantActivityV2.this)
//                        .load(mRestaurant.getLogo())
//                        .placeholder(R.drawable.rest_icon) //показываем что-то, пока не загрузится указанная картинка
//                        .error(R.drawable.rest_icon) // показываем что-то, если не удалось скачать картинку
//                        .into(imgView);

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

        checkFavoriteButton(SingletonV2.currentState().getIUser());

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout= (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        addToolBarToScreen();
        setupViewPager(viewPager);

    }

    private void checkFavoriteButton(IUser user) {
        if(user.getStatus().equals(STATUS.REGISTER)){
            ApiController.getApi().checkFavoriteRestaurantsByUser(user.getSession(),slug).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.code() == 305){
                        favoriteStatus = true;
                        favorite.setBackground(ContextCompat.getDrawable(RestaurantActivityV2.this, R.drawable.ic_favorite_full));
                    }else if (response.code() == 306){
                        favorite.setBackground(ContextCompat.getDrawable(RestaurantActivityV2.this, R.drawable.ic_favorite));
                    }else{
                        favorite.setBackground(ContextCompat.getDrawable(RestaurantActivityV2.this, R.drawable.ic_favorite));
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    favorite.setBackground(ContextCompat.getDrawable(RestaurantActivityV2.this, R.drawable.ic_favorite));
                }
            });
        }else{
            SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
            Set<String> favorites = store.getStringSetValueStorage(store.USER_FAVORITES);
            if (favorites != null){
                if (favorites.contains(slug)){
                    favoriteStatus = true;
                    favorite.setBackground(ContextCompat.getDrawable(RestaurantActivityV2.this, R.drawable.ic_favorite_full));
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(SingletonV2.currentState().getIUser().getStatus().equals(STATUS.REGISTER)){
            ApiController.getApi().getCountOrderItemsByUser(SingletonV2.currentState().getIUser().getSession()).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.code() == 200){
                        fab.setCount(response.body());
                    }
                    Log.i(TAG, "onResponse: code " + response.code());
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.i(TAG, "onFailure: ");
                }
            });

        }else{
            SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
            String variants = store.getStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS);
            if (variants != null){
                try {
                    int count = 0;
                    JSONArray array = new JSONArray(variants);
                    for(int i =0 ; i < array.length(); i++){
                        JSONObject variant  = array.getJSONObject(i);
                        count += variant.getInt("count");
                    }
                    fab.setCount(count);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
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

        menusFragmentV2 = new MenusFragmentV2();
        menusFragmentV2.setSlug(slug);

        infoFragmentV2 = new InfoFragmentV2();
        infoFragmentV2.setSlug(slug);

        commentsFragmentV2 = new CommentsFragmentV2();
        commentsFragmentV2.setSlug(slug);

        adapter.addFragment(menusFragmentV2, "Меню");
        adapter.addFragment(infoFragmentV2, "Инфо");
        adapter.addFragment(commentsFragmentV2, "Отзывы");
        viewPager.setAdapter(adapter);
    }

    public void favoriteSend(View view) {
        IUser user = SingletonV2.currentState().getIUser();
        fakeFavoriteBtn();
        if(user.getStatus().equals(STATUS.REGISTER)){
            ApiController.getApi().changeFavoriteRestaurantsByUser(user.getSession(),slug).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.code() == 305){
                        favorite.setBackground(ContextCompat.getDrawable(RestaurantActivityV2.this, R.drawable.ic_favorite_full));
                    }else if (response.code() == 306){
                        favorite.setBackground(ContextCompat.getDrawable(RestaurantActivityV2.this, R.drawable.ic_favorite));
                    }else{
                        Log.i(TAG, "onFailure: " + response.code());
                        favorite.setBackground(ContextCompat.getDrawable(RestaurantActivityV2.this, R.drawable.ic_favorite));
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.i(TAG, "onFailure: ");
                    favorite.setBackground(ContextCompat.getDrawable(RestaurantActivityV2.this, R.drawable.ic_favorite));
                }
            });
        }else{
            SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
            Set<String> favorites = store.getStringSetValueStorage(store.USER_FAVORITES);
            if (favorites == null){
                Set<String> newSet = new HashSet<>();
                newSet.add(slug);
                store.setStringSetValueStorage(store.USER_FAVORITES, newSet);
            }else{
                if (favorites.contains(slug)){
                    favorites.remove(slug);
                }else{
                    favorites.add(slug);
                }
                store.setStringSetValueStorage(store.USER_FAVORITES, favorites);
            }

        }
    }

    private void fakeFavoriteBtn(){
        favoriteStatus = !favoriteStatus;
        if (favoriteStatus){
            favorite.setBackground(ContextCompat.getDrawable(RestaurantActivityV2.this, R.drawable.ic_favorite_full));
        }else{
            favorite.setBackground(ContextCompat.getDrawable(RestaurantActivityV2.this, R.drawable.ic_favorite));
        }
    }
}
