package com.example.alexandrzanko.mobile_6vkusov.Fragments;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantActivity;
import com.example.alexandrzanko.mobile_6vkusov.R;

/**
 * Created by alexandrzanko on 29/11/16.
 */

public class InfoFragment extends Fragment {

    private String description;

    private TextView textView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        description = ((RestaurantActivity)this.getActivity()).getRestaurant().getDescription();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant_info_layout, container, false);
        textView = (TextView)rootView.findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.setNestedScrollingEnabled(true);
        }
        return rootView;
    }

}
