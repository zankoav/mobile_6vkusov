package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.BasketAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MOrderItem;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MRestaurantDeliveryInfo;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SessionStoreV2;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.Basket;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasketActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private ListView lv;
    public final String TAG = this.getClass().getSimpleName();


    public BasketAdapter getAdapter() {
        return adapter;
    }
    private BasketAdapter adapter;

    private IUser user;
    private MRestaurantDeliveryInfo deliveryInfo;
    private List<MOrderItem> orderItems;

    private Button btnSender;
    private RelativeLayout checkOut;
    private TextView tvPrice, tvDeliveryPrice, tvTotalPrice, tvPoints ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.activity_basket);
        addToolBarToScreen();
        user = SingletonV2.currentState().getIUser();
        loadDeliveryInfo();
        lv = (ListView)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.listView_basket);
        initViews();
    }

    private void loadDeliveryInfo(){
        String session = null;
        String restaurant_slug = null;
        if (user.getStatus().equals(STATUS.REGISTER)){
            session = user.getSession();
        }else{
            restaurant_slug = user.getCurrentOrderRestaurantSlug();
        }
        ApiController.getApi().getInfoDeliveryRestaurant(session, restaurant_slug).enqueue(new Callback<MRestaurantDeliveryInfo>() {
            @Override
            public void onResponse(Call<MRestaurantDeliveryInfo> call, Response<MRestaurantDeliveryInfo> response) {
                if(response.code() == 200){
                    deliveryInfo = response.body();
                    if(deliveryInfo != null){
                        if(user.getStatus().equals(STATUS.REGISTER)){
                            loadBasketByRegisterUser();
                        }else {
                            loadBasketByGeneralUser();
                        }
                    }
                }else{
                    Log.i(TAG, "onResponse: code = " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MRestaurantDeliveryInfo> call, Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });
    }

    private void loadBasketByRegisterUser() {
        ApiController.getApi().getOrderItemsByRegisterUser(user.getSession()).enqueue(new Callback<List<MOrderItem>>() {
            @Override
            public void onResponse(Call<List<MOrderItem>> call, Response<List<MOrderItem>> response) {
                if(response.code() == 200){
                    orderItems = response.body();
                    if(orderItems != null){
                        adapter = new BasketAdapter(BasketActivity.this, orderItems);
                        lv.setAdapter(adapter);
                        checkOut.setVisibility(View.VISIBLE);
                        checkOutUpdateView();
                    }
                }else{
                    Log.i(TAG, "onResponse: code = " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MOrderItem>> call, Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });
    }

    private void loadBasketByGeneralUser() {
        SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
        String variants = store.getStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS);
        String restaurant_slug = user.getCurrentOrderRestaurantSlug();
        ApiController.getApi().getOrderItemsForGeneralByRestaurantsSlug(restaurant_slug, variants).enqueue(new Callback<List<MOrderItem>>() {
            @Override
            public void onResponse(Call<List<MOrderItem>> call, Response<List<MOrderItem>> response) {
                if(response.code() == 200){
                    orderItems = response.body();
                    if(orderItems != null){
                        adapter = new BasketAdapter(BasketActivity.this, orderItems);
                        lv.setAdapter(adapter);
                        checkOut.setVisibility(View.VISIBLE);
                        checkOutUpdateView();
                    }
                }else{
                    Log.i(TAG, "onResponse: code = " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MOrderItem>> call, Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });
    }

    private void initViews() {
        btnSender = (Button)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.button);
        tvPrice = (TextView)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.checkPriceR);
        tvDeliveryPrice = (TextView)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.checkDeliveryPriceRub);
        tvTotalPrice= (TextView)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.checkTotalPriceR);
        tvPoints= (TextView)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.checkTotalPointsR);
        checkOut = (RelativeLayout)findViewById(R.id.checkOut);
    }

    private void addToolBarToScreen() {
        toolbar = (Toolbar)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.toolbar_actionbar);
        toolbar.setTitle(by.vkus.alexandrzanko.mobile_6vkusov.R.string.basket);
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

    public void orderButtonPressed(View view) {
        Intent intent = new Intent(BasketActivity.this, CheckOutActivity.class);
        double price = getPrice();
        double totalPrice = getTotalPrice(price);
        String points = String.valueOf(getPoints(price));

        intent.putExtra("price", String.valueOf(Validation.twoNumbersAfterAfterPoint(price)));
        intent.putExtra("totalPrice", String.valueOf(Validation.twoNumbersAfterAfterPoint(totalPrice)));
        intent.putExtra("points", points);

        startActivity(intent);
    }

    public void checkOutUpdateView(){
        double price = getPrice();
        int points = getPoints(price);
        double totalPrice = getTotalPrice(price);

        tvPrice.setText(Validation.twoNumbersAfterAfterPoint(price));
        tvPoints.setText(points + "");
        tvDeliveryPrice.setText((deliveryInfo.getDeliveryPrice() > 0 ? Validation.twoNumbersAfterAfterPoint(deliveryInfo.getDeliveryPrice()) : "бесплатно") + "");
        tvTotalPrice.setText(Validation.twoNumbersAfterAfterPoint(totalPrice) + "");

        if (deliveryInfo.getMinimalPrice() > totalPrice){
            btnSender.setEnabled(false);
            btnSender.setBackgroundResource(R.drawable.shape_corner);
            btnSender.setText("Мин. стоимость заказа " + Validation.twoNumbersAfterAfterPoint(deliveryInfo.getMinimalPrice()) + " руб.");
        }else{
            btnSender.setEnabled(true);
            btnSender.setBackgroundResource(R.drawable.shape_corner_green);
            btnSender.setText("Оформить заказ");
        }
    }

    private int getPoints(double price){
        return (int)(price*5);
    }

    private double getTotalPrice(double price){
        return price - deliveryInfo.getDeliveryPrice();
    }

    private double getPrice(){
        double price = 0;
        for (int i = 0; i < orderItems.size(); i++){
            if(orderItems.get(i).isSolidByPoints()){
                continue;
            }
            price += orderItems.get(i).getPrice()* orderItems.get(i).getCount();
        }
        return price;
    }
}
