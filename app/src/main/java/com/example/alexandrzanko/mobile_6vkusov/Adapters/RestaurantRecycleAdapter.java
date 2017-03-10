package com.example.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexandrzanko.mobile_6vkusov.Models.Restaurant;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexandrzanko on 3/9/17.
 */

public class RestaurantRecycleAdapter extends RecyclerView.Adapter<RestaurantRecycleAdapter.ViewHolder>  implements Filterable{

    private final String TAG = this.getClass().getSimpleName();
    ArrayList<Restaurant> restaurants, filterList, filtersAdd;
    private RestaurantsFilter filter;
    Context context;

    public final String NEW = "new";
    public final String FREE_FOOD = "free_food";
    public final String FLASH = "flash";
    public final String SALE = "sale";

    private HashMap<String,Boolean> rules;
    private int priceMin;

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
        changeRestList();
    }

    public void setRule(String key, Boolean value){
        this.rules.put(key,value);
        changeRestList();
    }

    public RestaurantRecycleAdapter(ArrayList<Restaurant> restaurants, Context context){
            this.restaurants = restaurants;
            this.filterList = restaurants;
            this.context = context;

            rules = new HashMap<>();
            rules.put(NEW, false);
            rules.put(FREE_FOOD, false);
            rules.put(FLASH, false);
            rules.put(SALE, false);
            priceMin = 0;
    }

    private void changeRestList(){

        boolean checkNew = rules.get(NEW);
        boolean checkFreeFood = rules.get(FREE_FOOD);
        boolean checkFlash = rules.get(FLASH);
        boolean checkSale = rules.get(SALE);
        boolean seekBar = priceMin > 0 ? true: false;
        if (checkNew || checkFreeFood || checkFlash || checkSale || seekBar){
            filtersAdd = new ArrayList<>();
            for(int i = 0 ; i < filterList.size(); i++){

                boolean match = true;
                Restaurant restaurant = filterList.get(i);

                if (checkNew){
                    if(!restaurant.isNew()){
                        match = false;
                        continue;
                    }
                }

                if (checkFreeFood){
                    if(!restaurant.isFreeFood()){
                        match = false;
                        continue;
                    }
                }

                if (checkFlash){
                    if(!restaurant.isFlash()){
                        match = false;
                        continue;
                    }
                }

                if (checkSale){
                    if(!restaurant.isSale()){
                        match = false;
                        continue;
                    }
                }

                if (seekBar){
                    if(restaurant.getMinimalPrice().intValue() > priceMin){
                        match = false;
                        continue;
                    }
                }

                if(match){
                    filtersAdd.add(restaurant);
                }
            }
            restaurants = filtersAdd;
        }else {
            filtersAdd = null;
            restaurants = filterList;
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_restaurant_layout,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setRestaurant(restaurants.get(position));
    }

    @Override
    public int getItemCount() {
        return this.restaurants.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new RestaurantsFilter();
        }
        return filter;
    }

    class RestaurantsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if(constraint != null && constraint.length() > 0){
                constraint = constraint.toString().toUpperCase();
                ArrayList<Restaurant> filters = new ArrayList<>();
                ArrayList<Restaurant> filterX;
                if(filtersAdd != null){
                    filterX = filtersAdd.size() < filterList.size() ? filtersAdd : filterList;
                }else{
                    filterX = filterList;
                }
                for (int i = 0; i < filterX.size(); i++){
                    if(filterX.get(i).getName().toUpperCase().contains(constraint)){
                        Restaurant restaurant = new Restaurant(filterX.get(i).getBaseUrl(), filterX.get(i).getJson());
                        filters.add(restaurant);
                    }
                }
                filterResults.count = filters.size();
                filterResults.values = filters;
            }else {
                if(filtersAdd != null){
                    filterResults.count = filtersAdd.size();
                    filterResults.values = filtersAdd;
                }else{
                    filterResults.count = filterList.size();
                    filterResults.values = filterList;
                }
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            restaurants = (ArrayList<Restaurant>)results.values;
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
            TextView nameTV;
            TextView kitchenType;
            TextView timeTV;
            TextView deliveryType;
            TextView likesTV;
            TextView dislikesTV;
            ImageView imageView;


            public ViewHolder(View view){
                super(view);
                nameTV = (TextView) view.findViewById(R.id.restaurants_name);
                timeTV = (TextView) view.findViewById(R.id.restaurants_time);
                deliveryType = (TextView) view.findViewById(R.id.restaurants_delivery);
                kitchenType = (TextView) view.findViewById(R.id.restaurants_kitchen_type);
                likesTV = (TextView) view.findViewById(R.id.restaurants_likes);
                dislikesTV = (TextView) view.findViewById(R.id.restaurants_dislikes);
                imageView = (ImageView) view.findViewById(R.id.restaurants_icon);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Log.i(TAG, "onClick: " + restaurants.get(position).getName());
                    }
                });
            }
            public void setRestaurant(Restaurant restaurant){
                nameTV.setText(restaurant.getName());
                timeTV.setText(restaurant.getDeliveryTime() + " мин.");
                deliveryType.setText(restaurant.getMinimalPrice().toString() + " руб.");
                kitchenType.setText(restaurant.getKitchens());
                likesTV.setText(restaurant.getLikes() + "");
                dislikesTV.setText(restaurant.getDislikes() + "");
                Picasso.with(context)
                        .load(restaurant.getUrl())
                        .placeholder(R.drawable.ic_thumbs_up) //показываем что-то, пока не загрузится указанная картинка
                        .error(R.drawable.ic_thumb_down) // показываем что-то, если не удалось скачать картинку
                        .into(imageView);
            }

    }
}
