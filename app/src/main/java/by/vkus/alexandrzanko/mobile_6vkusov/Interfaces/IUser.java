package by.vkus.alexandrzanko.mobile_6vkusov.Interfaces;

import by.vkus.alexandrzanko.mobile_6vkusov.Models.BasketUser;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;

/**
 * Created by alexandrzanko on 8/2/17.
 */

public interface IUser {
    STATUS getStatus();
    BasketUser getBasket();

    String getFirstName();
    String getLastName();
    String getEmail();
    Integer getPoints();
    String getSession();
    Integer getPhoneCode();
    Integer getPhoneNumber();
    String getAvatar();
    void setBasketUser(BasketUser basket);
}
