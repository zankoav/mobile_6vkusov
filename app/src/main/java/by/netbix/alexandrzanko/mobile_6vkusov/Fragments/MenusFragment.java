package by.netbix.alexandrzanko.mobile_6vkusov.Fragments;
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

import by.netbix.alexandrzanko.mobile_6vkusov.Activities.Restaurant.ProductActivity;
import by.netbix.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantActivity;
import by.netbix.alexandrzanko.mobile_6vkusov.Adapters.MenuAdapter;
import by.netbix.alexandrzanko.mobile_6vkusov.R;

import java.util.ArrayList;

/**
 * Created by alexandrzanko on 29/11/16.
 */

public class MenusFragment extends Fragment {

    private String slug;

    private final String TAG = this.getClass().getSimpleName();

    public static final String EXTRA_CATEGORY = "Category";
    public static final String EXTRA_SLUG = "Slug";

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    private ArrayList<String> categories;
    private MenuAdapter adapter;
    private ListView listView;

    public MenusFragment(){}

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

        adapter = new MenuAdapter( getActivity(), categories);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra(EXTRA_SLUG, slug);
                intent.putExtra(EXTRA_CATEGORY, position);
                startActivityForResult(intent,1);
            }

        });

    }
}
