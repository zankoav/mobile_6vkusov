package com.example.alexandrzanko.mobile_6vkusov.Models;

import android.graphics.Bitmap;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alexandrzanko on 30/11/16.
 */

public class Product {

    private Bitmap img;
    private JSONObject product;
    private String baseUrl;

    public Product(JSONObject obj, String baseImageUrl){
        this.baseUrl = baseImageUrl;
        this.product = obj;
        try{
            String url = this.baseUrl + this.product.getString("image");
//            new BitmapHelperLoad(url,this).execute();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Integer getId() {
        try {
            return product.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getName() {
        try {
            return product.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getImageUrl() {
        try {
            return baseUrl + product.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCategory() {
        try {
            return product.getString("category");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDescription() {
        try {
            return product.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public ArrayList<Variant> getVariants(){
        ArrayList<Variant> variants = new ArrayList<>();
        try {
            JSONArray variantsJson = product.getJSONArray("variants");
            for(int i= 0; i < variantsJson.length(); i++){
                Double price = variantsJson.getJSONObject(i).getDouble("price");
                String size = variantsJson.getJSONObject(i).getString("size");
                String weight = variantsJson.getJSONObject(i).getString("weigth");
                int id = variantsJson.getJSONObject(i).getInt("id");
                variants.add(new Variant(price, size, weight, id));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return variants;
    }

    public Bitmap getImg() {
        return img;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product){
            Product product = (Product)obj;
            return  this.getId().equals(product.getId());
        }else{
            return false;
        }
    }
}