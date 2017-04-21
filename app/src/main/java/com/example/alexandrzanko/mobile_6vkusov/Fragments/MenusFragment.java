package com.example.alexandrzanko.mobile_6vkusov.Fragments;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.alexandrzanko.mobile_6vkusov.Activities.Restaurant.ProductActivity;
import com.example.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantActivity;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by alexandrzanko on 29/11/16.
 */

public class MenusFragment extends Fragment implements LoadJson {

    private String slug;
    public static final String EXTRA_CATEGORIES = "Categories";
    public static final String EXTRA_CATEGORY = "Category";
    public static final String EXTRA_SLUG = "Slug";

    private ArrayList<String> categories;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        slug = ((RestaurantActivity)this.getActivity()).getRestaurant().getSlug();
//        if (categories == null){
//            categories = new ArrayList<>();
//            JSONObject params = new JSONObject();
//            try {
//                params.put("slug", slug);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            String url = getResources().getString(R.string.api_categories_restaurant);
//            new JsonHelperLoad(url, params, this, null).execute();
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu_restaurant_layout, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.list1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }

//        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, categories);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String slug = ((RestaurantActivity)getActivity()).getRestaurant().getSlug();
//                Intent intent = new Intent(getActivity(), ProductActivity.class);
//                intent.putExtra(EXTRA_SLUG, slug);
//                intent.putExtra(EXTRA_CATEGORY, position);
//                String[] catArray = new String [categories.size()];
//                categories.toArray(catArray);
//                intent.putExtra(EXTRA_CATEGORIES, catArray);
//                startActivityForResult(intent,1);
//            }
//        });
    }

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        try {
            if (obj != null){
                JSONArray jsonArray = obj.getJSONArray("categories");
                for(int i=0; i < jsonArray.length(); i++){
                    categories.add(jsonArray.getString(i));
                }
            }else{
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
}
