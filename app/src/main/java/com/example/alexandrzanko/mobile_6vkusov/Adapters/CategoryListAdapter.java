package com.example.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexandrzanko.mobile_6vkusov.Models.Category;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by alexandrzanko on 3/7/17.
 */

public class CategoryListAdapter extends BaseAdapter {

    private ArrayList<Category> listData, listData2;
    private LayoutInflater layoutInflater;
    private Context context;

    public CategoryListAdapter(Context context, ArrayList<Category> listData, ArrayList<Category> listData2) {
        this.listData = listData;
        this.listData2 = listData2;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;

    }

    @Override
    public int getCount() {
        return (listData.size() + listData2.size());
    }

    @Override
    public Object getItem(int position) {
        if(position < listData.size()){
            return listData.get(position);
        }else{
            return listData2.get(position - listData.size());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (position < listData.size()) {
            ViewHolder holder;
            convertView = layoutInflater.inflate(R.layout.category_type_1, null);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView)convertView.findViewById(R.id.thumbImage);
            convertView.setTag(holder);
            Category category = listData.get(position);
            holder.titleView.setText(category.getTitle());
            Picasso.with(context)
                    .load(category.getUrlImg())
                    .placeholder(R.drawable.ic_thumbs_up) //показываем что-то, пока не загрузится указанная картинка
                    .error(R.drawable.ic_thumb_down) // показываем что-то, если не удалось скачать картинку
                    .into(holder.imageView);
        }else{
            ViewHolder2 holder;
            convertView = layoutInflater.inflate(R.layout.category_type_2, null);
            holder = new ViewHolder2();
            holder.titleView = (TextView) convertView.findViewById(R.id.cat2_title);
            convertView.setTag(holder);
            Category category = listData2.get(position - listData.size());
            holder.titleView.setText(category.getTitle());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView titleView;
        ImageView imageView;
    }

    static class ViewHolder2 {
        TextView titleView;
    }
}
