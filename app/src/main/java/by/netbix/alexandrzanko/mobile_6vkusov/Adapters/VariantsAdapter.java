package by.netbix.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import by.netbix.alexandrzanko.mobile_6vkusov.Models.Variant;

import java.util.ArrayList;

/**
 * Created by alexandrzanko on 1/24/17.
 */

public class VariantsAdapter extends BaseAdapter {

    private ArrayList<Variant> listData;
    private LayoutInflater layoutInflater;
    private Button btn;

    public VariantsAdapter(Context context, ArrayList<Variant> listData, Button btn) {
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(context);
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
        convertView = layoutInflater.inflate(by.netbix.alexandrzanko.mobile_6vkusov.R.layout.product_variant_item_layout, null);
        holder = new ViewHolder();
        convertView.setBackgroundColor(Color.WHITE);
        holder.productPrice  = (TextView) convertView.findViewById(by.netbix.alexandrzanko.mobile_6vkusov.R.id.product_price);
        holder.productType   = (TextView) convertView.findViewById(by.netbix.alexandrzanko.mobile_6vkusov.R.id.product_width);
        holder.productCount  = (TextView) convertView.findViewById(by.netbix.alexandrzanko.mobile_6vkusov.R.id.product_count);
        holder.btnPlus       = (Button) convertView.findViewById(by.netbix.alexandrzanko.mobile_6vkusov.R.id.product_btn_plus);
        holder.btnMinus      = (Button) convertView.findViewById(by.netbix.alexandrzanko.mobile_6vkusov.R.id.product_btn_minus);

        convertView.setTag(holder);

        final Variant variant = listData.get(position);
        holder.productCount.setText(variant.get_count() + "");
        String weight = variant.get_weigth();
        String size = variant.get_size();
        if(!weight.equals("null")){
            holder.productType.setText("Вес " + weight);
        }
        if(!size.equals("null")){
            holder.productType.setText(size);
        }

        String price = getTotalPriceVariant(variant);
        holder.productPrice.setText(price + " р.");

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                variant.addCount();
                btn.setVisibility(View.VISIBLE);
                holder.productCount.setText(variant.get_count() + "");
                String price = getTotalPriceVariant(variant);
                holder.productPrice.setText(price + " р.");


            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                variant.minusCount();
                if(variant.get_count() > 0) {
                    holder.productCount.setText(variant.get_count() + "");
                    String price = getTotalPriceVariant(variant);
                    holder.productPrice.setText(price + " р.");
                }else{
                    holder.productCount.setText(variant.get_count() + "");
                    if (variant.get_count() == 0){
                        btn.setVisibility(View.GONE);
                        holder.productPrice.setText(getTotalPriceVariant(variant) + " р.");
                    }
                }
            }
        });
        return convertView;
    }

    private String getTotalPriceVariant(Variant variant){
        int count = variant.get_count();
        if (count == 0){
            count = 1;
        }
        double price = variant.get_price() * count;
        double pr = (double) Math.round(price*100);
        double prD = pr/100;
        return pr%10 == 0 ? prD + "0" : prD + "";
    }

    static class ViewHolder {
        TextView  productType;
        TextView  productPrice;
        TextView  productCount;
        Button    btnPlus, btnMinus;
    }
}
