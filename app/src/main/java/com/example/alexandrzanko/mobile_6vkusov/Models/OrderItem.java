package com.example.alexandrzanko.mobile_6vkusov.Models;

import java.util.ArrayList;

/**
 * Created by alexandrzanko on 5/2/17.
 */

public class OrderItem {

    private ArrayList<OrderItemFood> foods;
    private Restaurant restaurant;


    public Restaurant getRestaurant() {
        return restaurant;
    }

    public ArrayList<OrderItemFood> getFoods() {
        return foods;
    }
}
