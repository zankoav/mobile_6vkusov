package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.PromoAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.ProfileFragment;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.FoodByPoint;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FreeFoodActivity extends BaseMenuActivity {

    private PromoAdapter adapter;
    private List<FoodByPoint> foodByPoints;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_food);
        initViews(this.getString(R.string.free_food_title));
        listView = (ListView) findViewById(R.id.listView_free_food);

        ApiController.getApi().getFoodsByPoint().enqueue(new Callback<List<FoodByPoint>>() {

            @Override
            public void onResponse(Call<List<FoodByPoint>> call, Response<List<FoodByPoint>> response) {
                foodByPoints = response.body();
                adapter  = new PromoAdapter(FreeFoodActivity.this, foodByPoints);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<FoodByPoint>> call, Throwable t) {

            }
        });
    }
}
