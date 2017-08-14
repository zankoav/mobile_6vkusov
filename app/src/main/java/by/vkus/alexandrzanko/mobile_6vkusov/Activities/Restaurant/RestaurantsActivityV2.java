package by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.List;
import java.util.Set;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.BaseMenuActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.RestaurantRecycleAdapterV2;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MRestaurant;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SessionStoreV2;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsActivityV2 extends BaseMenuActivity {

    public static final String EXTRA_FAVORITE = "Favorite";
    public static final String EXTRA_SLUG = "Slug";
    public static final String EXTRA_CATEGORY_NAME = "CategoryName";
    public static final String EXTRA_RESTAURANT = "Restaurant";


    private Boolean favorite;
    private String categorySlug;
    private String categoryName;


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RestaurantRecycleAdapterV2 adapter;
    private List<MRestaurant> restaurantList;


    @Override
    protected void onResume() {
        super.onResume();
        if (favorite){
            if (restaurantList != null){
                restaurantList.clear();
                adapter.notifyDataSetChanged();
            }
            loadFavoritesRestaurants();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_v2);
        recyclerView = (RecyclerView)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_recycler_view);
        favorite = getIntent().getBooleanExtra(EXTRA_FAVORITE, false);
        if (favorite){
            initViews(this.getString(R.string.favorites_restaurants_title));
        }else{
            categorySlug = getIntent().getStringExtra(EXTRA_SLUG);
            categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
            initViews(categoryName);
            loadRestaurantsBySlug(categorySlug);
        }

    }

    private void loadRestaurantsBySlug(String categorySlug) {
        ApiController.getApi().getRestaurantsBySlug(categorySlug).enqueue(new Callback<List<MRestaurant>>() {
            @Override
            public void onResponse(Call<List<MRestaurant>> call, Response<List<MRestaurant>> response) {
                if (response.code() == 200){
                    restaurantList = response.body();
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT
                    );
                    layoutManager = new LinearLayoutManager(RestaurantsActivityV2.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setLayoutParams(params);

                    adapter = new RestaurantRecycleAdapterV2(restaurantList, RestaurantsActivityV2.this);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<MRestaurant>> call, Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });
    }

    private void loadFavoritesRestaurants() {
        IUser user = SingletonV2.currentState().getIUser();
        if (user.getStatus().equals(STATUS.REGISTER)){
            Log.i(TAG, "loadFavoritesRestaurants: " + user.getSession());
            ApiController.getApi().getFavoriteRestaurantsByUser(user.getSession()).enqueue(new Callback<List<MRestaurant>>() {
                @Override
                public void onResponse(Call<List<MRestaurant>> call, Response<List<MRestaurant>> response) {
                    if (response.code() == 200){
                        restaurantList = response.body();
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT
                        );
                        layoutManager = new LinearLayoutManager(RestaurantsActivityV2.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setLayoutParams(params);

                        adapter = new RestaurantRecycleAdapterV2(restaurantList, RestaurantsActivityV2.this);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<List<MRestaurant>> call, Throwable t) {
                    Log.i(TAG, "onFailure: ");
                }
            });
        }else{
            SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
            Set<String> favorites = store.getStringSetValueStorage(store.USER_FAVORITES);
            if (favorites != null){
                ApiController.getApi().getFavoriteRestaurantsBySlugs(new Gson().toJson(favorites)).enqueue(new Callback<List<MRestaurant>>() {
                    @Override
                    public void onResponse(Call<List<MRestaurant>> call, Response<List<MRestaurant>> response) {
                        if (response.code() == 200){
                            restaurantList = response.body();
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.MATCH_PARENT
                            );
                            layoutManager = new LinearLayoutManager(RestaurantsActivityV2.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setLayoutParams(params);

                            adapter = new RestaurantRecycleAdapterV2(restaurantList, RestaurantsActivityV2.this);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MRestaurant>> call, Throwable t) {
                        Log.i(TAG, "onFailure: ");
                    }
                });
            }
        }
    }
}
