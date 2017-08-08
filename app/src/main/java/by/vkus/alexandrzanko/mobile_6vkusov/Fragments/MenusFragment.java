package by.vkus.alexandrzanko.mobile_6vkusov.Fragments;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.ProductsActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.MenuAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MenuRestaurant;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

/**
 * Created by alexandrzanko on 29/11/16.
 */

public class MenusFragment extends Fragment {

    private String slug;

    private final String TAG = this.getClass().getSimpleName();

    public static final String EXTRA_SLUG = "Slug";
    public static final String EXTRA_NAME = "Name";
    public static final String EXTRA_RESTAURANT_SLUG = "Restaurant_slug";

    public void setSlug(String slug) {
        this.slug = slug;
    }

    private List<MenuRestaurant> menus;
    private MenuAdapter adapter;
    private ListView listView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: slug"  + slug);
        ApiController.getApi().getMenuByRestaurantSlug(slug).enqueue(new Callback<List<MenuRestaurant>>() {
            @Override
            public void onResponse(Call<List<MenuRestaurant>> call, Response<List<MenuRestaurant>> response) {
                menus = response.body();
                Log.i(TAG, "onResponse: code" + response.code());
                if(menus != null){
                    adapter = new MenuAdapter(MenusFragment.this.getActivity(), menus);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MenusFragment.this.getActivity(), ProductsActivity.class);
                            String categorySlug = menus.get(position).getSlug();
                            String categoryName = menus.get(position).getName();
                            intent.putExtra(EXTRA_SLUG, categorySlug);
                            intent.putExtra(EXTRA_NAME, categoryName);
                            intent.putExtra(EXTRA_RESTAURANT_SLUG, slug);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<MenuRestaurant>> call, Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu_restaurant_layout, container, false);
        listView = (ListView) rootView.findViewById(R.id.list1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
