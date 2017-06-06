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

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.CommentsAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Comment;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.InetConnection;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alexandrzanko on 29/11/16.
 */

public class CommentsFragment extends Fragment implements LoadJson {

    private String slug;
    private CommentsAdapter adapter;
    private ArrayList<Comment> comments;

    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createView();
    }


    private void createView(){
        slug = ((RestaurantActivity)this.getActivity()).getRestaurant().get_slug();
        if (comments == null){
            comments = new ArrayList<>();
            String url = getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.api_restaurant_comments);
            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.put("slug", slug);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new JsonHelperLoad(url,jsonParam, this, null).execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.fragment_restaurant_comments_layout, container, false);
        return rootView;

    }

    static ListView listView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        listView = (ListView) getView().findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.listComments);
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
                String baseUrl =getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.api_base) + "/" +  obj.getString("img_path");
                JSONArray array = obj.getJSONArray("comments");
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        Integer type = array.getJSONObject(i).getInt("type");
                        String title = array.getJSONObject(i).getString("title");
                        String text = array.getJSONObject(i).getString("text");
                        String name = array.getJSONObject(i).getString("user");
                        String imgLogoStr = baseUrl  + "/" + array.getJSONObject(i).getString("avatarFile");
                        long time = array.getJSONObject(i).getLong("date_time");
                        Date date = new Date(time * 1000);
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String dateFormatted = formatter.format(date);
                        comments.add(new Comment(dateFormatted, type, name, text, title, imgLogoStr));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }


}
