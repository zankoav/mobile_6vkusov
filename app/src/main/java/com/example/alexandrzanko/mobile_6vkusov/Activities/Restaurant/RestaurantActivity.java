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

import com.example.alexandrzanko.mobile_6vkusov.Activities.BasketActivity;
import com.example.alexandrzanko.mobile_6vkusov.Activities.Trash.RestaurantsActivity;
import com.example.alexandrzanko.mobile_6vkusov.Fragments.CommentsFragment;
import com.example.alexandrzanko.mobile_6vkusov.Fragments.InfoFragment;
import com.example.alexandrzanko.mobile_6vkusov.Fragments.MenusFragment;
import com.example.alexandrzanko.mobile_6vkusov.Fragments.ViewPageAdapter;
import com.example.alexandrzanko.mobile_6vkusov.Models.Product;
import com.example.alexandrzanko.mobile_6vkusov.Models.ProductItem;
import com.example.alexandrzanko.mobile_6vkusov.Models.Restaurant;
import com.example.alexandrzanko.mobile_6vkusov.Models.Variant;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Singleton;
import com.example.alexandrzanko.mobile_6vkusov.Users.BasketViewInterface;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantActivity extends AppCompatActivity implements LoadJson, BasketViewInterface {

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
    private ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        this.singleton = Singleton.currentState();
        singleton.getUser().getBasket().setDelegateContext(this);
        String slug = getIntent().getStringExtra(RestaurantsActivity.EXTRA_RESTAURANT);
        this.restaurant = singleton.getStore().getRestaurantBySlug(slug);
        loadHeaderRestaurant();
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout= (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        addToolBarToScreen();

        JSONObject params = new JSONObject();
        try {
            params.put("slug", slug);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = this.getResources().getString(R.string.api_food);
        new JsonHelperLoad(url, params, this, getResources().getString(R.string.api_food)).execute();

    }

    @Override
    public void onResume() {
        super.onResume();
        Singleton.currentState().getUser().getBasket().initBasketFromRegisterUser();
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

        menusFragment = new MenusFragment(categories);
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
                if (Singleton.currentState().getUser().getBasket().getTotalCount() == 0){
                    return;
                }
                Intent intent = new Intent(RestaurantActivity.this, BasketActivity.class);
                startActivityForResult(intent,1);
            }
        });
        this.updateCountBasket();
        return super.onCreateOptionsMenu(menu);
    }

    private void updateCountBasket(){
        TextView countProducts = (TextView) notificationCount.findViewById(R.id.notif_count);
        int count = this.singleton.getUser().getBasket().getTotalCount();
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

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        if (obj != null){
            try {
                Log.i(TAG, "loadComplete: " + obj.toString());
                String status = obj.getString("status");
                if (status.equals("successful")){
                    String img_path = obj.getString("img_path");
                    JSONArray prod = obj.getJSONArray("food");
                    Singleton.currentState().getStore().currentProducts = initProducts(prod,img_path);
                    categories = initCategories(Singleton.currentState().getStore().currentProducts);
                    setupViewPager(viewPager);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "loadComplete: error" + e);
            }
        }
    }

    private ArrayList<Product> initProducts(JSONArray prod, String img_path){
        ArrayList<Product> prods = new ArrayList<>();
        String baseUrl = getResources().getString(R.string.api_base);
        for(int i=0; i < prod.length(); i++){
            try {
                JSONObject prJson = prod.getJSONObject(i);
                int id = prJson.getInt("id");
                String name = prJson.getString("name");
                int points = 0;
                if (!prJson.getString("points").equals("null")){
                    points = prJson.getInt("points");
                }
                String icon = baseUrl + img_path + "/" + prJson.getString("image");
                String description = prJson.getString("description");
                JSONObject categoryJson = prJson.getJSONObject("category");
                HashMap<String,String> category = new HashMap<>();
                category.put("name",categoryJson.getString("name"));
                category.put("slug",categoryJson.getString("slug"));

                JSONArray variantsJson = prJson.getJSONArray("variants");
                ArrayList<Variant> variants = new ArrayList<>();

                for (int j = 0; j< variantsJson.length(); j++){

                    JSONObject variantJson = variantsJson.getJSONObject(j);
                    int idVariant = variantJson.getInt("id");
                    double priceVariant = variantJson.getDouble("price");
                    String sizeVariant = variantJson.getString("size");
                    String weightVariant = variantJson.getString("weigth");
                    int countVariant = 1;
                    if(variantsJson.length() > 1){
                        countVariant = 0;
                    }

                    Variant variant = new Variant(idVariant,priceVariant, sizeVariant, weightVariant, countVariant);
                    variants.add(variant);
                }

                Product product = new Product(id, name, icon, description, points, category, variants);
                prods.add(product);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "initProducts: error" + e);
            }
        }
        return prods;
    }

    private ArrayList<String> initCategories(ArrayList<Product> prods){
        ArrayList<String> categories = new ArrayList<>();
        String freeFood = null;
        for(int i = 0; i < prods.size(); i++){
            String name = prods.get(i).get_category().get("name");
            int points = prods.get(i).get_points();
            if (points > 0){
                freeFood = "Еда за баллы";
            }
            if (!categories.contains(name)){
                categories.add(name);
            }
        }
        if (freeFood != null){
            categories.add(0,freeFood);
        }
        return categories;
    }

    @Override
    public void updateBasket(int count) {
        updateCountBasket();
    }

    @Override
    public void showAlert(ProductItem product, String slug) {

    }

    @Override
    public void showAlertNewOrder(Product product, String slug) {

    }
}
