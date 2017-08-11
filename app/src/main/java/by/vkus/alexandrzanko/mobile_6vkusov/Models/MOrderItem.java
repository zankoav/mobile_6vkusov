package by.vkus.alexandrzanko.mobile_6vkusov.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;

public class MOrderItem {

    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("idVariant")
    @Expose
    private int idVariant;
    @SerializedName("idOrderItem")
    @Expose
    private Integer idOrderItem;
    @SerializedName("idOrder")
    @Expose
    private Integer idOrder;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("solidByPoints")
    @Expose
    private boolean solidByPoints;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("icon")
    @Expose
    private String icon;

    public int getIdVariant() {
        return idVariant;
    }

    public void setIdVariant(int idVariant) {
        this.idVariant = idVariant;
    }

    public Integer getIdOrderItem() {
        return idOrderItem;
    }

    public void setIdOrderItem(Integer idOrderItem) {
        this.idOrderItem = idOrderItem;
    }

    public Integer getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Integer idOrder) {
        this.idOrder = idOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSolidByPoints() {
        return solidByPoints;
    }

    public void setSolidByPoints(boolean solidByPoints) {
        this.solidByPoints = solidByPoints;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getIcon() {
        return ApiController.BASE_URL + SingletonV2.currentState().getSettingsApp().getImage_path().getFood() + icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}