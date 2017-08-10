package by.vkus.alexandrzanko.mobile_6vkusov.Models;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandrzanko on 8/2/17.
 */

public class BasketUser {

    private List<OrderItem> orderItems;

    private String restaurantSlug;

    public BasketUser(){
        orderItems = new ArrayList<>();
    }

    public int getCountItems(){
        return orderItems.size();
    }

    public String getRestaurantSlug() {
        return restaurantSlug;
    }

    public void setRestaurantSlug(String restaurantSlug) {
        this.restaurantSlug = restaurantSlug;
    }
}
