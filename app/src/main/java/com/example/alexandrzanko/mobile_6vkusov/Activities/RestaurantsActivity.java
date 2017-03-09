package com.example.alexandrzanko.mobile_6vkusov.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.alexandrzanko.mobile_6vkusov.Adapters.RestaurantsListAdapter;
import com.example.alexandrzanko.mobile_6vkusov.Models.Restaurant;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Singleton;

import java.util.ArrayList;

public class RestaurantsActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private ListView listView;
    private RestaurantsListAdapter adapter;
    public static final String EXTRA_RESTAURANT = "Restaurant";
    private Singleton singleton = Singleton.currentState();
    private ArrayList<Restaurant> restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        addToolBarToScreen();
        Bundle bundle = getIntent().getExtras();
        String slug = bundle.getString("slug");
        restaurants = singleton.getStore().getRestaurants(slug);
        listView = (ListView) findViewById(R.id.restaurants_list);
        adapter = new RestaurantsListAdapter(RestaurantsActivity.this, restaurants);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                Restaurant restaurant = (Restaurant)listView.getItemAtPosition(position);
                Log.i(TAG, "onItemClick: " + restaurant.getName());
//                Intent intent = new Intent(RestaurantsActivity.this, RestaurantActivity.class);
//                intent.putExtra(EXTRA_RESTAURANT, restaurant.getSlug());
//                startActivity(intent);
            }
        });
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

}
