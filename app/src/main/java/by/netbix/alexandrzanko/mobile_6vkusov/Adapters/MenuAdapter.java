package by.netbix.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import by.netbix.alexandrzanko.mobile_6vkusov.Models.Category;

/**
 * Created by alexandrzanko on 5/24/17.
 */

public class MenuAdapter extends BaseAdapter {

    private ArrayList<String> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public MenuAdapter(Context context, ArrayList<String> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
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
            convertView = layoutInflater.inflate(by.netbix.alexandrzanko.mobile_6vkusov.R.layout.menu_category_item, null);
            holder = new MenuAdapter.ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(by.netbix.alexandrzanko.mobile_6vkusov.R.id.cat2_title);
            convertView.setTag(holder);
            holder.titleView.setText(listData.get(position));

        return convertView;
    }

    static class ViewHolder {
        TextView titleView;
    }
}
