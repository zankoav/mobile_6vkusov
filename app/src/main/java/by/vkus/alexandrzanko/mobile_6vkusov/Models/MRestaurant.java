package by.vkus.alexandrzanko.mobile_6vkusov.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;

public class MRestaurant {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("working_time")
    @Expose
    private String working_time;
    @SerializedName("minimal_price")
    @Expose
    private float minimal_price;
    @SerializedName("delivery_time")
    @Expose
    private String delivery_time;
    @SerializedName("kitchens")
    @Expose
    private String kitchens;
    @SerializedName("likes")
    @Expose
    private int likes;
    @SerializedName("dislikes")
    @Expose
    private int dislikes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getLogo() {
        return  ApiController.BASE_URL + Singleton.currentState().getSettingsApp().getImage_path().getRestaurant() + logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWorking_time() {
        return working_time;
    }

    public void setWorking_time(String working_time) {
        this.working_time = working_time;
    }

    public float getMinimal_price() {
        return minimal_price;
    }

    public void setMinimal_price(float minimal_price) {
        this.minimal_price = minimal_price;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getKitchens() {
        return kitchens;
    }

    public void setKitchens(String kitchens) {
        this.kitchens = kitchens;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

}