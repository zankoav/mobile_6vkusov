package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.os.Bundle;

import by.vkus.alexandrzanko.mobile_6vkusov.R;

public class FreeFoodActivity extends BaseMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_food);
        initViews(this.getString(R.string.free_food_title));
    }
}
