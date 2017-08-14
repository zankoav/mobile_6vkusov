package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

import by.vkus.alexandrzanko.mobile_6vkusov.Models.MenuRestaurant;
import by.vkus.alexandrzanko.mobile_6vkusov.R;

/**
 * Created by alexandrzanko on 5/24/17.
 */

public class MenuAdapter extends BaseAdapter {

    private List<MenuRestaurant> listData;
    private LayoutInflater layoutInflater;

    public MenuAdapter(Context context, List<MenuRestaurant> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return (listData.size());
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

            MenuAdapter.ViewHolder holder;
            convertView = layoutInflater.inflate(R.layout.menu_restaurant_category, null);
            holder = new MenuAdapter.ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.cat_title);
            holder.countView = (TextView) convertView.findViewById(R.id.cat_count);
        convertView.setTag(holder);
            holder.titleView.setText(listData.get(position).getName());
            holder.countView.setText("(" + listData.get(position).getCount() + ")");

        return convertView;
    }

    static class ViewHolder {
        TextView titleView, countView;
    }
}
