package com.example.alexandrzanko.mobile_6vkusov.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.alexandrzanko.mobile_6vkusov.Adapters.ProductsAdapter;
import com.example.alexandrzanko.mobile_6vkusov.Models.Product;
import com.example.alexandrzanko.mobile_6vkusov.R;
import java.util.ArrayList;

/**
 * Created by alexandrzanko on 01/12/16.
 */

public class ProductFragment extends Fragment {

    private ArrayList<Product> products;
    private String category;
    private ProductsAdapter adapter;
    private ListView listView;

    public ProductsAdapter getAdapter() {
        return adapter;
    }


    public void setProducts(ArrayList<Product> products){
        this.products = products;
    }


    public String getCategory() {
        return category;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_layout, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView) getView().findViewById(R.id.product_list_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }
        if(products != null){
            adapter  = new ProductsAdapter(getActivity(),products);
            listView.setAdapter(adapter);
        }
    }

    public void setCategory(String category) {
        this.category = category;
    }
}