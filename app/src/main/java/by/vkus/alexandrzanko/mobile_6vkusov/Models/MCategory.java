package by.vkus.alexandrzanko.mobile_6vkusov.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;

/**
 * Created by alexandrzanko on 7/28/17.
 */

public class MCategory {


    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("image")
    @Expose
    private String image;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImage() {
        return ApiController.BASE_URL + SingletonV2.currentState().getSettingsApp().getImage_path().getCategory() + image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
