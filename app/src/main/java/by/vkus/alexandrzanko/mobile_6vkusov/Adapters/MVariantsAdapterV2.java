package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant.ProductsActivityV2;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MVariant;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SessionStoreV2;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;


/**
 * Created by alexandrzanko on 1/24/17.
 */

public class MVariantsAdapterV2 extends BaseAdapter {

    public final String TAG = this.getClass().getSimpleName();

    private List<MVariant> listData;
    private LayoutInflater layoutInflater;
    private CounterFab btn;
    private String restaurant_slug;
    private Context context;

    public MVariantsAdapterV2(Context context, List<MVariant> listData, CounterFab btn) {
        this.listData = listData;
        this.restaurant_slug = ((ProductsActivityV2)context).getRestaurant_slug();
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.btn = btn;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public MVariant getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        convertView = layoutInflater.inflate(R.layout.product_variant_item_layout, null);
        holder = new ViewHolder();
        convertView.setBackgroundColor(Color.WHITE);
        holder.productPrice  = (TextView) convertView.findViewById(R.id.product_price);
        holder.productType   = (TextView) convertView.findViewById(R.id.product_width);
        holder.btnPlus       = (Button) convertView.findViewById(R.id.product_btn_plus);

        convertView.setTag(holder);

        final MVariant variant = listData.get(position);
        String weight = variant.getWidth();
        String size = variant.getSize();
        if(weight != null){
            holder.productType.setText("Вес " + weight);
        }else if (size != null){
            holder.productType.setText(size);
        }
        String price = Validation.twoNumbersAfterAfterPoint(variant.getPrice());
        holder.productPrice.setText(price + " р.");

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrderVariant(variant);
            }
        });
        return convertView;
    }

    private void addOrderVariant(MVariant variant){
        String currentOrderRestaurantSlug = SingletonV2.currentState().getIUser().getCurrentOrderRestaurantSlug();
        if (currentOrderRestaurantSlug == null){
            createNewOrder(variant);
        }else if (restaurant_slug.equals(currentOrderRestaurantSlug)){
            addOrderItem(variant);
        }else{
            showAlertNewOrder(variant);
        }
    }

    private void createNewOrder(MVariant variant) {
        final IUser user = SingletonV2.currentState().getIUser();
        btn.increase();
        if(user.getStatus().equals(STATUS.REGISTER)){
            ApiController.getApi().createNewOrderByRegisterUser(user.getSession(), variant.getId()).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.code() == 301){
                        user.setCurrentOrderRestaurantSlug(restaurant_slug);
                    }else{
                        btn.decrease();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.i(TAG, "onFailure: ");
                    btn.decrease();
                }
            });
        }else{
            SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
            String variants = store.getStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS);
            if (variants != null){
                store.clearKeyStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS);
            }
            try {
                JSONArray array = new JSONArray();
                JSONObject variantJson = new JSONObject();
                variantJson.put("id",variant.getId());
                variantJson.put("count",1);
                array.put(variantJson);
                store.setStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS,array.toString());
                user.setCurrentOrderRestaurantSlug(restaurant_slug);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addOrderItem(MVariant variant) {
        final IUser user = SingletonV2.currentState().getIUser();
        btn.increase();
        if(user.getStatus().equals(STATUS.REGISTER)){
            ApiController.getApi().addItemOrderByRegisterUser(user.getSession(), variant.getId()).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.code() == 303){
                        btn.setCount(1);
                        user.setCurrentOrderRestaurantSlug(restaurant_slug);
                        Log.i(TAG, "onResponse: new create 303");
                    }
                    if (response.code() == 302){
                        btn.decrease();
                        Log.i(TAG, "onResponse: error 302");
                    }

                    if (response.code() == 301){
                        Log.i(TAG, "onResponse: add 301");
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.i(TAG, "onFailure: ");
                    btn.decrease();
                }
            });
        }else{

            SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
            String variants = store.getStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS);
            if (variants != null){
                try {

                    JSONArray array = new JSONArray(variants);
                    boolean isExists = false;
                    for(int i =0 ; i < array.length(); i++){
                        JSONObject variantJson  = array.getJSONObject(i);
                        if(variantJson.getInt("id") == variant.getId()){
                            int count = variantJson.getInt("count") + 1;
                            variantJson.put("count", count);
                            isExists = true;
                            break;
                        }
                    }

                    if (!isExists){
                        JSONObject variantJson = new JSONObject();
                        variantJson.put("id",variant.getId());
                        variantJson.put("count",1);
                        array.put(variantJson);
                    }
                    store.setStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS,array.toString());
                    user.setCurrentOrderRestaurantSlug(restaurant_slug);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                btn.decrease();
            }
        }
    }

    public void showAlertNewOrder(final MVariant variant) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Внимание!");
        builder.setMessage("В Вашей корзине присутствуют товары из другого ресторана. Очистить корзину ?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                final IUser user = SingletonV2.currentState().getIUser();
                if(user.getStatus().equals(STATUS.REGISTER)){
                    ApiController.getApi().createNewOrderByRegisterUser(user.getSession(), variant.getId()).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 301){
                                btn.setCount(1);
                                user.setCurrentOrderRestaurantSlug(restaurant_slug);
                            }else{
                                btn.decrease();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Log.i(TAG, "onFailure: ");
                            btn.decrease();
                        }
                    });
                }else{
                    SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
                    String variants = store.getStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS);
                    if (variants != null){
                        store.clearKeyStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS);
                    }
                    try {
                        JSONArray array = new JSONArray();
                        JSONObject variantJson = new JSONObject();
                        variantJson.put("id",variant.getId());
                        variantJson.put("count",1);
                        array.put(variantJson);
                        store.setStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS,array.toString());
                        user.setCurrentOrderRestaurantSlug(restaurant_slug);
                        btn.setCount(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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

    static class ViewHolder {
        TextView  productType;
        TextView  productPrice;
        Button    btnPlus;
    }
}
