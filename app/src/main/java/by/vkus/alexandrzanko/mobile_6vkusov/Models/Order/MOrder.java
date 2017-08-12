package by.vkus.alexandrzanko.mobile_6vkusov.Models.Order;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;

public class MOrder {

    @SerializedName("restaurant_name")
    @Expose
    private String restaurant_name;
    @SerializedName("restaurant_slug")
    @Expose
    private String restaurant_slug;
    @SerializedName("restaurant_icon")
    @Expose
    private String restaurant_icon;
    @SerializedName("total_price")
    @Expose
    private int total_price;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("created")
    @Expose
    private int created;
    @SerializedName("comment_exists")
    @Expose
    private boolean comment_exists;
    @SerializedName("food")
    @Expose
    private List<MOrderFood> food = null;

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getRestaurant_slug() {
        return restaurant_slug;
    }

    public void setRestaurant_slug(String restaurant_slug) {
        this.restaurant_slug = restaurant_slug;
    }

    public String getRestaurant_icon() {
        return  ApiController.BASE_URL + SingletonV2.currentState().getSettingsApp().getImage_path().getRestaurant() + restaurant_icon;
    }

    public void setRestaurant_icon(String restaurant_icon) {
        this.restaurant_icon = restaurant_icon;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public boolean isComment_exists() {
        return comment_exists;
    }

    public void setComment_exists(boolean comment_exists) {
        this.comment_exists = comment_exists;
    }

    public List<MOrderFood> getFood() {
        return food;
    }

    public void setFood(List<MOrderFood> food) {
        this.food = food;
    }

}