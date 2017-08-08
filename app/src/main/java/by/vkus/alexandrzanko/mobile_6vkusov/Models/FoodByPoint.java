package by.vkus.alexandrzanko.mobile_6vkusov.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;

public class FoodByPoint {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("points")
    @Expose
    private int points;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("restaurant_slug")
    @Expose
    private String restaurant_slug;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getIcon() {
        return ApiController.BASE_URL + Singleton.currentState().getSettingsApp().getImage_path().getFood() + icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRestaurant_slug() {
        return restaurant_slug;
    }

    public void setRestaurant_slug(String restaurant_slug) {
        this.restaurant_slug = restaurant_slug;
    }

}