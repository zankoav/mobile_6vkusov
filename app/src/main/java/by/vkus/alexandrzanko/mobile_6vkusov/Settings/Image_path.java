package by.vkus.alexandrzanko.mobile_6vkusov.Settings;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image_path {

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("food")
    @Expose
    private String food;

    @SerializedName("comment")
    @Expose
    private String comment;

    @SerializedName("restaurant")
    @Expose
    private String restaurant;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}