package com.example.alexandrzanko.mobile_6vkusov.Fragments;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.alexandrzanko.mobile_6vkusov.Activities.Restaurant.ProductActivity;
import com.example.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantActivity;
import com.example.alexandrzanko.mobile_6vkusov.Models.Product;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Singleton;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexandrzanko on 29/11/16.
 */

public class MenusFragment extends Fragment {

    private String slug;

    private final String TAG = this.getClass().getSimpleName();

    public static final String EXTRA_CATEGORIES = "Categories";
    public static final String EXTRA_CATEGORY = "Category";
    public static final String EXTRA_SLUG = "Slug";

    private ArrayList<String> categories;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    public MenusFragment(ArrayList<String> categories){
        this.categories = categories;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu_restaurant_layout, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        slug = ((RestaurantActivity)this.getActivity()).getRestaurant().get_slug();

        listView = (ListView) getView().findViewById(R.id.list1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, categories);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra(EXTRA_SLUG, slug);
                intent.putExtra(EXTRA_CATEGORY, position);
                String[] catArray = new String [categories.size()];
                categories.toArray(catArray);
                intent.putExtra(EXTRA_CATEGORIES, catArray);
                startActivityForResult(intent,1);
            }
        });

    }
}
