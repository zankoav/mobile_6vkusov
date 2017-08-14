package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;

import java.util.List;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.AuthActivities.LoginActivityV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Activities.BaseMenuActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantActivityV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantsActivityV2;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.FoodByPoint;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexandrzanko on 5/2/17.
 */

public class PromoAdapter extends BaseAdapter {

    private List<FoodByPoint> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private CounterFab fab;

    public final String TAG = this.getClass().getSimpleName();


    public PromoAdapter(Context context, List<FoodByPoint> listData) {
        this.listData = listData;
        fab = ((BaseMenuActivity)context).getFab();
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        convertView = layoutInflater.inflate(R.layout.free_food_item_layout, null);
        holder = new ViewHolder();

        holder.productImg           = (ImageView) convertView.findViewById(R.id.free_food_icon);
        holder.productName          = (TextView) convertView.findViewById(R.id.free_food_name);
        holder.productPoints   = (TextView) convertView.findViewById(R.id.free_food_points);

        holder.productDescription   = (TextView) convertView.findViewById(R.id.free_food_description);
        holder.addToBasketButton    = (Button) convertView.findViewById(R.id.free_food_add_basket);
        holder.restaurantBtn    = (Button) convertView.findViewById(R.id.free_food_restaurant);

        convertView.setTag(holder);
        final FoodByPoint food = listData.get(position);
        holder.productName.setText(food.getName());
        holder.productPoints.setText(food.getPoints()+ " баллов");
        holder.productDescription.setText("");
        holder.restaurantBtn.setText(food.getRestaurant_name());
        buttonUnderlineText(holder.restaurantBtn);

        Glide.with(context)
                .load(food.getIcon())
                .into(holder.productImg);

        if (SingletonV2.currentState().getIUser().getStatus().equals(STATUS.REGISTER)){
            holder.addToBasketButton.setText("Добавить");
            holder.addToBasketButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorButtons));
        }else {
            holder.addToBasketButton.setText("Войти");
            holder.addToBasketButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOdnoklasniki));
        }

        holder.addToBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IUser user = SingletonV2.currentState().getIUser();
                if (user.getStatus().equals(STATUS.REGISTER)){
                    int variantId = food.getId();
                    Log.i(TAG, "foodPointsPressed: size = " + variantId);
                    ApiController.getApi().addFreeFoodOrderByRegisterUser(user.getSession(),variantId).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 301){
                                fab.increase();
                            }else if(response.code() == 302){
                                showAlertNewOrder(food);
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

                }else{
                    Intent intent = new Intent(context, LoginActivityV2.class);
                    context.startActivity(intent);
                    ((BaseMenuActivity)context).finish();
                }
            }
        });

        holder.restaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(context, RestaurantActivityV2.class);
            intent.putExtra(RestaurantsActivityV2.EXTRA_RESTAURANT,food.getRestaurant_slug());
            context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void showAlertByHaveNoPoints() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

    private void showAlertNewOrder(final FoodByPoint foodByPoint) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Внимание!");
        builder.setMessage("В Вашей корзине присутствуют товары из другого ресторана. Создать новую корзину ?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                final IUser user = SingletonV2.currentState().getIUser();
                int variantId = foodByPoint.getId();
                ApiController.getApi().createNewOrderFreeFoodByRegisterUser(user.getSession(), variantId).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.code() == 301){
                            fab.setCount(1);
                            user.setCurrentOrderRestaurantSlug(foodByPoint.getRestaurant_slug());
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

    private void buttonUnderlineText(Button button){
        button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    static class ViewHolder {
        ImageView productImg;
        TextView productName, productPoints;
        TextView  productDescription;
        Button addToBasketButton, restaurantBtn;
    }
}
