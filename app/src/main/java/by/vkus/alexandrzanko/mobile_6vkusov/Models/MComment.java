package by.vkus.alexandrzanko.mobile_6vkusov.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;

public class MComment {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("date_time")
    @Expose
    private long date_time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAvatar() {
        return ApiController.BASE_URL + SingletonV2.currentState().getSettingsApp().getImage_path().getComment() +  avatar;
    }

    public void setAvatar(String avatarFile) {
        this.avatar = avatarFile;
    }

    public String getDate_time() {
        long time = date_time * 1000;
        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    public void setDate_time(long date_time) {
        this.date_time = date_time;
    }

}