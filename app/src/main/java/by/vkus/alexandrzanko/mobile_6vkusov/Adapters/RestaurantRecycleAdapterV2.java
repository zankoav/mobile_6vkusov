package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import java.util.List;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantActivityV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantsActivityV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MRestaurant;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;

/**
 * Created by alexandrzanko on 8/9/17.
 */

public class RestaurantRecycleAdapterV2 extends RecyclerView.Adapter<RestaurantRecycleAdapterV2.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();
    List<MRestaurant> restaurants;
    private float density;
    Context context;

    public RestaurantRecycleAdapterV2(List<MRestaurant> restaurants, Context context){
        this.restaurants = restaurants;
        this.context = context;
        density = context.getResources().getDisplayMetrics().density;
    }

    @Override
    public RestaurantRecycleAdapterV2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_restaurant_layout, parent, false);
        return new RestaurantRecycleAdapterV2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantRecycleAdapterV2.ViewHolder holder, int position) {
        holder.setRestaurant(restaurants.get(position));
    }

    @Override
    public int getItemCount() {
        return this.restaurants.size();
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

            nameTV = (TextView) view.findViewById(R.id.restaurants_name);

            kitchenType = (TextView) view.findViewById(R.id.restaurants_kitchen_type);

            workTimeTitleTV = (TextView) view.findViewById(R.id.restaurants_work_time_title);
            workTimeTV = (TextView) view.findViewById(R.id.restaurants_work_time);

            timeTitleTV = (TextView) view.findViewById(R.id.restaurants_delivery_time_title);
            timeTV = (TextView) view.findViewById(R.id.restaurants_delivery_time);

            deliveryTitleType = (TextView) view.findViewById(R.id.restaurants_min_price_title);
            deliveryType = (TextView) view.findViewById(R.id.restaurants_min_price);

            imageView = (ImageView) view.findViewById(R.id.restaurants_icon);

            likesTV = (TextView) view.findViewById(R.id.restaurants_likes);
            dislikesTV = (TextView) view.findViewById(R.id.restaurants_dislike);

            resizeText();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, RestaurantActivityV2.class);
                    intent.putExtra(RestaurantsActivityV2.EXTRA_RESTAURANT, restaurants.get(position).getSlug());
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

        public void setRestaurant(MRestaurant restaurant){
            nameTV.setText(restaurant.getName());
            timeTV.setText(restaurant.getDelivery_time());
            String price = Validation.twoNumbersAfterAfterPoint(restaurant.getMinimal_price());
            deliveryType.setText(price + " руб.");
            kitchenType.setText(restaurant.getKitchens());
            likesTV.setText(restaurant.getLikes() + "");
            dislikesTV.setText(restaurant.getDislikes() + "");
            workTimeTV.setText(restaurant.getWorking_time() + "");


            Glide.with(context)
                    .load(restaurant.getLogo())
                    .into(imageView);
//            Picasso.with(context)
//                    .load(restaurant.getLogo())
//                    .placeholder(by.vkus.alexandrzanko.mobile_6vkusov.R.drawable.rest_icon) //показываем что-то, пока не загрузится указанная картинка
//                    .error(by.vkus.alexandrzanko.mobile_6vkusov.R.drawable.rest_icon) // показываем что-то, если не удалось скачать картинку
//                    .into(imageView);
        }

    }
}
