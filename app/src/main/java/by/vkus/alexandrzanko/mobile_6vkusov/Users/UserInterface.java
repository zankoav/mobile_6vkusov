package by.vkus.alexandrzanko.mobile_6vkusov.Users;

import org.json.JSONObject;

/**
 * Created by alexandrzanko on 3/1/17.
 */

public interface UserInterface {
    STATUS getStatus();
    JSONObject getProfile();
    Basket getBasket();
    int getPoints();
    void setPoints(int points);
}
