package com.example.alexandrzanko.mobile_6vkusov.Users;

import org.json.JSONObject;

/**
 * Created by alexandrzanko on 3/1/17.
 */

public interface UserInterface {
    public STATUS getStatus();
    public JSONObject getProfile();
    public Basket getBasket();
}
