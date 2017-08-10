package by.vkus.alexandrzanko.mobile_6vkusov.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.andremion.counterfab.CounterFab;

import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.PromoAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.FoodByPoint;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

/**
 * Created by alexandrzanko on 5/2/17.
 */

public class ProfileFragmentV2 extends Fragment {

    private PromoAdapter adapter;
    private List<FoodByPoint> foodByPoints;
    static ListView listView;


    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApiController.getApi().getFoodsByPoint().enqueue(new Callback<List<FoodByPoint>>() {

            @Override
            public void onResponse(Call<List<FoodByPoint>> call, Response<List<FoodByPoint>> response) {
                foodByPoints = response.body();
                adapter  = new PromoAdapter(ProfileFragmentV2.this.getActivity(), foodByPoints);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<FoodByPoint>> call, Throwable t) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.free_food_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.listView_free_food);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }
    }

}
