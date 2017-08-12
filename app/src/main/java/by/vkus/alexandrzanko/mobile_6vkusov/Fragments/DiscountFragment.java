package by.vkus.alexandrzanko.mobile_6vkusov.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.DiscountAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Discounts.Discount;

/**
 * Created by alexandrzanko on 8/12/17.
 */

public class DiscountFragment extends Fragment {
    private List<Discount> discounts;
    private ListView listView;

    private DiscountAdapter adapter;

    public void setDiscounts(List<Discount> discounts){
        this.discounts = discounts;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.fragment_discount_layout, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView) getView().findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.discount_list_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }

        if(discounts != null){
            adapter = new DiscountAdapter(this.getActivity(), discounts);
            listView.setAdapter(adapter);
        }
    }
}
