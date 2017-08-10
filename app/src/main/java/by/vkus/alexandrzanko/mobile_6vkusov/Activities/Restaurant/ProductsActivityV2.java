package by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.Orientation;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.BasketActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.MVariantsAdapterV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.ProductAdapterV2;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MProduct;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SessionStoreV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Settings.DiscreteScrollViewOptions;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivityV2 extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener {

    private InfiniteScrollAdapter infiniteAdapter;
    public final String TAG = this.getClass().getSimpleName();

    private DiscreteScrollView itemPicker;
    private List<MProduct> products;
    private TextView tvName, tvDescription, tvPoints, tvMessage;
    private ListView variants;
    private CounterFab fab;
    private Button pointBtn;
    private MProduct product;
    private String restaurant_slug;


    public static final String EXTRA_SLUG = "Slug";
    public static final String EXTRA_NAME = "Name";
    public static final String EXTRA_RESTAURANT_SLUG = "Restaurant_slug";

    @Override
    public void onResume() {
        super.onResume();
        if(SingletonV2.currentState().getIUser().getStatus().equals(STATUS.REGISTER)){
            ApiController.getApi().getCountOrderItemsByUser(SingletonV2.currentState().getIUser().getSession()).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.code() == 200){
                        fab.setCount(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.i(TAG, "onFailure: ");
                }
            });

        }else{
            SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
            String variants = store.getStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS);
            if (variants != null){
                try {
                    int count = 0;
                    JSONArray array = new JSONArray(variants);
                    for(int i =0 ; i < array.length(); i++){
                        JSONObject variant  = array.getJSONObject(i);
                        count += variant.getInt("count");
                    }
                    fab.setCount(count);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Bundle bundle = getIntent().getExtras();
        String menu_slug = bundle.getString(EXTRA_SLUG);
        String name = bundle.getString(EXTRA_NAME);
        restaurant_slug = bundle.getString(EXTRA_RESTAURANT_SLUG);
        addToolBarToScreen(name);
        pointBtn = (Button)findViewById(R.id.food_by_points);
        tvPoints = (TextView) findViewById(R.id.item_points);
        tvMessage =  (TextView) findViewById(R.id.food_by_message);
        variants = (ListView)findViewById(R.id.variants);
        if (menu_slug.equals("free_food")){
            pointBtn.setVisibility(View.VISIBLE);
            tvPoints.setVisibility(View.VISIBLE);
            variants.setVisibility(View.GONE);
            tvMessage.setVisibility(View.VISIBLE);
        }else{
            pointBtn.setVisibility(View.GONE);
            tvPoints.setVisibility(View.GONE);
            tvMessage.setVisibility(View.GONE);
            variants.setVisibility(View.VISIBLE);
        }
        fab = (CounterFab) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fab.getCount()>0){
                    Intent intent = new Intent(ProductsActivityV2.this, BasketActivity.class);
                    startActivity(intent);
                }else{
                    Snackbar.make(view, "Ваша корзина пуста", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });

        tvName = (TextView) findViewById(R.id.item_name);
        tvDescription = (TextView) findViewById(R.id.item_description);
        ApiController.getApi().getProductsByMenuSlug(menu_slug,restaurant_slug).enqueue(new Callback<List<MProduct>>() {
            @Override
            public void onResponse(Call<List<MProduct>> call, Response<List<MProduct>> response) {
                products = response.body();
                if (products != null){
                    itemPicker = (DiscreteScrollView) findViewById(R.id.item_picker);
                    itemPicker.setOrientation(Orientation.HORIZONTAL);
                    itemPicker.addOnItemChangedListener(ProductsActivityV2.this);
                    infiniteAdapter = InfiniteScrollAdapter.wrap(new ProductAdapterV2(products));
                    itemPicker.setAdapter(infiniteAdapter);
                    itemPicker.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
                    itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                            .setMinScale(0.8f)
                            .build());
                }
            }

            @Override
            public void onFailure(Call<List<MProduct>> call, Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });

        if (SingletonV2.currentState().getIUser().getStatus().equals(STATUS.REGISTER)){
            pointBtn.setText("Добавить");
            pointBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorButtons));
            tvMessage.setVisibility(View.GONE);
        }else {
            pointBtn.setVisibility(View.GONE);
        }
    }

    public String getRestaurant_slug(){
        return restaurant_slug;
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        int positionInDataSet = infiniteAdapter.getRealPosition(adapterPosition);
        product = products.get(positionInDataSet);
        tvName.setText(product.getName());
        tvDescription.setText(product.getDescription());
        if(product.getPoints() != null){
            tvPoints.setText( product.getPoints() + " баллов");
        }
        MVariantsAdapterV2 adapter = new MVariantsAdapterV2(this, product.getVariants(),fab);
        variants.setAdapter(adapter);
    }

    public void backButtonClick(View view) {
        finish();
    }

    public void foodPointsPressed(View view) {
        IUser user = SingletonV2.currentState().getIUser();
        if (user.getStatus().equals(STATUS.REGISTER)){
            int variantId = product.getVariants().get(0).getId();
            Log.i(TAG, "foodPointsPressed: size = " + product.getVariants().size());
            ApiController.getApi().addFreeFoodOrderByRegisterUser(user.getSession(),variantId).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.code() == 301){
                        fab.increase();
                    }else if(response.code() == 302){
                        showAlertNewOrder();
                    }else if(response.code() == 303){
                        showAlertByExistingFreeFoodOrder();
                    }else if(response.code() == 304){
                        showAlertByHaveNoPoints();
                    }else {
                        Log.i(TAG, "onResponse: code = " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.i(TAG, "onFailure: ");
                }
            });
        }

    }

    private void showAlertByHaveNoPoints() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Внимание!");
        builder.setMessage("У вас не достаточно баллов");
        builder.setNeutralButton("ОК", new DialogInterface.OnClickListener() {
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

    private void showAlertByExistingFreeFoodOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Внимание!");
        builder.setMessage("В Вашей корзине уже есть один продукт за баллы");
        builder.setNeutralButton("ОК", new DialogInterface.OnClickListener() {
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

    private void showAlertNewOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Внимание!");
        builder.setMessage("В Вашей корзине присутствуют товары из другого ресторана. Создать новую корзину ?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                final IUser user = SingletonV2.currentState().getIUser();
                int variantId = product.getVariants().get(0).getId();
                ApiController.getApi().createNewOrderFreeFoodByRegisterUser(user.getSession(), variantId).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.code() == 301){
                            fab.setCount(1);
                            user.setCurrentOrderRestaurantSlug(restaurant_slug);
                        }else{
                            Log.i(TAG, "onResponse: code = " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.i(TAG, "onFailure: ");
                    }
                });

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



    private void addToolBarToScreen(String title) {
        Toolbar toolbar = (Toolbar)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
