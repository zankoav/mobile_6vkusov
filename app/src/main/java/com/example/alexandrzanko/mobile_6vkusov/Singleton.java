package com.example.alexandrzanko.mobile_6vkusov;

import android.content.Context;

import com.example.alexandrzanko.mobile_6vkusov.Users.UserInterface;

/**
 * Created by alexandrzanko on 3/1/17.
 */

public class Singleton {

    private final String TAG = this.getClass().getSimpleName();

    private static Singleton instance;
    private LocalStorage store;
    private UserInterface user;

    private Singleton(){}

    public static Singleton currentState(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    public void initStore(Context context) {
        store = new LocalStorage(context);
    }

    public UserInterface getUser() {
        return user;
    }

    public void setUser(UserInterface user) {
        this.user = user;
    }

    public LocalStorage getStore() {
        return store;
    }

}
