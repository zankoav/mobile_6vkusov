package by.vkus.alexandrzanko.mobile_6vkusov.Interfaces;

import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;

/**
 * Created by alexandrzanko on 8/2/17.
 */

public interface IUser {
    STATUS getStatus();
    void setCurrentOrderRestaurantSlug(String currentOrderRestaurantSlug);
    String getCurrentOrderRestaurantSlug();
    String getFirstName();
    String getLastName();
    String getEmail();
    Integer getPoints();
    String getSession();
    Integer getPhoneCode();
    Integer getPhoneNumber();
    String getAvatar();
    void setPhone_number(Integer phone_number);
    void setAvatar(String avatar);
    void setPhone_code(Integer phone_code);
    void setSession(String session);
    void setPoints(Integer points);
    void setEmail(String email);
    void setLast_name(String last_name);
    void setFirst_name(String first_name);
}
