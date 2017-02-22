package com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader;

import org.json.JSONObject;

/**
 * Created by alexandrzanko on 2/22/17.
 */

public interface LoadJson {
    void loadComplete(JSONObject obj, String sessionName);
}

