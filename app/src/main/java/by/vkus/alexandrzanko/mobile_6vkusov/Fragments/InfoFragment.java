package by.vkus.alexandrzanko.mobile_6vkusov.Fragments;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.InfoRestaurant;

/**
 * Created by alexandrzanko on 29/11/16.
 */

public class InfoFragment extends Fragment {

    private String description;

    private TextView textView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InfoRestaurant info = ((RestaurantActivity)this.getActivity()).getRestaurant().get_info();
        description = "";
        if (info != null){
            description = info.get_descriptionInfo();
            if (description == null){
                description = "";
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.fragment_restaurant_info_layout, container, false);
        textView = (TextView)rootView.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        if (!description.equals("null")){
            textView.setText(description);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.setNestedScrollingEnabled(true);
        }
        return rootView;
    }

}
