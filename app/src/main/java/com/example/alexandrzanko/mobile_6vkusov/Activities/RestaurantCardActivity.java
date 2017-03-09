package com.example.alexandrzanko.mobile_6vkusov.Activities;

import android.animation.Animator;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.alexandrzanko.mobile_6vkusov.Adapters.RestaurantRecycleAdapter;
import com.example.alexandrzanko.mobile_6vkusov.Models.Restaurant;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Singleton;

import java.util.ArrayList;

public class RestaurantCardActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    public static final String EXTRA_RESTAURANT = "Restaurant";
    private Singleton singleton = Singleton.currentState();
    private ArrayList<Restaurant> restaurants;
    private LinearLayout filterView, allView;
    private boolean menuFilterOpen = false;


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RestaurantRecycleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_new);
        addToolBarToScreen();
        Bundle bundle = getIntent().getExtras();
        String slug = bundle.getString("slug");
        restaurants = singleton.getStore().getRestaurants(slug);
        filterView = (LinearLayout)findViewById(R.id.filter_view);
        allView = (LinearLayout)findViewById(R.id.all_view);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RestaurantRecycleAdapter(restaurants,this);
        recyclerView.setAdapter(adapter);
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
                allView.animate().translationY(filterView.getHeight()).setDuration(600).setListener(new Animator.AnimatorListener() {
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
                item.setIcon(R.drawable.ic_filter_full);
            }else{
                allView.animate().translationY(0).setDuration(600).setListener(new Animator.AnimatorListener() {
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
                item.setIcon(R.drawable.ic_filter_clear);
                menuFilterOpen = false;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
