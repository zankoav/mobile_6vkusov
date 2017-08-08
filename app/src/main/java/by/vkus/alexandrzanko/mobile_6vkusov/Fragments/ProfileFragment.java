package by.vkus.alexandrzanko.mobile_6vkusov.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.CommentsAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.PromoAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.FoodByPoint;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.FreeFood;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandrzanko on 5/2/17.
 */

public class ProfileFragment extends Fragment {

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
                adapter  = new PromoAdapter(ProfileFragment.this.getActivity(), foodByPoints);
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
