package by.vkus.alexandrzanko.mobile_6vkusov.Activities.Trash;

import android.animation.Animator;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;


import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import by.vkus.alexandrzanko.mobile_6vkusov.Models.Restaurant;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;

import java.util.ArrayList;

public class RestaurantsActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private ListView listView;
    private RestaurantsListAdapter adapter;
    public static final String EXTRA_RESTAURANT = "Restaurant";
    private Singleton singleton = Singleton.currentState();
    private ArrayList<Restaurant> restaurants;
    private LinearLayout filterView, allView;



    private boolean menuFilterOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        addToolBarToScreen();
        Bundle bundle = getIntent().getExtras();
        String slug = bundle.getString("slug");
        restaurants = singleton.getStore().getRestaurants(slug);
        listView = (ListView) findViewById(R.id.restaurants_list);
        filterView = (LinearLayout)findViewById(R.id.filter_view);
        allView = (LinearLayout)findViewById(R.id.all_view);
        adapter = new RestaurantsListAdapter(RestaurantsActivity.this, restaurants);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                Restaurant restaurant = (Restaurant)listView.getItemAtPosition(position);
                Log.i(TAG, "onItemClick: " + restaurant.get_name());
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



