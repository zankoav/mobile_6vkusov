package com.example.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexandrzanko.mobile_6vkusov.Models.Restaurant;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by alexandrzanko on 3/9/17.
 */

public class RestaurantsListAdapter extends BaseAdapter {
    private ArrayList<Restaurant> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public RestaurantsListAdapter(Context context, ArrayList<Restaurant> listData) {
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
        RestaurantsListAdapter.ViewHolder holder;
        convertView = layoutInflater.inflate(R.layout.restaurant_item_layout, null);
        holder = new RestaurantsListAdapter.ViewHolder();
        holder.nameTV = (TextView) convertView.findViewById(R.id.restaurants_name);
        holder.timeTV = (TextView) convertView.findViewById(R.id.restaurants_time);
        holder.deliveryType = (TextView) convertView.findViewById(R.id.restaurants_delivery);
        holder.kitchenType = (TextView) convertView.findViewById(R.id.restaurants_kitchen_type);
        holder.likesTV = (TextView) convertView.findViewById(R.id.restaurants_likes);
        holder.dislikesTV = (TextView) convertView.findViewById(R.id.restaurants_dislikes);
        holder.imageView = (ImageView) convertView.findViewById(R.id.restaurants_icon);
        convertView.setTag(holder);

        Restaurant restaurant = listData.get(position);
        holder.nameTV.setText(restaurant.getName());
        holder.timeTV.setText(restaurant.getDeliveryTime() + " мин.");
        holder.deliveryType.setText(restaurant.getMinimalPrice().toString() + " руб.");
        holder.kitchenType.setText(join(", ",restaurant.getKitchens()));
        holder.likesTV.setText(restaurant.getLikes() + "");
        holder.dislikesTV.setText(restaurant.getDislikes() + "");

        Picasso.with(context)
                .load(restaurant.getUrl())
                .placeholder(R.drawable.ic_thumbs_up) //показываем что-то, пока не загрузится указанная картинка
                .error(R.drawable.ic_thumb_down) // показываем что-то, если не удалось скачать картинку
                .into(holder.imageView);
        return convertView;
    }

    public String join(String join, String... strings) {
        if (strings == null || strings.length == 0) {
            return "";
        } else if (strings.length == 1) {
            return strings[0];
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(strings[0]);
            for (int i = 1; i < strings.length; i++) {
                sb.append(join).append(strings[i]);
            }
            return sb.toString();
        }
    }

    static class ViewHolder {
        TextView nameTV;
        TextView kitchenType;
        TextView timeTV;
        TextView deliveryType;
        TextView likesTV;
        TextView dislikesTV;
        ImageView imageView;
    }

}
