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
import by.vkus.alexandrzanko.mobile_6vkusov.Models.FreeFood;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alexandrzanko on 5/2/17.
 */

public class ProfileFragment extends Fragment implements LoadJson {

    private String slug;
    private PromoAdapter adapter;
    private ArrayList<FreeFood> freeFoods;

    private final String TAG = this.getClass().getSimpleName();



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JSONObject params = new JSONObject();
        (new JsonHelperLoad(getResources().getString(R.string.api_food_points), params, this, null)).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.free_food_fragment, container, false);
        return rootView;

    }

    static ListView listView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.listView_free_food);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }
        if(freeFoods != null){
            adapter  = new PromoAdapter(getActivity(), freeFoods);
            listView.setAdapter(adapter);
        }
    }
    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        if(obj != null){
            String status = "error";
            try {
                status = obj.getString("status");
                if (status.equals("successful")){
                    this.freeFoods = new ArrayList<>();
                    String baseUrl = getResources().getString(R.string.api_base);
                    String image_path = obj.getString("image_path");
                    JSONArray items = obj.getJSONArray("items");
                    for (int i = 0; i < items.length(); i++){
                        JSONObject itemJson = items.getJSONObject(i);
                        String name = itemJson.getString("name");
                        String restaurant_slug = itemJson.getString("restaurant_slug");
                        String icon = itemJson.getString("icon");
                        String description = itemJson.getString("description");
                        int points = itemJson.getInt("points");
                        String url = baseUrl+image_path+"/"+icon;
                        FreeFood food = new FreeFood(name, restaurant_slug, description, url, points);
                        this.freeFoods.add(food);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter = new PromoAdapter(this.getContext(), freeFoods);
        listView.setAdapter(adapter);
    }
}
