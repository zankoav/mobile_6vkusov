package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.DiscountFragment;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.ViewPageAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Discounts.MOurDiscountCategory;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OurPintsV2 extends BaseMenuActivity {

    private TabLayout tabLayout;
    public ViewPager viewPager;
    private List<DiscountFragment> fragments;
    private List<MOurDiscountCategory> categories;
    public ViewPageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_pints);
        this.initViews(this.getString(R.string.our_points_title));
        viewPager = (ViewPager)findViewById(R.id.viewpager_points);
        tabLayout= (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setVisibility(View.GONE);
        ApiController.getApi().getOurPoints().enqueue(new Callback<List<MOurDiscountCategory>>() {
            @Override
            public void onResponse(Call<List<MOurDiscountCategory>> call, Response<List<MOurDiscountCategory>> response) {
                if(response.code() == 200){
                    categories = response.body();
                    Log.i(TAG, "onResponse: count = " + categories.size());
                    setupViewPager(viewPager);
                }else{
                    Log.i(TAG, "onResponse: code = " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MOurDiscountCategory>> call, Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });

    }

    private void setupViewPager(ViewPager viewPager){
        fragments = new ArrayList<>();
        adapter = new ViewPageAdapter(getSupportFragmentManager());
        for (int i = 0; i < categories.size(); i++){
            DiscountFragment fragment = new DiscountFragment();
            fragment.setDiscounts(categories.get(i).getDiscounts());
            fragments.add(fragment);
            adapter.addFragment(fragment, categories.get(i).getName());
        }
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.VISIBLE);
    }

}
