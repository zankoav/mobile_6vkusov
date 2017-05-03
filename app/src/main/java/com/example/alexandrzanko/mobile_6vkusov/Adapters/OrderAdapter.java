package com.example.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alexandrzanko.mobile_6vkusov.Activities.Restaurant.ProductActivity;
import com.example.alexandrzanko.mobile_6vkusov.Fragments.ProductFragment;
import com.example.alexandrzanko.mobile_6vkusov.Models.Category;
import com.example.alexandrzanko.mobile_6vkusov.Models.OrderItem;
import com.example.alexandrzanko.mobile_6vkusov.Models.OrderItemFood;
import com.example.alexandrzanko.mobile_6vkusov.Models.Product;
import com.example.alexandrzanko.mobile_6vkusov.Models.ProductItem;
import com.example.alexandrzanko.mobile_6vkusov.Models.Variant;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Singleton;
import com.example.alexandrzanko.mobile_6vkusov.Users.Basket;
import com.example.alexandrzanko.mobile_6vkusov.Users.STATUS;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by alexandrzanko on 5/2/17.
 */

public class OrderAdapter extends BaseAdapter{

    private ArrayList<OrderItem> listData;
    private LayoutInflater layoutInflater;
    private final String slug;
    private Context context;

    private final String TAG = this.getClass().getSimpleName();


    public OrderAdapter(Context context, ArrayList<OrderItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.slug = ((ProductActivity)context).getSlug();
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
        final OrderAdapter.ViewHolder holder;
        convertView = layoutInflater.inflate(R.layout.order_item_layout, null);
        holder = new OrderAdapter.ViewHolder();

        holder.restImg           = (ImageView) convertView.findViewById(R.id.product_icon);
        holder.restName          = (TextView) convertView.findViewById(R.id.product_name);
        holder.itemsListView     = (ListView) convertView.findViewById(R.id.product_variants);

        convertView.setTag(holder);
        final OrderItem item = listData.get(position);
        Picasso.with(context)
                .load(item.getRestaurant().get_iconURL())
                .placeholder(R.drawable.ic_thumbs_up) //показываем что-то, пока не загрузится указанная картинка
                .error(R.drawable.ic_thumb_down) // показываем что-то, если не удалось скачать картинку
                .into(holder.restImg);


        final ArrayList<OrderItemFood> items = item.getFoods();
        OrderFoodAdapter adapter = new OrderFoodAdapter(this.context, items);
        holder.itemsListView.setAdapter(adapter);
        float density = context.getResources().getDisplayMetrics().density;
        ViewGroup.LayoutParams params = holder.itemsListView.getLayoutParams();
        params.height = Math.round(50 * density * items.size());
        holder.itemsListView.setLayoutParams(params);

        return convertView;
    }

    static class ViewHolder {
        ImageView restImg;
        TextView  restName;
        ListView itemsListView;
    }
}