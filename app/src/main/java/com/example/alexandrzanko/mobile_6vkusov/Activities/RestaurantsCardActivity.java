package com.example.alexandrzanko.mobile_6vkusov.Activities;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.alexandrzanko.mobile_6vkusov.Adapters.RestaurantRecycleAdapter;
import com.example.alexandrzanko.mobile_6vkusov.Models.ProductItem;
import com.example.alexandrzanko.mobile_6vkusov.Models.Restaurant;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Singleton;
import com.example.alexandrzanko.mobile_6vkusov.Users.STATUS;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestaurantsCardActivity extends AppCompatActivity implements LoadJson{

    private final String TAG = this.getClass().getSimpleName();
    public static final String EXTRA_RESTAURANT = "Restaurant";
    private Singleton singleton = Singleton.currentState();
    private ArrayList<Restaurant> restaurants;
    private LinearLayout filterView, allView;
    private boolean menuFilterOpen = false;
    private SeekBar seekBar;
    private TextView seekBarResult;
    private CheckBox checkNew, checkFreeFood, checkFlash, checkSale;
    private int toolBarHeight;

    private MenuItem search;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RestaurantRecycleAdapter adapter;

    private boolean isFavorite;

    @Override
    public void onResume() {
        super.onResume();
        if (isFavorite) {
            Singleton.currentState().getUser().getBasket().initBasketFromRegisterUser();
            if (Singleton.currentState().getUser().getStatus() == STATUS.REGISTER) {
                JSONObject params = new JSONObject();
                String url = this.getResources().getString(R.string.api_favourites);
                try {
                    params.put("session", Singleton.currentState().getUser().getProfile().getString("session"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "onResume: JsonHelperLoad starts");
                (new JsonHelperLoad(url, params, this, "favorites")).execute();
            } else {
                ArrayList<String> slugs = Singleton.currentState().getStore().getAllSlugs();
                if (slugs != null) {
                    restaurants = Singleton.currentState().getStore().getFavoriteRestaurants(slugs);
                    adapter = new RestaurantRecycleAdapter(restaurants,this);
                    recyclerView.setAdapter(adapter);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_new);
        toolBarHeight = Build.VERSION.SDK_INT > 18 ? 0: getToolBarHeight();
        addToolBarToScreen();
        Bundle bundle = getIntent().getExtras();
        isFavorite = bundle.getBoolean("favorite");
        restaurants = new ArrayList<>();
        if (!isFavorite){
            String slug = bundle.getString("slug");
            restaurants = singleton.getStore().getRestaurants(slug);
        }
        initViews();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RestaurantRecycleAdapter(restaurants,this);
        recyclerView.setAdapter(adapter);
        allView.animate().translationY(toolBarHeight).setDuration(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {}

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }

    public int getToolBarHeight() {
        int[] attrs = new int[] {R.attr.actionBarSize};
        TypedArray ta = this.obtainStyledAttributes(attrs);
        int toolBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
        return toolBarHeight;
    }

    private void initViews(){
        filterView = (LinearLayout)findViewById(R.id.filter_view);
        allView = (LinearLayout)findViewById(R.id.all_view);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        seekBarResult = (TextView) findViewById(R.id.seekBar_result);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        int maxPrice = (int)getMaxPrice() + 20;
        seekBarResult.setText("0 руб.");
        seekBar.setMax(maxPrice);
        seekBar.setProgress(0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                seekBarResult.setText(progress + " руб");
                changeIconFilter();
                adapter.setPriceMin(progress);
            }
        });


        checkNew = (CheckBox) findViewById(R.id.check_box_new);
        checkNew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeIconFilter();
                adapter.setRule(adapter.NEW, isChecked);
            }
        });
        checkFreeFood = (CheckBox) findViewById(R.id.check_box_free_food);
        checkFreeFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeIconFilter();
                adapter.setRule(adapter.FREE_FOOD, isChecked);
            }
        });
        checkFlash = (CheckBox) findViewById(R.id.check_box_flash);
        checkFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeIconFilter();
                adapter.setRule(adapter.FLASH, isChecked);
            }
        });
        checkSale = (CheckBox) findViewById(R.id.check_box_sale);
        checkSale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeIconFilter();
                adapter.setRule(adapter.SALE, isChecked);
            }
        });
    }


    private void changeIconFilter(){
        if(isFilterFull()){
           search.setIcon(R.drawable.ic_filter_full);
        }else{
            search.setIcon(R.drawable.ic_filter_clear);
        }
    }

    private boolean isFilterFull(){
        boolean checkNew = this.checkNew.isChecked();
        boolean checkFreeFood = this.checkFreeFood.isChecked();
        boolean checkFlash = this.checkFlash.isChecked();
        boolean checkSale = this.checkSale.isChecked();
        boolean seekBar = this.seekBar.getProgress() > 0 ? true: false;
        return checkNew || checkFreeFood || checkFlash || checkSale || seekBar;
    }

    private double getMaxPrice(){
        double max = 0;
        if(restaurants.size()>0){
            max = restaurants.get(0).get_minimal_price();
            for (int i = 0 ; i< restaurants.size(); i++){
                if(max < restaurants.get(i).get_minimal_price()){
                    max = restaurants.get(i).get_minimal_price();
                }
            }
        }
        return max;
    }

    private void addToolBarToScreen() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        search = menu.findItem(R.id.menu_filter);
        SearchView searchView = (SearchView) item.getActionView();
        EditText editText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextColor(Color.WHITE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_filter) {
            if(!menuFilterOpen){
                allView.animate().translationY(filterView.getHeight() + toolBarHeight).setDuration(600).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                menuFilterOpen = true;
            }else{
                allView.animate().translationY(toolBarHeight).setDuration(600).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                menuFilterOpen = false;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        Log.i(TAG, "loadComplete: obj = " + obj);
        if (obj != null){
            try {
                String status = obj.getString("status");
                if (status.equals("successful")){
                        JSONArray array = obj.getJSONArray("slugs");
                        restaurants = new ArrayList<>();
                        for(int i = 0; i < array.length(); i++){
                            restaurants.add(Singleton.currentState().getStore().getRestaurantBySlug(array.getString(i)));
                        }
                        adapter = new RestaurantRecycleAdapter(restaurants,this);
                        recyclerView.setAdapter(adapter);
                }
            } catch (JSONException e) {
                Log.i(TAG, "loadComplete: error" + e);
                e.printStackTrace();
            }
        }else{
            Log.i(TAG, "loadComplete: obj = null");
        }
    }
}
