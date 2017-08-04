package by.vkus.alexandrzanko.mobile_6vkusov.Models;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandrzanko on 8/2/17.
 */

public class BasketUser {

    private List<OrderItem> orderItems;

    public BasketUser(){
        orderItems = new ArrayList<>();
    }

    public int getCountItems(){
        return orderItems.size();
    }

}
