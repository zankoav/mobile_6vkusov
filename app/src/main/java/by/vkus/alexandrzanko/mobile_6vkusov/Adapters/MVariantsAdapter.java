package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;

import by.vkus.alexandrzanko.mobile_6vkusov.Models.MVariant;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Variant;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandrzanko on 1/24/17.
 */

public class MVariantsAdapter extends BaseAdapter {

    private List<MVariant> listData;
    private LayoutInflater layoutInflater;
    private CounterFab btn;

    public MVariantsAdapter(Context context, List<MVariant> listData, CounterFab btn) {
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(context);
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
        holder.productCount  = (TextView) convertView.findViewById(R.id.product_count);
        holder.btnPlus       = (Button) convertView.findViewById(R.id.product_btn_plus);
        holder.btnMinus      = (Button) convertView.findViewById(R.id.product_btn_minus);

        convertView.setTag(holder);

        final MVariant variant = listData.get(position);
        if(listData.size() > 0){
            holder.productCount.setText("0");
        }else{
            holder.productCount.setText("1");
        }
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
                Log.i("btnPlus", "onClick: " + variant.getPrice());
//                variant.addCount();
//                btn.setVisibility(View.VISIBLE);
//                holder.productCount.setText(variant.get_count() + "");
//                String price = Validation.twoNumbersAfterAfterPoint(variant.get_price());
//                holder.productPrice.setText(price + " р.");

                btn.increase();

            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("btnMinus", "onClick: " + variant.getPrice());
                btn.decrease();
//                variant.minusCount();
//                if(variant.get_count() > 0) {
//                    holder.productCount.setText(variant.get_count() + "");
//                    String price = Validation.twoNumbersAfterAfterPoint(variant.get_price());
//                    holder.productPrice.setText(price + " р.");
//                }else{
//                    holder.productCount.setText(variant.get_count() + "");
//                    if (variant.get_count() == 0){
//                        btn.setVisibility(View.GONE);
//                        holder.productPrice.setText(Validation.twoNumbersAfterAfterPoint(variant.get_price()) + " р.");
//                    }
//                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView  productType;
        TextView  productPrice;
        TextView  productCount;
        Button    btnPlus, btnMinus;
    }
}
