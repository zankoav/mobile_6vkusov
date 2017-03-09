package com.example.alexandrzanko.mobile_6vkusov.Models;

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

    public Restaurant(String url, JSONObject json) {

        this.baseUrl = url;
        this.json = json;
        try {
            this.url = this.baseUrl + this.json.getString("logo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public String[] getKitchens() {
        try {
            JSONArray kitchensJson = json.getJSONArray("kitchens");
            String[] kitchens = new String[kitchensJson.length()];
            for(int j = 0; j < kitchensJson.length(); j++){
                kitchens[j] = (kitchensJson.getJSONObject(j)).toString();
                System.out.println(kitchens[j]);
            }
            return kitchens;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
}
