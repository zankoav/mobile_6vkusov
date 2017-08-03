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

}