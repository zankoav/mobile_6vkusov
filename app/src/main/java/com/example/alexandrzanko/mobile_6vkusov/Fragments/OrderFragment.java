package com.example.alexandrzanko.mobile_6vkusov.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantActivity;
import com.example.alexandrzanko.mobile_6vkusov.Adapters.OrderAdapter;
import com.example.alexandrzanko.mobile_6vkusov.Models.OrderItem;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Singleton;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alexandrzanko on 5/2/17.
 */

public class OrderFragment extends Fragment implements LoadJson {

    private String slug;
    private OrderAdapter adapter;
    private ArrayList<OrderItem> orders;

    private final String TAG = this.getClass().getSimpleName();



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant_comments_layout, container, false);
        return rootView;

    }

    static ListView listView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String url = getContext().getResources().getString(R.string.api_orders);
        JSONObject params = new JSONObject();
        try {
            JSONObject userProfileJson = Singleton.currentState().getUser().getProfile();
            String session = userProfileJson.getString("session");
            params.put("session", session);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        (new JsonHelperLoad(url,params,this,"orders")).execute();
    }

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        Log.i(TAG, "loadComplete: obj = "  + obj);
        //adapter = new OrderAdapter(getContext(),orders);
    }


}