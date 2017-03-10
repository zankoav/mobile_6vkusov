package com.example.alexandrzanko.mobile_6vkusov.Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexandrzanko on 3/9/17.
 */

public class Restaurant {

    private String url;
    private String baseUrl;
    private JSONObject json;
    private final String TAG = this.getClass().getSimpleName();


    public Restaurant(String url, JSONObject json) {

        this.baseUrl = url;
        this.json = json;
        try {
            this.url = this.baseUrl + this.json.getString("logo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isNew(){
        return true;
    }

    public boolean isFreeFood(){
        return true;
    }

    public boolean isFlash(){
        return true;
    }

    public boolean isSale(){
        return false;
    }

    public String getSlug() {
        try {
            return json.getString("slug");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getName() {
        try {
            return json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getWorkingTime() {
        try {
            return json.getString("working_time");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Double getMinimalPrice() {
        try {
            return json.getDouble("minimal_price");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDeliveryTime() {
        try {
            return json.getString("delivery_time");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getKitchens() {
        String kitchens = "";
        try {
            JSONArray kitchensJson = json.getJSONArray("kitchens");
            for(int j = 0; j < kitchensJson.length(); j++){
                kitchens += kitchensJson.getString(j);
                if(j != kitchensJson.length() - 1){
                    kitchens += ", ";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return kitchens;
    }

    public String getDescription() {
        try {
            return json.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getLikes() {
        try {
            return json.getJSONObject("comments").getInt("like");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getDislikes() {
        try {
            int total = json.getJSONObject("comments").getInt("total");
            int likes = json.getJSONObject("comments").getInt("like");
            int dislikes = total - likes;
            return dislikes;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getUrl() {
        return url;
    }

    public JSONObject getJson(){
        return json;
    }

    public String getBaseUrl(){
        return baseUrl;
    }

    private String join(String join, String... strings) {
        if (strings == null || strings.length == 0) {
            return "";
        } else if (strings.length == 1) {
            return strings[0];
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(strings[0]);
            for (int i = 1; i < strings.length; i++) {
                sb.append(join).append(strings[i]);
            }
            return sb.toString();
        }
    }
}
