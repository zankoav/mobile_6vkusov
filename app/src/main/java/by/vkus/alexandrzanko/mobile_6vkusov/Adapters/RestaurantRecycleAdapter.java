package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Activities.RestaurantsCardActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Restaurant;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;

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
    private float density;
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
            density = context.getResources().getDisplayMetrics().density;
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
        boolean seekBar = priceMin > 0;
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
                    Double pr = new Double(restaurant.get_minimal_price());
                    if(pr.intValue() > priceMin){
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
        View view = LayoutInflater.from(parent.getContext()).inflate(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.card_restaurant_layout,parent, false);
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
                    if(filterX.get(i).get_name().toUpperCase().contains(constraint)){
                        Restaurant restaurant = filterX.get(i);
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
            TextView timeTV, timeTitleTV;
            TextView workTimeTV, workTimeTitleTV;
            TextView deliveryType, deliveryTitleType;
            TextView likesTV;
            TextView dislikesTV;
            ImageView imageView;
            private final float TEXT_SIZE_14 = 14;


        public ViewHolder(View view){
                super(view);

                nameTV = (TextView) view.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_name);

                kitchenType = (TextView) view.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_kitchen_type);

                workTimeTitleTV = (TextView) view.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_work_time_title);
                workTimeTV = (TextView) view.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_work_time);

                timeTitleTV = (TextView) view.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_delivery_time_title);
                timeTV = (TextView) view.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_delivery_time);

                deliveryTitleType = (TextView) view.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_min_price_title);
                deliveryType = (TextView) view.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_min_price);

                imageView = (ImageView) view.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_icon);

                likesTV = (TextView) view.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_likes);
                dislikesTV = (TextView) view.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.restaurants_dislike);

                resizeText();

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Intent intent = new Intent(context, RestaurantActivity.class);
                        intent.putExtra(RestaurantsCardActivity.EXTRA_RESTAURANT, restaurants.get(position).get_slug());
                        context.startActivity(intent);
                    }
                });
            }

            private void resizeText(){
                if (density > 1.5){

                    timeTitleTV.setTextSize(TEXT_SIZE_14);
                    timeTV.setTextSize(TEXT_SIZE_14);

                    deliveryTitleType.setTextSize(TEXT_SIZE_14);
                    deliveryType.setTextSize(TEXT_SIZE_14);

                    workTimeTitleTV.setTextSize(TEXT_SIZE_14);
                    workTimeTV.setTextSize(TEXT_SIZE_14);

                    kitchenType.setTextSize(TEXT_SIZE_14);
                }
            }

            public void setRestaurant(Restaurant restaurant){
                nameTV.setText(restaurant.get_name());
                timeTV.setText(restaurant.get_delivery_time() + " мин.");
                String price = Validation.twoNumbersAfterAfterPoint(restaurant.get_minimal_price());
                deliveryType.setText(price + " руб.");
                kitchenType.setText(restaurant.get_kitchens());
                likesTV.setText(restaurant.get_comments().get("likes") + "");
                dislikesTV.setText(restaurant.get_comments().get("dislikes") + "");
                workTimeTV.setText(restaurant.get_working_time() + "");

                Picasso.with(context)
                        .load(restaurant.get_iconURL())
                        .placeholder(by.vkus.alexandrzanko.mobile_6vkusov.R.drawable.rest_icon) //показываем что-то, пока не загрузится указанная картинка
                        .error(by.vkus.alexandrzanko.mobile_6vkusov.R.drawable.rest_icon) // показываем что-то, если не удалось скачать картинку
                        .into(imageView);
            }

    }
}
