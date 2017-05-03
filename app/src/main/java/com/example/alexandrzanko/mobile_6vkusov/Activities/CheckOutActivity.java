package com.example.alexandrzanko.mobile_6vkusov.Activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexandrzanko.mobile_6vkusov.Models.ProductItem;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Singleton;
import com.example.alexandrzanko.mobile_6vkusov.Users.Basket;
import com.example.alexandrzanko.mobile_6vkusov.Users.STATUS;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckOutActivity extends AppCompatActivity implements LoadJson{

    private Toolbar toolbar;
    private EditText name, phone, street, house, block, flat, comment;
    private String nameStr, phoneStr, streetStr, houseStr, blockStr, flatStr, commentStr, messageError;
    private TextView tvPrice, tvDeliveryPrice, tvTotalPrice, tvPoints;
    private final String TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        addToolBarToScreen();
        name = (EditText)findViewById(R.id.order_user_name);
        phone = (EditText)findViewById(R.id.order_user_phone);
        street = (EditText)findViewById(R.id.order_user_street);
        house = (EditText)findViewById(R.id.order_user_house);
        block = (EditText)findViewById(R.id.order_user_block);
        flat = (EditText)findViewById(R.id.order_user_flat);
        comment = (EditText)findViewById(R.id.order_user_comments);

        if(Singleton.currentState().getUser().getStatus() == STATUS.REGISTER){
            JSONObject profile = Singleton.currentState().getUser().getProfile();
            try {
                String firstNameUser = profile.getString("firstName");
                String lastNameUser = profile.getString("lastName");
                name.setText(firstNameUser + " " + lastNameUser);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String phoneStr = profile.getString("phone");
                phone.setText("+375"+phoneStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        initViews();
    }

    private void addToolBarToScreen() {
        toolbar = (Toolbar)findViewById(R.id.toolbar_actionbar);
        toolbar.setTitle(R.string.order_title);
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

    public void buttonPressed(View view) {
        messageError = null;
        String phone = this.phone.getText().toString().trim();
        String street = this.street.getText().toString().trim();
        if (validateData(phone, street)) {
            nameStr = this.name.getText().toString().trim();
            houseStr = this.house.getText().toString().trim();
            blockStr = this.block.getText().toString().trim();
            flatStr = this.flat.getText().toString().trim();
            commentStr = this.comment.getText().toString().trim();

            JSONObject params = new JSONObject();
            String url = this.getResources().getString(R.string.api_send_order);
            try {
                params.put("fio", nameStr);
                params.put("phone", phone);
                params.put("address", street + " " + houseStr);
                params.put("flat", flatStr);
//                params.put("porch", "Подъезд");
//                params.put("floor", "Этаж");
//                params.put("intercom", "Код домофона");
                params.put("delivery_time", 1); //"время доставки, 1 - как можно скорее, 2 - ко времени, "
//                params.put("delivery-time-detail", "детали времени доставки");
                params.put("note", commentStr);
//                params.put("promocode", "промокод");
//                params.put("payment-method", "тип оплаты");
//                params.put("change", "сдача");
//                params.put("delivery-type", "тип доставки, самовывоз, значения 1 или 2");

                if (Singleton.currentState().getUser().getStatus() == STATUS.GENERAL){
                    params.put("variants", Singleton.currentState().getUser().getBasket().getItemsJson());
                }else{
                    url = this.getResources().getString(R.string.api_checkout_cart);
                    params.put("session", Singleton.currentState().getUser().getProfile().getString("session"));
                }
                new JsonHelperLoad(url,params,this, null).execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),messageError, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void initViews() {
        tvPrice = (TextView)findViewById(R.id.checkPriceR);
        tvDeliveryPrice = (TextView)findViewById(R.id.checkDeliveryPriceRub);
        tvTotalPrice= (TextView)findViewById(R.id.checkTotalPriceR);
        tvPoints= (TextView)findViewById(R.id.checkTotalPointsR);
        checkOutUpdateView();
    }

    public void checkOutUpdateView(){
        Basket basket = Singleton.currentState().getUser().getBasket();
        double price = basket.getPrice();
        double deliveryPrice = basket.getDeliveryPrice();
        double totalPrice = price - deliveryPrice;
        int points = basket.getPoints();

        tvPrice.setText(round(price,2) + "");
        tvDeliveryPrice.setText((deliveryPrice > 0 ? round(deliveryPrice,2) : "бесплатно") + "");
        tvTotalPrice.setText(round(totalPrice,2) + "");
        tvPoints.setText(points + "");

    }

    private boolean validateData(String phone, String street){
        Boolean isValidPhone = validPhone(phone);
        Boolean isValidStreet = validStreet(street);
        return isValidPhone && isValidStreet;
    }

    private boolean validStreet(String street) {
        if(street.length() > 0) {
            streetStr = street;
            return true;
        }else{
            if (messageError == null) {
                messageError = "Введите адрес доставки";
            }
            return false;
        }
    }

    private boolean validPhone(String phone) {
        if(phone.length() == 13 && phone.contains("+375")) {
            try{
                Integer.parseInt(phone.substring(1,6));
                Integer.parseInt(phone.substring(6,13));
            }catch (NumberFormatException ex){
                if (messageError == null) {
                    messageError = "Не правильно введен телефон";
                }
                return false;
            }
            phoneStr = phone;
            return true;
        }else{
            if (messageError == null) {
                messageError = "Не правильно введен телефон";
            }
            return false;
        }
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        if (obj != null){
            Log.i(TAG, "loadComplete: " + obj.toString());
            try {
                String status = obj.getString("status");
                if (status.equals("successful")){

                    int orderId = obj.getInt("order");
                    double totalPrice = obj.getDouble("totalPrice");

                    Singleton.currentState().getUser().getBasket().setProductItems(new ArrayList<ProductItem>());
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Заказ принят!");
                    builder.setMessage("Ваш заказ №"+orderId+ ", через несколько минут Вам перезвонит оператор, сумма заказа "+ totalPrice +" рублей");
                    builder.setPositiveButton("Продолжить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            Intent intent = getParentActivityIntent();
                            intent.putExtra(RestaurantsCardActivity.EXTRA_RESTAURANT, Singleton.currentState().getUser().getBasket().getSlugRestaurant());
                            startActivity(intent);
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            } catch (JSONException e) {
                Log.i(TAG, "loadComplete: error" + e);
                e.printStackTrace();
            }
        }else{
            Log.i(TAG, "loadComplete: obj = null");
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }
}
