package com.example.alexandrzanko.mobile_6vkusov.Users;

import org.json.JSONObject;

/**
 * Created by alexandrzanko on 3/1/17.
 */

public class General implements UserInterface {

    private Basket basket;

    public General(){
        this.basket = new Basket();
    }

    @Override
    public STATUS getStatus() {
        return STATUS.GENERAL;
    }

    @Override
    public JSONObject getProfile() {
        return null;
    }

    @Override
    public Basket getBasket() {
        return basket;
    }
}
