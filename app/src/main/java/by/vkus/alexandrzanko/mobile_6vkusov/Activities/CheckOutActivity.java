package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SessionStoreV2;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckOutActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private EditText name, phone, street, house, block, flat, comment;
    private String nameStr, phoneStr, streetStr, houseStr, blockStr, flatStr, commentStr, messageError;
    private TextView tvPrice, tvDeliveryPrice, tvTotalPrice, tvPoints;
    private final String TAG = this.getClass().getSimpleName();
    private String totalPrice;
    private Button btnCheckOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.activity_check_out);
        btnCheckOut = (Button) findViewById(R.id.btn_order);
        addToolBarToScreen();
        name = (EditText)findViewById(R.id.order_user_name);
        phone = (EditText)findViewById(R.id.order_user_phone);
        street = (EditText)findViewById(R.id.order_user_street);
        house = (EditText)findViewById(R.id.order_user_house);
        block = (EditText)findViewById(R.id.order_user_block);
        flat = (EditText)findViewById(R.id.order_user_flat);
        comment = (EditText)findViewById(R.id.order_user_comments);
        IUser user = SingletonV2.currentState().getIUser();
        if(user.getStatus().equals(STATUS.REGISTER)) {

            if(user.getFirstName() != null) {
                name.setText(user.getFirstName());
            }

            if (user.getPhoneNumber() != null && user.getPhoneCode() != null) {
                phone.setText("+375"+user.getPhoneCode()+user.getPhoneNumber());
            }

        }
        initViews();
    }

    private void addToolBarToScreen() {
        toolbar = (Toolbar)findViewById(R.id.toolbar_actionbar);
        toolbar.setTitle(by.vkus.alexandrzanko.mobile_6vkusov.R.string.order_title);
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
        btnCheckOut.setEnabled(false);
        String name = this.name.getText().toString().trim();
        String phone = this.phone.getText().toString().trim();
        String street = this.street.getText().toString().trim();

        if (!Validation.minLength(name, 2)){
            Toast toast = Toast.makeText(getApplicationContext(),"Слишком короткое имя", Toast.LENGTH_SHORT);
            toast.show();
            btnCheckOut.setEnabled(true);
        }else if (!Validation.namePhoneNumbers(phone)){
            Toast toast = Toast.makeText(getApplicationContext(),"Номер телефона имеет формат +375XXYYYYYYY", Toast.LENGTH_SHORT);
            toast.show();
            btnCheckOut.setEnabled(true);
        }else if(!Validation.minLength(street, 3)){
            Toast toast = Toast.makeText(getApplicationContext(),"Введите адрес доставки", Toast.LENGTH_SHORT);
            toast.show();
            btnCheckOut.setEnabled(true);
        }else{
            nameStr = this.name.getText().toString().trim();
            houseStr = this.house.getText().toString().trim();
            blockStr = this.block.getText().toString().trim();
            flatStr = this.flat.getText().toString().trim();
            commentStr = this.comment.getText().toString().trim();

            JSONObject params = new JSONObject();
            try {
                params.put("fio", nameStr);
                params.put("phone", phone);


                if (!houseStr.equals("") && !Validation.nameLiteralsAndNumbers(houseStr)){
                    Toast toast = Toast.makeText(getApplicationContext(),"Не корректно введен номер дома", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                params.put("address", street + " " + houseStr);

                if (!flatStr.equals("") && !Validation.nameLiteralsAndNumbers(flatStr)){
                    Toast toast = Toast.makeText(getApplicationContext(),"Не корректно введен номер квартиры", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                if (!flatStr.equals("")) {
                    params.put("flat", flatStr);
                }
                params.put("note", commentStr);
                params.put("delivery_time", 1); //"время доставки, 1 - как можно скорее, 2 - ко времени, "
//                params.put("porch", "Подъезд");
//                params.put("floor", "Этаж");
//                params.put("intercom", "Код домофона");
//                params.put("delivery-time-detail", "детали времени доставки");
//                params.put("promocode", "промокод");
//                params.put("payment-method", "тип оплаты");
//                params.put("change", "сдача");
//                params.put("delivery-type", "тип доставки, самовывоз, значения 1 или 2");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (SingletonV2.currentState().getIUser().getStatus().equals(STATUS.GENERAL)) {
                final SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
                String variants = store.getStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS);
                String currentSlug = store.getStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_RESTAURANT_SLUG);

                ApiController.getApi().checkoutOrderByGeneralUser(currentSlug,variants,params.toString()).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.code() == 200){
                            int orderId = response.body();
                            store.clearKeyStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS);
                            SingletonV2.currentState().getIUser().setCurrentOrderRestaurantSlug(null);
                            showAlert(orderId);
                            btnCheckOut.setEnabled(true);
                        }else{
                            Log.i(TAG, "onResponse: code " + response.code());
                            Toast.makeText(CheckOutActivity.this,"Ошибка соединения ...",Toast.LENGTH_LONG).show();
                            btnCheckOut.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.i(TAG, "onFailure: ");
                        Toast.makeText(CheckOutActivity.this,"Ошибка соединения ...",Toast.LENGTH_LONG).show();
                        btnCheckOut.setEnabled(true);

                    }
                });
            }else{
               String session = SingletonV2.currentState().getIUser().getSession();
                ApiController.getApi().checkoutOrderByRegisterUser(session,params.toString()).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (response.code() == 200) {
                            int orderId = response.body();
                            SingletonV2.currentState().getIUser().setCurrentOrderRestaurantSlug(null);
                            showAlert(orderId);
                            btnCheckOut.setEnabled(true);
                        } else if(response.code() == 307){
                            Toast.makeText(CheckOutActivity.this, "У Вас не достаточно баллов", Toast.LENGTH_LONG).show();
                            btnCheckOut.setEnabled(true);
                        }else{
                            Log.i(TAG, "onResponse: code " + response.code());
                            Toast.makeText(CheckOutActivity.this, "Ошибка соединения ...", Toast.LENGTH_LONG).show();
                            btnCheckOut.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.i(TAG, "onFailure: ");
                        Toast.makeText(CheckOutActivity.this, "Ошибка соединения ...", Toast.LENGTH_LONG).show();
                        btnCheckOut.setEnabled(true);

                    }
                });
            }
        }
    }

    private void showAlert(int orderId) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Внимание!");
        builder.setMessage("Ваш заказ №"+orderId+ ", через несколько минут Вам перезвонит оператор, сумма заказа "+ totalPrice +" рублей");
        builder.setNeutralButton("Закрыть", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Log.i(TAG, "onClick: Закрыть");
                Intent intent = getParentActivityIntent();
                startActivity(intent);
                finish();
            }
        });
        builder.setCancelable(true);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Intent intent = getParentActivityIntent();
                startActivity(intent);
                finish();

            }
        });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void initViews() {
        tvPrice = (TextView)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.checkPriceR);
        tvDeliveryPrice = (TextView)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.checkDeliveryPriceRub);
        tvTotalPrice= (TextView)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.checkTotalPriceR);
        tvPoints= (TextView)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.checkTotalPointsR);
        checkOutUpdateView();
    }

    public void checkOutUpdateView(){
        String price = getIntent().getStringExtra("price");
        String points = getIntent().getStringExtra("points");
        totalPrice = getIntent().getStringExtra("totalPrice");

        tvPrice.setText(price);
        tvDeliveryPrice.setText("бесплатно");
        tvTotalPrice.setText(totalPrice);
        tvPoints.setText(points);

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
