package com.example.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alexandrzanko.mobile_6vkusov.Models.Variant;
import com.example.alexandrzanko.mobile_6vkusov.R;

import java.util.ArrayList;

/**
 * Created by alexandrzanko on 1/24/17.
 */

public class VariantsAdapter extends BaseAdapter {

    private ArrayList<Variant> listData;
    private LayoutInflater layoutInflater;
    private ListView listView;
    private Button btn;

    public VariantsAdapter(Context context, ArrayList<Variant> listData, ListView listView, Button btn) {
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(context);
        this.listView = listView;
        this.btn = btn;
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
        convertView = layoutInflater.inflate(R.layout.product_variant_item_layout, null);
        holder = new ViewHolder();
        convertView.setBackgroundColor(Color.WHITE);
        holder.productPrice  = (TextView) convertView.findViewById(R.id.product_price);
        holder.productType   = (TextView) convertView.findViewById(R.id.product_width);
        holder.productCount  = (TextView) convertView.findViewById(R.id.product_count);
        holder.btnPlus       = (Button) convertView.findViewById(R.id.product_btn_plus);
        holder.btnMinus      = (Button) convertView.findViewById(R.id.product_btn_minus);

        convertView.setTag(holder);

        final Variant variant = listData.get(position);
        holder.productCount.setText("0");
        String weight = variant.get_weigth();
        String size = variant.get_size();
        if(weight != "null"){
            holder.productType.setText("Вес " + weight);
        }
        if(size != "null"){
            holder.productType.setText(size);
        }
        holder.productPrice.setText(variant.get_price() + " р.");

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(holder.productCount.getText().toString());
                    count++;
                btn.setVisibility(View.VISIBLE);
                holder.productCount.setText(count + "");
                double price = count * variant.get_price();
                holder.productPrice.setText(price + " р.");
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(holder.productCount.getText().toString());
                count--;
                if(count > 0) {
                    holder.productCount.setText(count + "");
                    double price = count * variant.get_price();
                    holder.productPrice.setText(price + " р.");
                }else{
                    holder.productCount.setText("0");
                    if (getCountListViewProducts() == 0){
                        btn.setVisibility(View.GONE);
                        holder.productPrice.setText(variant.get_price() + " р.");
                    }
                }
            }
        });
        return convertView;
    }

    private int getCountListViewProducts(){
        int count = 0;
        for (int i = 0; i < listData.size(); i++){
            View view = listView.getChildAt(i);
            TextView tv = (TextView) view.findViewById(R.id.product_count);
            if (tv != null){
                count += Integer.parseInt(tv.getText().toString());
            }
        }
        return count;
    }

    static class ViewHolder {
        TextView  productType;
        TextView  productPrice;
        TextView  productCount;
        Button    btnPlus, btnMinus;
    }
}
