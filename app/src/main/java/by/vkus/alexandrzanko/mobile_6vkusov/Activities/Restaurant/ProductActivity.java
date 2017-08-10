package by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.BasketActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.ProductsAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.ProductFragment;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.ViewPageAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Product;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.ProductItem;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.BasketViewInterface;

import java.util.ArrayList;
import java.util.Collections;

public class ProductActivity extends AppCompatActivity implements BasketViewInterface {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    public ViewPager viewPager;

    private String slug;

    private int category;
    private RelativeLayout notificationCount;
    private TextView countProducts;
    private ArrayList<String> categories;
    private ArrayList<Product> products;
    private ArrayList<ProductFragment> fragments;
    public ViewPageAdapter adapter;


    public static final String EXTRA_CATEGORY = "Category";
    public static final String EXTRA_SLUG = "Slug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        addToolBarToScreen();
        slug = getIntent().getStringExtra(EXTRA_SLUG);
        category = getIntent().getIntExtra(EXTRA_CATEGORY,0);
        viewPager = (ViewPager)findViewById(R.id.viewpager_products);
        tabLayout= (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setVisibility(View.GONE);
        fragments = new ArrayList<>();

//        products = SingletonV2.currentState().getStore().currentProducts;
        categories = initCategories(products);

        setupViewPager(viewPager);
        SingletonV2.currentState().getUser().getBasket().setDelegateContext(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        SingletonV2.currentState().getUser().getBasket().initBasketFromRegisterUser();
    }

    public String getSlug() {
        return slug;
    }

    private void setupViewPager(ViewPager viewPager){
        adapter = new ViewPageAdapter(getSupportFragmentManager());
        for (int i = 0; i < categories.size(); i++){
            ProductFragment fragment = new ProductFragment();
            fragment.setCategory(categories.get(i));
            ArrayList<Product> prods = new ArrayList<>();
            for(int j = 0; j< products.size(); j++){

                if (products.get(j).get_points() > 0 && categories.get(i).equals("Еда за баллы")){
                    prods.add(products.get(j));
                    continue;
                }

                if(products.get(j).get_category().get("name").equals(categories.get(i)) && products.get(j).get_points() == 0){
                    prods.add(products.get(j));
                }
            }
            fragment.setProducts(prods);
            fragments.add(fragment);
            adapter.addFragment(fragment, categories.get(i));
        }
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(category);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.VISIBLE);
    }

    private void addToolBarToScreen() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        toolbar.setTitle(by.vkus.alexandrzanko.mobile_6vkusov.R.string.rest_menu);
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
                if (SingletonV2.currentState().getUser().getBasket().getTotalCount() == 0){
                    return;
                }
                Intent intent = new Intent(ProductActivity.this, BasketActivity.class);
                startActivityForResult(intent,1);
            }
        });
        countProducts = (TextView) notificationCount.findViewById(R.id.notif_count);
        updateCountOrdersMenu();
        return super.onCreateOptionsMenu(menu);
    }

    public void updateCountOrdersMenu(){
        int count = SingletonV2.currentState().getUser().getBasket().getTotalCount();
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

    private ArrayList<String> initCategories(ArrayList<Product> prods){
        ArrayList<String> categories = new ArrayList<>();
        String freeFood = null;
        for(int i = 0; i < prods.size(); i++){
            String name = prods.get(i).get_category().get("name");
            int points = prods.get(i).get_points();
            if (points > 0){
                freeFood = "Еда за баллы";
            }
            if (!categories.contains(name)){
                categories.add(name);
            }
        }
        if (freeFood != null){
            categories.add(0,freeFood);
        }
        Collections.sort(categories);
        return categories;
    }

    @Override
    public void updateBasket(int count) {
        updateCountOrdersMenu();
    }

    @Override
    public void showAlert(final ProductItem product, final String slug) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Внимание!");
                            builder.setMessage("В Вашей корзине присутствуют товары из другого ресторана. Очистить корзину ?");
                            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int arg1) {
                                    SingletonV2.currentState().getUser().getBasket().setProductItems(new ArrayList<ProductItem>());
                                    SingletonV2.currentState().getUser().getBasket().setFreeFoodExist(false);
                                    SingletonV2.currentState().getUser().getBasket().addProductItem(product, slug);
                                }
                            });
                            builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int arg1) {
                                }
                            });
                            builder.setCancelable(true);
                            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                public void onCancel(DialogInterface dialog) {
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();
    }

    @Override
    public void showAlertNewOrder(final Product product, final String slug) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Внимание!");
        builder.setMessage("В Вашей корзине присутствуют товары из другого ресторана. Очистить корзину ?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                SingletonV2.currentState().getUser().getBasket().setProductItems(new ArrayList<ProductItem>());
                SingletonV2.currentState().getUser().getBasket().setFreeFoodExist(false);
                SingletonV2.currentState().getUser().getBasket().resetRegisterBasket(product, slug);
            }
        });
        builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
        builder.setCancelable(true);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}