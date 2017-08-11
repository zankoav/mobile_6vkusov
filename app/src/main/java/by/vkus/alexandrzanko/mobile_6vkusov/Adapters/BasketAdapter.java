package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.BasketActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MOrderItem;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.ProductItem;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Variant;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SessionStoreV2;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


/**
 * Created by alexandrzanko on 07/12/16.
 */

public class BasketAdapter extends BaseAdapter {
    private List<MOrderItem> orderItems;
    private LayoutInflater layoutInflater;
    private Context context;
    private IUser user;

    public final String TAG = this.getClass().getSimpleName();


    public BasketAdapter(Context context, List<MOrderItem>  listData) {
        this.orderItems = listData;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.user = SingletonV2.currentState().getIUser();
    }

    @Override
    public int getCount() {
        return orderItems.size();
    }

    @Override
    public Object getItem(int position) {
        return orderItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        convertView = layoutInflater.inflate(R.layout.product_item_basket_layout, null);
        holder = new ViewHolder();
        holder.productName     = (TextView) convertView.findViewById(R.id.product_name);
        holder.productImg      = (ImageView) convertView.findViewById(R.id.product_icon);
        holder.productWidth    = (TextView) convertView.findViewById(R.id.product_width);
        holder.productPrice    = (TextView) convertView.findViewById(R.id.product_price);
        holder.countProducts   = (TextView) convertView.findViewById(R.id.product_count);
        holder.productDescription   = (TextView) convertView.findViewById(R.id.product_description);

        holder.btnPlus         = (Button) convertView.findViewById(R.id.product_btn_plus);
        holder.btnMinus        = (Button) convertView.findViewById(R.id.product_btn_minus);
        holder.btnRemove       = (Button) convertView.findViewById(R.id.product_btn_remove);

        convertView.setTag(holder);

        final MOrderItem productItem = orderItems.get(position);

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getStatus().equals(STATUS.REGISTER)){
                    int count = productItem.getCount() + 1;
                    productItem.setCount(count);
                    holder.countProducts.setText(productItem.getCount() + "");
                    ApiController.getApi().addItemOrderByRegisterUser(user.getSession(), productItem.getIdVariant()).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                            if (response.code() == 302){
                                int count = productItem.getCount() - 1;
                                productItem.setCount(count);
                                holder.countProducts.setText(productItem.getCount() + "");
                                ((BasketActivity)context).checkOutUpdateView();
                                Log.i(TAG, "onResponse: error 302");
                            }

                            if (response.code() == 301){
                                Log.i(TAG, "onResponse: add 301");
                                ((BasketActivity)context).checkOutUpdateView();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            int count = productItem.getCount() - 1;
                            productItem.setCount(count);
                            holder.countProducts.setText(productItem.getCount() + "");
                            ((BasketActivity)context).checkOutUpdateView();
                            Log.i(TAG, "onResponse: error 302");
                        }
                    });
                }else{
                    SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
                    String variants = store.getStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS);
                    if (variants != null){
                        try {
                            JSONArray array = new JSONArray(variants);
                            for(int i =0 ; i < array.length(); i++){
                                JSONObject variantJson  = array.getJSONObject(i);
                                if(variantJson.getInt("id") == productItem.getIdVariant()){
                                    int count = variantJson.getInt("count") + 1;
                                    productItem.setCount(count);
                                    holder.countProducts.setText(productItem.getCount() + "");
                                    String price = Validation.twoNumbersAfterAfterPoint(productItem.getPrice()*productItem.getCount()) + " p.";
                                    holder.productPrice.setText(price);
                                    variantJson.put("count", productItem.getCount());
                                    store.setStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS,array.toString());
                                    ((BasketActivity)context).checkOutUpdateView();
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getStatus().equals(STATUS.REGISTER)){
                    int count = productItem.getCount() - 1;
                    if(count >= 1){
                        productItem.setCount(count);
                        holder.countProducts.setText(productItem.getCount() + "");
                        ApiController.getApi().minusItemOrderByRegisterUser(user.getSession(), productItem.getIdVariant()).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                                if (response.code() == 302){
                                    int count = productItem.getCount() + 1;
                                    productItem.setCount(count);
                                    holder.countProducts.setText(productItem.getCount() + "");
                                    ((BasketActivity)context).checkOutUpdateView();
                                    Log.i(TAG, "onResponse: error 302");
                                }

                                if (response.code() == 301){
                                    Log.i(TAG, "onResponse: minus 301");
                                    ((BasketActivity)context).checkOutUpdateView();
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                int count = productItem.getCount() + 1;
                                productItem.setCount(count);
                                holder.countProducts.setText(productItem.getCount() + "");
                                ((BasketActivity)context).checkOutUpdateView();
                                Log.i(TAG, "onResponse: error 302");
                            }
                        });
                    }
                }else{
                    SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
                    String variants = store.getStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS);
                    if (variants != null){
                        try {
                            JSONArray array = new JSONArray(variants);
                            for(int i =0 ; i < array.length(); i++){
                                JSONObject variantJson  = array.getJSONObject(i);
                                if(variantJson.getInt("id") == productItem.getIdVariant()){
                                    int count = variantJson.getInt("count") - 1;
                                    if (count >= 1){
                                        productItem.setCount(count);

                                        holder.countProducts.setText(productItem.getCount() + "");
                                        String price = Validation.twoNumbersAfterAfterPoint(productItem.getPrice()*productItem.getCount()) + " p.";
                                        holder.productPrice.setText(price);

                                        variantJson.put("count", productItem.getCount());
                                        store.setStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS,array.toString());
                                        ((BasketActivity)context).checkOutUpdateView();
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getStatus().equals(STATUS.REGISTER)){
                    ApiController.getApi().removeItemOrderByRegisterUser(user.getSession(), productItem.getIdOrderItem()).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                            if (response.code() == 301){
                                orderItems.remove(productItem);
                                notifyDataSetChanged();
                                if(orderItems.size() == 0){
                                    user.setCurrentOrderRestaurantSlug(null);
                                }
                                ((BasketActivity)context).checkOutUpdateView();
                            }else{
                                Log.i(TAG, "onResponse: code: " + response.code());

                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Log.i(TAG, "onResponse: error 302");
                        }
                    });
                }else{
                    SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
                    String variants = store.getStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS);
                    if (variants != null){
                        try {
                            JSONArray newArray = new JSONArray();
                            JSONArray array = new JSONArray(variants);
                            for(int i =0 ; i < array.length(); i++){
                                JSONObject variantJson  = array.getJSONObject(i);
                                if(variantJson.getInt("id") == productItem.getIdVariant()){
                                        orderItems.remove(productItem);
                                        notifyDataSetChanged();
                                        continue;
                                }
                                newArray.put(variantJson);
                            }

                            Log.i(TAG, "onClick: "  + newArray.length());
                            if(newArray.length() == 0){
                                Log.i(TAG, "onClick: ok");
                                user.setCurrentOrderRestaurantSlug(null);
                            }
                            store.setStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS,newArray.toString());
                            ((BasketActivity)context).checkOutUpdateView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });



        if(productItem.isSolidByPoints()){
            holder.productDescription.setText(productItem.getPoints() + " баллов");
            holder.productDescription.setVisibility(View.VISIBLE);
            holder.btnPlus.setVisibility(View.GONE);
            holder.btnMinus.setVisibility(View.GONE);
            holder.countProducts.setVisibility(View.GONE);
            holder.productPrice.setVisibility(View.GONE);
        }else{
            holder.productDescription.setVisibility(View.GONE);
        }
        String price = Validation.twoNumbersAfterAfterPoint(productItem.getPrice()*productItem.getCount()) + " p.";
        holder.productPrice.setText(price);
        String weight = productItem.getWidth();
        String size = productItem.getSize();
        if(weight != null){
            holder.productWidth.setText(weight);
        }else if (size != null){
            holder.productWidth.setText(size);
        }
        holder.countProducts.setText(productItem.getCount() + "");
        holder.productName.setText(productItem.getName());

        Glide.with(context)
                .load(productItem.getIcon())
                .into(holder.productImg);
        return convertView;
    }


    static class ViewHolder {
        TextView  productName;
        TextView  productWidth;
        TextView  productPrice;
        TextView  productDescription;
        ImageView productImg;
        TextView  countProducts;
        Button    btnPlus, btnMinus, btnRemove;
    }

}