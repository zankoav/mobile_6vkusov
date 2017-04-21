package com.example.alexandrzanko.mobile_6vkusov.Fragments;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantActivity;
import com.example.alexandrzanko.mobile_6vkusov.Adapters.CommentsAdapter;
import com.example.alexandrzanko.mobile_6vkusov.Models.Comment;
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

public class CommentsFragment extends Fragment implements LoadJson {

    private String slug;
    private CommentsAdapter adapter;
    private ArrayList<Comment> comments;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        slug = ((RestaurantActivity)this.getActivity()).getRestaurant().getSlug();
//        if (comments == null){
//            comments = new ArrayList<>();
//            String url = getResources().getString(R.string.api_restaurant_comments);
//            JSONObject jsonParam = new JSONObject();
//            try {
//                jsonParam.put("slug", slug);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            new JsonHelperLoad(url,jsonParam, this, null).execute();
//        }
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
        listView = (ListView) getView().findViewById(R.id.listComments);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }
        if(comments != null){
            adapter  = new CommentsAdapter(getActivity(), comments);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        try {
            if (obj != null) {
                String baseUrl =getResources().getString(R.string.api_base) + "/" +  obj.getString("img_path");
//                JSONArray array = obj.getJSONArray("comments");
//                if (array != null) {
//                    for (int i = 0; i < array.length(); i++) {
//                        Integer type = array.getJSONObject(i).getInt("type");
//                        String title = array.getJSONObject(i).getString("title");
//                        String text = array.getJSONObject(i).getString("text");
//                        String name = array.getJSONObject(i).getString("user");
//                        String imgLogoStr = baseUrl  + "/" + array.getJSONObject(i).getString("avatarFile");
//                        String time = array.getJSONObject(i).getString("date_time");
//                        comments.add(new Comment(time, type, name, text, title, imgLogoStr, adapter));
//                    }
//                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

}
