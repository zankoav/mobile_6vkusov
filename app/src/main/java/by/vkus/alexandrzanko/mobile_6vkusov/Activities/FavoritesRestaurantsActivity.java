package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.os.Bundle;

import by.vkus.alexandrzanko.mobile_6vkusov.R;

public class FavoritesRestaurantsActivity extends BaseMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_restaurants);
        initViews(this.getString(R.string.favorites_restaurants_title));
    }
}
