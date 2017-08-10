package by.vkus.alexandrzanko.mobile_6vkusov.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;

public class MProduct {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("variants")
    @Expose
    private List<MVariant> variants = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return ApiController.BASE_URL + SingletonV2.currentState().getSettingsApp().getImage_path().getFood() + icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<MVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<MVariant> variants) {
        this.variants = variants;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}