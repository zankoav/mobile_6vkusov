package by.vkus.alexandrzanko.mobile_6vkusov.Models;

/**
 * Created by alexandrzanko on 5/2/17.
 */

public class FreeFood {

    private String restaurant_slug;
    private String name;
    private String description;
    private String urlImg;
    private int points;

    public FreeFood(String name, String restaurant_slug, String description, String urlImg, int points) {
        this.restaurant_slug = restaurant_slug;
        this.name = name;
        this.description = description;
        this.urlImg = urlImg;
        this.points = points;

    }

    public String getDescription() {
        return description;
    }

    public int getPoints() {return points;}

    public String getRestaurant_slug() {return restaurant_slug;}

    public String getUrlImg() {
        return urlImg;
    }

    public String getName() {
        return name;
    }
}
