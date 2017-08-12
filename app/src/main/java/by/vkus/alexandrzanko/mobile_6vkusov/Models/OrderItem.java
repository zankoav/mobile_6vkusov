package by.vkus.alexandrzanko.mobile_6vkusov.Models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alexandrzanko on 5/2/17.
 */

public class OrderItem {

    private ArrayList<OrderItemFood> foods;
    private Restaurant restaurant;

    public void setComment_exists(boolean comment_exists) {
        this.comment_exists = comment_exists;
    }

    private boolean comment_exists;
    private double total_price;
    private int id;
    private int created;

    public OrderItem(ArrayList<OrderItemFood> foods, String slug, boolean comment_exists, double total_price, int id, int created) {
        this.foods = foods;
        this.comment_exists = comment_exists;
        this.total_price = total_price;
        this.id = id;
        this.created = created;
    }

    public boolean isComment_exists() {
        return comment_exists;
    }

    public double getTotal_price() {
        return total_price;
    }

    public int getId() {
        return id;
    }

    public String getCreated() {
        Date date = new Date((long)created*1000);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public ArrayList<OrderItemFood> getFoods() {
        return foods;
    }

}
