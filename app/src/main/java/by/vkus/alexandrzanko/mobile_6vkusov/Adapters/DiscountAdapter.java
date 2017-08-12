package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
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

import com.bumptech.glide.Glide;

import java.util.List;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.AuthActivities.LoginActivityV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Activities.BaseMenuActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Discounts.Discount;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;

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
                    Log.i(TAG, "onClick: register" );
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

    static class ViewHolder {
        ImageView discountImg;
        TextView discountName, discountPoints;
        TextView  discountDescription;
        TextView  discountLabel;
        Button getButton;
    }
}

