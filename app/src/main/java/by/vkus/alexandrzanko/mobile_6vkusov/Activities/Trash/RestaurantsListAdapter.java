package by.vkus.alexandrzanko.mobile_6vkusov.Activities.Trash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import by.vkus.alexandrzanko.mobile_6vkusov.Models.Restaurant;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by alexandrzanko on 3/9/17.
 */

public class RestaurantsListAdapter extends BaseAdapter implements Filterable {
    private ArrayList<Restaurant> listData, filterList;

    private LayoutInflater layoutInflater;

    private Context context;
    private RestaurantsFilter filter;


    public RestaurantsListAdapter(Context context, ArrayList<Restaurant> listData) {
        this.listData = listData;
        this.filterList = listData;
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
        convertView = layoutInflater.inflate(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.restaurant_item_layout, null);
        holder = new RestaurantsListAdapter.ViewHolder();
        holder.nameTV = (TextView) convertView.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_name);
        holder.timeTV = (TextView) convertView.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_time);
        holder.deliveryType = (TextView) convertView.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_delivery);
        holder.kitchenType = (TextView) convertView.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_kitchen_type);
        holder.likesTV = (TextView) convertView.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_likes);
        holder.dislikesTV = (TextView) convertView.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_dislikes);
        holder.imageView = (ImageView) convertView.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_icon);
        convertView.setTag(holder);

        Restaurant restaurant = listData.get(position);
        holder.nameTV.setText(restaurant.get_name());
        holder.timeTV.setText(restaurant.get_delivery_time() + " мин.");
        holder.deliveryType.setText((new Double(restaurant.get_minimal_price())).toString() + " руб.");
        holder.kitchenType.setText(join(", ",restaurant.get_kitchens()));
        holder.likesTV.setText(restaurant.get_comments().get("likes") + "");
        holder.dislikesTV.setText(restaurant.get_comments().get("dislikes") + "");

        Picasso.with(context)
                .load(restaurant.get_iconURL())
                .placeholder(by.vkus.alexandrzanko.mobile_6vkusov.R.drawable.ic_thumbs_up) //показываем что-то, пока не загрузится указанная картинка
                .error(by.vkus.alexandrzanko.mobile_6vkusov.R.drawable.ic_thumb_down) // показываем что-то, если не удалось скачать картинку
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

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new RestaurantsFilter();
        }
        return filter;
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

    class RestaurantsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if(constraint != null && constraint.length() > 0){

                constraint = constraint.toString().toUpperCase();
                ArrayList<Restaurant> filters = new ArrayList<>();

                for (int i = 0; i < filterList.size(); i++){
                    if(filterList.get(i).get_name().toUpperCase().contains(constraint)){
                        Restaurant restaurant = filterList.get(i);
                        filters.add(restaurant);
                    }
                }
                filterResults.count = filters.size();
                filterResults.values = filters;
            }else {
                filterResults.count = filterList.size();
                filterResults.values = filterList;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listData = (ArrayList<Restaurant>)results.values;
            notifyDataSetInvalidated();
        }
    }

}
