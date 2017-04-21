package com.example.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexandrzanko.mobile_6vkusov.Models.Order;
import com.example.alexandrzanko.mobile_6vkusov.R;

import java.util.ArrayList;


/**
 * Created by alexandrzanko on 07/12/16.
 */

public class BasketAdapter extends BaseAdapter {
    private ArrayList<Order> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public BasketAdapter(Context context, ArrayList<Order> listData) {
        this.listData = listData;
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
        convertView = layoutInflater.inflate(R.layout.product_item_basket_layout, null);
        holder = new ViewHolder();
        holder.productName     = (TextView) convertView.findViewById(R.id.product_name);
        holder.productDescription = (TextView) convertView.findViewById(R.id.product_description);
        holder.productImg      = (ImageView) convertView.findViewById(R.id.product_icon);
        holder.productWidth    = (TextView) convertView.findViewById(R.id.product_width);
        holder.productPrice    = (TextView) convertView.findViewById(R.id.product_price);
        holder.countProducts   = (TextView) convertView.findViewById(R.id.product_count);
        holder.btnPlus         = (Button) convertView.findViewById(R.id.product_btn_plus);
        holder.btnMinus        = (Button) convertView.findViewById(R.id.product_btn_minus);
        convertView.setTag(holder);

        final Order order = listData.get(position);

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count =  Integer.parseInt(holder.countProducts.getText().toString());
                count++;
                holder.countProducts.setText(count + "");
                order.setCount(count);
                double price = count * order.getVariant().get_price();
                holder.productPrice.setText(price + " р.");
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count =  Integer.parseInt(holder.countProducts.getText().toString());
                count--;
                if (count > 0) {
                    holder.countProducts.setText(count + "");
                    order.setCount(count);
                    double price = count * order.getVariant().get_price();
                    holder.productPrice.setText(price + " р.");
                }else{
                    listData.remove(order);
                    notifyDataSetChanged();
                }
            }
        });
        double price = order.getCount() * order.getVariant().get_price();
        holder.productDescription.setText(order.getProduct().get_description());
        holder.productPrice.setText(price + " р.");
        holder.productWidth.setText("Вес " + order.getVariant().get_weigth());
        holder.countProducts.setText(order.getCount() + "");
        holder.productName.setText( order.getProduct().get_name());

        return convertView;
    }


    static class ViewHolder {
        TextView  productName;
        TextView  productWidth;
        TextView  productPrice;
        TextView  productDescription;
        ImageView productImg;
        TextView  countProducts;
        Button    btnPlus, btnMinus;
    }

}