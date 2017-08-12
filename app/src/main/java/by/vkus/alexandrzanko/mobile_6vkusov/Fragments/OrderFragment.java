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
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Order.MOrder;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.OrderItem;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.OrderItemFood;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
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

public class OrderFragment extends Fragment {

    private OrderAdapter adapter;
    private List<MOrder> orders;
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
        String session = SingletonV2.currentState().getIUser().getSession();
        ApiController.getApi().getOrdersUser(session).enqueue(new Callback<List<MOrder>>() {
            @Override
            public void onResponse(Call<List<MOrder>> call, Response<List<MOrder>> response) {
                if(response.code() == 200){
                    orders = response.body();
                    adapter = new OrderAdapter(OrderFragment.this,orders);
                    listView.setAdapter(adapter);
                }else{
                    Log.i(TAG, "onResponse: code = " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MOrder>> call, Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });

    }

}
