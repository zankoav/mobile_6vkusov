package by.vkus.alexandrzanko.mobile_6vkusov.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.OrderAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.OrderItem;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.OrderItemFood;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alexandrzanko on 5/2/17.
 */

public class OrderFragment extends Fragment implements LoadJson {

    private OrderAdapter adapter;
    private ArrayList<OrderItem> orders;
    private ListView listView;

    private final String TAG = this.getClass().getSimpleName();



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.fragment_orders_layout, container, false);
        listView = (ListView)rootView.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.listView);
        return rootView;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String url = getContext().getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.api_orders);
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
        if(obj != null){
            String status = "error";
            try {
                status = obj.getString("status");
                if (status.equals("successful")){
                    this.orders = new ArrayList<>();
                    //String image_path = obj.getString("image_path");
                    JSONArray orders = obj.getJSONArray("orders");
                    for (int i = 0; i < orders.length(); i++){
                        JSONObject orderJson = orders.getJSONObject(i);
                        //String restaurant_name = orderJson.getString("restaurant_name");
                        String restaurant_slug = orderJson.getString("restaurant_slug");
                        //String restaurant_icon = orderJson.getString("restaurant_icon");
                        double total_price = orderJson.getDouble("total_price");
                        int id = orderJson.getInt("id");
                        int created = orderJson.getInt("created");
                        boolean comment_exists = orderJson.getBoolean("comment_exists");
                        JSONArray foods = orderJson.getJSONArray("food");
                        ArrayList<OrderItemFood> arrayFoods = new ArrayList<>();

                        for (int j = 0; j < foods.length(); j++){
                            JSONObject foodJson = foods.getJSONObject(j);
                            String food_name = foodJson.getString("name");
                            int food_count = foodJson.getInt("count");
                            OrderItemFood food = new OrderItemFood(food_count, food_name);
                            arrayFoods.add(food);
                        }

                        OrderItem order = new OrderItem(arrayFoods, restaurant_slug, comment_exists, total_price, id, created);
                        this.orders.add(order);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter = new OrderAdapter(this,orders);
        listView.setAdapter(adapter);
    }


}
