package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import by.vkus.alexandrzanko.mobile_6vkusov.Models.Order.MOrderFood;
import by.vkus.alexandrzanko.mobile_6vkusov.R;

import java.util.List;

/**
 * Created by alexandrzanko on 5/3/17.
 */

public class OrderFoodAdapter extends BaseAdapter {

    private List<MOrderFood> listData;
    private LayoutInflater layoutInflater;

    public OrderFoodAdapter(Context context, List<MOrderFood> listData) {
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(context);
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
        final OrderFoodAdapter.ViewHolder holder;
        convertView = layoutInflater.inflate(R.layout.food_variant_item_layout, null);
        holder = new OrderFoodAdapter.ViewHolder();
        convertView.setBackgroundColor(Color.WHITE);
        holder.productName = (TextView) convertView.findViewById(R.id.name);
        holder.productCount = (TextView) convertView.findViewById(R.id.count);

        convertView.setTag(holder);

        final MOrderFood item = listData.get(position);
        holder.productCount.setText(item.getCount() + "");
        holder.productName.setText(item.getName() + "");

        return convertView;
    }


    static class ViewHolder {
        TextView productName;
        TextView productCount;
    }
}