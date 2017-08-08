package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.MVariantsAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.ProductAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MProduct;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Settings.DiscreteScrollViewOptions;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener {

    private InfiniteScrollAdapter infiniteAdapter;
    public final String TAG = this.getClass().getSimpleName();

    private DiscreteScrollView itemPicker;
    private List<MProduct> products;
    private TextView tvName, tvDescription, tvPoints;
    private ListView variants;
    private CounterFab fab;
    private Button pointBtn;


    public static final String EXTRA_SLUG = "Slug";
    public static final String EXTRA_NAME = "Name";
    public static final String EXTRA_RESTAURANT_SLUG = "Restaurant_slug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Bundle bundle = getIntent().getExtras();
        String menu_slug = bundle.getString(EXTRA_SLUG);
        String name = bundle.getString(EXTRA_NAME);
        String restaurant_slug = bundle.getString(EXTRA_RESTAURANT_SLUG);
        TextView title = (TextView) findViewById(R.id.category_title);
        title.setText(name);
        pointBtn = (Button)findViewById(R.id.food_by_points);
        tvPoints = (TextView) findViewById(R.id.item_points);

        variants = (ListView)findViewById(R.id.variants);
        if (menu_slug.equals("free_food")){
            pointBtn.setVisibility(View.VISIBLE);
            tvPoints.setVisibility(View.VISIBLE);
            variants.setVisibility(View.GONE);
        }else{
            pointBtn.setVisibility(View.GONE);
            tvPoints.setVisibility(View.GONE);
            variants.setVisibility(View.VISIBLE);
        }
        fab = (CounterFab) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Singleton.currentState().getIUser().getBasket().getCountItems();
                if (count>0){
                    Intent intent = new Intent(ProductsActivity.this, BasketActivity.class);
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
                    itemPicker.addOnItemChangedListener(ProductsActivity.this);
                    infiniteAdapter = InfiniteScrollAdapter.wrap(new ProductAdapter(products));
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

    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        int positionInDataSet = infiniteAdapter.getRealPosition(adapterPosition);
        MProduct product = products.get(positionInDataSet);
        tvName.setText(product.getName());
        tvDescription.setText(product.getDescription());
        if(product.getPoints() != null){
            tvPoints.setText( product.getPoints() + " баллов");
        }
        MVariantsAdapter adapter = new MVariantsAdapter(this, product.getVariants(),fab);
        variants.setAdapter(adapter);
    }

    public void backButtonClick(View view) {
        finish();
    }

    public void foodPointsPressed(View view) {
        fab.increase();
    }
}
