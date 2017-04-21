package com.example.alexandrzanko.mobile_6vkusov.Activities.Restaurant;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alexandrzanko.mobile_6vkusov.Adapters.ProductsAdapter;
import com.example.alexandrzanko.mobile_6vkusov.Fragments.ProductFragment;
import com.example.alexandrzanko.mobile_6vkusov.Fragments.ViewPageAdapter;
import com.example.alexandrzanko.mobile_6vkusov.Models.Product;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity implements LoadJson {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String slug;

    private int category;
    private RelativeLayout notificationCount;
    private TextView countProducts;
    private String[] categories;
    private ArrayList<Product> products;
    private ArrayList<ProductFragment> fragments;
    private ViewPageAdapter adapter;


    public static final String EXTRA_CATEGORY = "Category";
    public static final String EXTRA_CATEGORIES = "Categories";
    public static final String EXTRA_SLUG = "Slug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        addToolBarToScreen();
        products = new ArrayList<>();
        slug = getIntent().getStringExtra(EXTRA_SLUG);
        category = getIntent().getIntExtra(EXTRA_CATEGORY,0);
        categories = getIntent().getStringArrayExtra(EXTRA_CATEGORIES);
        viewPager = (ViewPager)findViewById(R.id.viewpager_products);
        tabLayout= (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setVisibility(View.GONE);
        fragments = new ArrayList<>();

        JSONObject params = new JSONObject();
        String url = getResources().getString(R.string.api_food);
        try {
            params.put("slug", slug);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new JsonHelperLoad(url, params, this, null).execute();

    }

    public TextView getNotifCount() {
        return countProducts;
    }


    public String getSlug() {
        return slug;
    }



    private void setupViewPager(ViewPager viewPager){
        adapter = new ViewPageAdapter(getSupportFragmentManager());
        for (int i = 0; i < categories.length; i++){
            ProductFragment fragment = new ProductFragment();
            fragment.setCategory(categories[i]);
            ArrayList<Product> prods = new ArrayList<>();
            for(int j = 0; j< products.size(); j++){
                if(products.get(j).getCategory().equals(categories[i])){
                    prods.add(products.get(j));
                }
            }
            fragment.setProducts(prods);
            fragments.add(fragment);
            adapter.addFragment(fragment, categories[i]);
        }
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(category);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.VISIBLE);
    }

    private void addToolBarToScreen() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        toolbar.setTitle(R.string.rest_menu);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
        );
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        invalidateOptionsMenu();
        for(int i = 0; i < fragments.size(); i++){
            ProductsAdapter adapter = fragments.get(i).getAdapter();
            if(adapter != null){
                adapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.shopping_cart);
        MenuItemCompat.setActionView(item, R.layout.menu_basket_icon);
        notificationCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        notificationCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ProductActivity.this, BasketActivity.class);
//                startActivityForResult(intent,1);
            }
        });
        countProducts = (TextView) notificationCount.findViewById(R.id.notif_count);
        updateCountOrdersMenu();
        return super.onCreateOptionsMenu(menu);
    }

    public void updateCountOrdersMenu(){
        int count = 1;//Singleton.currentUser().getStore().getUser().getBasket().getCountProducts();
        if (count>0){
            countProducts.setText(count + "");
            countProducts.setVisibility(View.VISIBLE);
        }else{
            countProducts.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.shopping_cart) {
            // action
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        try {
            if (obj != null){
                if (obj.get("food")!= null ) {
                    String urlImgPath = getResources().getString(R.string.api_base) + obj.getString("img_path") + "/";
                    JSONArray prods = obj.getJSONArray("food");
                    int length = prods.length();
                    for (int i = 0; i < length; i++) {
                        products.add(new Product(prods.getJSONObject(i), urlImgPath));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setupViewPager(viewPager);
    }
}