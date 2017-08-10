package by.vkus.alexandrzanko.mobile_6vkusov.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;

public class FoodByPoint {

    @SerializedName("id")
    @Expose
    private int id;
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
    @SerializedName("restaurant_name")
    @Expose
    private String restaurant_name;

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
        return ApiController.BASE_URL + SingletonV2.currentState().getSettingsApp().getImage_path().getFood() + icon;
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

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}