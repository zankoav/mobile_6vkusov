package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Activities.RestaurantsCardActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.FreeFood;
import by.vkus.alexandrzanko.mobile_6vkusov.R;

/**
 * Created by alexandrzanko on 5/2/17.
 */

public class PromoAdapter extends BaseAdapter {

    private final String TAG = this.getClass().getSimpleName();

    private ArrayList<FreeFood> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public PromoAdapter(Context context, ArrayList<FreeFood> listData) {
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
        convertView = layoutInflater.inflate(R.layout.free_food_item_layout, null);
        holder = new ViewHolder();

        holder.productImg           = (ImageView) convertView.findViewById(R.id.free_food_icon);
        holder.productName          = (TextView) convertView.findViewById(R.id.free_food_name);
        holder.productPoints   = (TextView) convertView.findViewById(R.id.free_food_points);

        holder.productDescription   = (TextView) convertView.findViewById(R.id.free_food_description);
        holder.addToBasketButton    = (Button) convertView.findViewById(R.id.free_food_add_basket);

        convertView.setTag(holder);
        final FreeFood food = listData.get(position);
        holder.productName.setText(food.getName());
        holder.productPoints.setText(food.getPoints()+ " баллов");
        String description = food.getDescription() == "null" ? "" : food.getDescription();
        holder.productDescription.setText(description);

        Picasso.with(context)
                .load(food.getUrlImg())
                .placeholder(R.drawable.product) //показываем что-то, пока не загрузится указанная картинка
                .error(R.drawable.product) // показываем что-то, если не удалось скачать картинку
                .into(holder.productImg);


        holder.addToBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slug = food.getRestaurant_slug();
                Intent intent = new Intent(context, RestaurantActivity.class);
                intent.putExtra(RestaurantsCardActivity.EXTRA_RESTAURANT, slug);
                context.startActivity(intent);
            }
        });

        return convertView;
    }


    static class ViewHolder {
        ImageView productImg;
        TextView productName, productPoints;
        TextView  productDescription;
        Button addToBasketButton;
    }
}
