package by.vkus.alexandrzanko.mobile_6vkusov.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MRestaurantDeliveryInfo {

    @SerializedName("minimalPrice")
    @Expose
    private double minimalPrice;
    @SerializedName("deliveryPrice")
    @Expose
    private double deliveryPrice;

    public double getMinimalPrice() {
        return minimalPrice;
    }

    public void setMinimalPrice(double minimalPrice) {
        this.minimalPrice = minimalPrice;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

}