package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.AuthActivities.LoginActivityV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Activities.BaseMenuActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Discounts.Discount;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexandrzanko on 8/12/17.
 */

public class DiscountAdapter extends BaseAdapter {


    private List<Discount> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public final String TAG = this.getClass().getSimpleName();


    public DiscountAdapter(Context context, List<Discount> listData) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Discount getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final DiscountAdapter.ViewHolder holder;
        convertView = layoutInflater.inflate(R.layout.discount_item_layout, null);
        holder = new DiscountAdapter.ViewHolder();
        convertView.setTag(holder);
        final Discount discount = listData.get(position);
        holder.discountImg           = (ImageView) convertView.findViewById(R.id.discount_icon);
        holder.discountName          = (TextView) convertView.findViewById(R.id.discount_name);
        holder.discountPoints   = (TextView) convertView.findViewById(R.id.discount_points);
        holder.discountDescription   = (TextView) convertView.findViewById(R.id.discount_description);
        holder.discountLabel   = (TextView) convertView.findViewById(R.id.discount_label);

        holder.getButton    = (Button) convertView.findViewById(R.id.discount_get);
        holder.discountLabel.setText(discount.getLabel());
        holder.discountName.setText(discount.getTitle());
        holder.discountName.setBackgroundColor(Color.parseColor(discount.getBgColor()));
        holder.discountPoints.setText(discount.getPoints()+ " баллов");
        holder.discountDescription.setText(discount.getDescription());

        if(SingletonV2.currentState().getIUser().getStatus().equals(STATUS.REGISTER)){
            holder.getButton.setText("Получить скидку");
            holder.getButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorButtons));
        }else{
            holder.getButton.setText("Войти");
            holder.getButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOdnoklasniki));
        }

        holder.getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SingletonV2.currentState().getIUser().getStatus().equals(STATUS.REGISTER)){

                    ApiController.getApi().getDiscountStep1(SingletonV2.currentState().getIUser().getSession(),discount.getId()).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if(response.code() == 301){
                                showAlertStep2(discount);
                            }else if(response.code() == 308){
                                Toast.makeText(context, "У Вас не достаточно баллов", Toast.LENGTH_SHORT).show();
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

        Glide.with(context)
                .load(discount.getPicture())
                .into(holder.discountImg);

        return convertView;
    }



    private void showAlertStep2(final Discount discount) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("Внимание!");
        builder.setMessage("В Вы уверены, что хотите поучить скидку - " + discount.getTitle() + " ?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                ApiController.getApi().getDiscountStep2(SingletonV2.currentState().getIUser().getSession(), discount.getId()).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.code() == 301){
                            showAlert(discount);
                        }else if(response.code() == 302){
                            Toast.makeText(context, "Извините, но на данную скидку промокоды закончились", Toast.LENGTH_LONG).show();
                        }else if(response.code() == 308){
                            Toast.makeText(context, "У Вас не достаточно баллов", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.i(TAG, "onResponse code = " + response.code());

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
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void showAlert(final Discount discount) {
        int points = SingletonV2.currentState().getIUser().getPoints();
        SingletonV2.currentState().getIUser().setPoints(points - discount.getPoints());
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("Внимание!");
        builder.setMessage("Вам на почту "+SingletonV2.currentState().getIUser().getEmail() +" отправлен промокод");
        builder.setNeutralButton("Закрыть", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
        builder.setCancelable(true);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
            }
        });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    static class ViewHolder {
        ImageView discountImg;
        TextView discountName, discountPoints;
        TextView  discountDescription;
        TextView  discountLabel;
        Button getButton;
    }
}

