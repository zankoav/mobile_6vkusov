package by.vkus.alexandrzanko.mobile_6vkusov.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;

public class UserRegister implements IUser{

    @SerializedName("first_name")
    @Expose
    private String first_name;
    @SerializedName("last_name")
    @Expose
    private String last_name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("session")
    @Expose
    private String session;
    @SerializedName("phone_code")
    @Expose
    private Integer phone_code;
    @SerializedName("phone_number")
    @Expose
    private Integer phone_number;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    private BasketUser basket;

    @Override
    public STATUS getStatus() {
        return STATUS.REGISTER;
    }

    @Override
    public BasketUser getBasket() {
        return null;
    }

    @Override
    public String getFirstName() {
        return first_name;
    }

    @Override
    public String getLastName() {
        return last_name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Integer getPoints() {
        return points;
    }

    @Override
    public String getSession() {
        return session;
    }

    @Override
    public Integer getPhoneCode() {
        return phone_code;
    }

    @Override
    public Integer getPhoneNumber() {
        return phone_number;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    @Override
    public void setBasketUser(BasketUser basket) {
        this.basket = basket;
    }

    public void setPhone_code(Integer phone_code) {
        this.phone_code = phone_code;
    }

    public void setPhone_number(Integer phone_number) {
        this.phone_number = phone_number;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

}
