package by.vkus.alexandrzanko.mobile_6vkusov.Models;

/**
 * Created by alexandrzanko on 5/3/17.
 */

public class OrderItemFood {

    private int count;
    private String name;

    public OrderItemFood(int count, String name) {
        this.count = count;
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }
}
