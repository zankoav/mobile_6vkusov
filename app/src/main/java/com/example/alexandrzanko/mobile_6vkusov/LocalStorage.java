package com.example.alexandrzanko.mobile_6vkusov;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.alexandrzanko.mobile_6vkusov.Activities.MainActivity;
import com.example.alexandrzanko.mobile_6vkusov.Models.Category;
import com.example.alexandrzanko.mobile_6vkusov.Models.InfoRestaurant;
import com.example.alexandrzanko.mobile_6vkusov.Models.Product;
import com.example.alexandrzanko.mobile_6vkusov.Models.Restaurant;
import com.example.alexandrzanko.mobile_6vkusov.Users.General;
import com.example.alexandrzanko.mobile_6vkusov.Users.Register;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexandrzanko on 3/1/17.
 */

public class LocalStorage implements LoadJson{

    private final String TAG = this.getClass().getSimpleName();

    private final String APP_STORE = "store";

    public ArrayList<Product> currentProducts = new ArrayList<Product>();


    public final String APP_CATEGORIES = "categories";
    public final String APP_RESTAURANTS = "restaurants";
    public final String APP_PROFILE = "profile";

    private SharedPreferences store;

    public Context getContext() {
        return context;
    }

    private Context context;

    public LocalStorage(Context context){
        this.store = context.getSharedPreferences(APP_STORE, Context.MODE_PRIVATE);
        this.context = context;
        Singleton.currentState().setUser(null);
        clearKeyStorage(APP_CATEGORIES);
        clearKeyStorage(APP_RESTAURANTS);


        JSONObject paramsCategories = new JSONObject();
        String urlCat = context.getResources().getString(R.string.api_categories);
        new JsonHelperLoad(urlCat, paramsCategories, this, APP_CATEGORIES).execute();

        JSONObject paramsRestaurants = new JSONObject();
        String urlRest = context.getResources().getString(R.string.api_restaurants);
        new JsonHelperLoad(urlRest, paramsRestaurants, this, APP_RESTAURANTS).execute();

        String userProfile = getStringValueStorage(APP_PROFILE);

        if(userProfile != null){
            JSONObject params = new JSONObject();
            try {
                JSONObject userProfileJson = new JSONObject(userProfile);
                String session = userProfileJson.getString("session");
                params.put("session", session);
                String url = context.getResources().getString(R.string.api_user);
                new JsonHelperLoad(url, params, this, APP_PROFILE).execute();
            } catch (JSONException e) {
                Singleton.currentState().setUser(new General());
                Log.i(TAG,"initial General User Exception");
                e.printStackTrace();
            }
        }else{
            Singleton.currentState().setUser(new General());
        }
    }

    public String getStringValueStorage(String key) {
        if(this.store.contains(key)) {
            return this.store.getString(key, "");
        }else{
            return null;
        }
    }

    public void setStringValueStorage(String key, String value){
        SharedPreferences.Editor editor = this.store.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void clearKeyStorage(String key) {
        SharedPreferences.Editor editor = this.store.edit();
        editor.remove(key);
        editor.apply();
    }


    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        if(obj != null){
            setStringValueStorage(sessionName, obj.toString());
            Log.i(TAG,obj.toString());
            if (sessionName == APP_PROFILE) {
                JSONObject user = null;
                try {
                    user = obj.getJSONObject("user");
                    setStringValueStorage(sessionName, user.toString());
                    int points = user.getInt("points");
                    Singleton.currentState().setUser(new Register(points));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(Singleton.currentState().getUser() != null &&
               getStringValueStorage(APP_CATEGORIES) != null &&
               getStringValueStorage(APP_RESTAURANTS) != null)
            {
                Log.i(TAG, "loadComplete: ");
                ((MainActivity)context).loadComplete();
            }
        }else{
            Log.i(TAG,"Error connection");
        }
    }


    public ArrayList<Category> getMainCategories(){
        ArrayList<Category> categories = new ArrayList<>();
        String catsSTR = getStringValueStorage(APP_CATEGORIES);
        JSONObject categoriesHash = null;
        try {
            JSONObject hash = new JSONObject(catsSTR);
            categoriesHash = hash.getJSONObject("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String img_path = categoriesHash.getString("img_path");
            JSONArray cats = categoriesHash.getJSONArray("categories");
            for (int i=0; i<cats.length(); i++) {
                int type = cats.getJSONObject(i).getInt("type");
                String name = cats.getJSONObject(i).getString("name");
                String slug = cats.getJSONObject(i).getString("slug");
                if (type == 1) {
                    String image = cats.getJSONObject(i).getString("image");
                    String url = this.context.getResources().getString(R.string.api_base) + img_path + "/" + image;
                    categories.add(new Category(name, slug, url, type));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public ArrayList<Category> getSecondaryCategories(){
        ArrayList<Category> categories = new ArrayList<>();
        String catsSTR = getStringValueStorage(APP_CATEGORIES);
        JSONObject categoriesHash = null;
        try {
            JSONObject hash = new JSONObject(catsSTR);
            categoriesHash = hash.getJSONObject("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray cats = categoriesHash.getJSONArray("categories");
            for (int i=0; i<cats.length(); i++) {
                int type = cats.getJSONObject(i).getInt("type");
                String name = cats.getJSONObject(i).getString("name");
                String slug = cats.getJSONObject(i).getString("slug");
                if (type == 2) {
                    categories.add(new Category(name, slug, null, type));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categories;
    }


    public ArrayList<Restaurant> getRestaurants(String slug){
        if (slug.equals("all")){
            return getAllRestaurants();
        }else{
            return getRestaurantsBySlug(slug);
        }
    }

    public Restaurant getRestaurantBySlug(String slug){
        ArrayList<Restaurant> restaurants = getAllRestaurants();
        for (int i=0; i< restaurants.size(); i++) {
            if (restaurants.get(i).get_slug().equals(slug)){
                return restaurants.get(i);
            }
        }
        return null;
    }

    private ArrayList<Restaurant> getAllRestaurants(){
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        String restSTR = getStringValueStorage(APP_RESTAURANTS);
        JSONObject restaurantsHash = null;
        try {
            restaurantsHash = new JSONObject(restSTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (restaurantsHash.get("restaurants") != null ) {
                restaurants = new ArrayList<>();
                JSONArray rests = restaurantsHash.getJSONArray("restaurants");
                String urlImgPath = context.getResources().getString(R.string.api_base) +  restaurantsHash.getString("img_path") + "/";
                for (int i = 0; i < rests.length(); i++) {

                    JSONObject rest = rests.getJSONObject(i);
                    String slug = rest.getString("slug");
                    String name = rest.getString("name");
                    String logo = rest.getString("logo");
                    String working_time = "";
                    double minimal_price = 0;
                    String delivery_time = "";
                    try {
                        working_time = rest.getString("working_time");
                        minimal_price = rest.getDouble("minimal_price");
                        delivery_time = rest.getString("delivery_time");
                    }catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(TAG, "for: error = " + e);
                    }
                    JSONArray kitchensJson = rest.getJSONArray("categories_slugs");
                    ArrayList<String> categories_slugs = new ArrayList<>();
                    for(int j = 0; j < kitchensJson.length(); j++){
                        categories_slugs.add(kitchensJson.getString(j));
                    }

                    JSONObject commentsJson = rest.getJSONObject("comments");
                    HashMap<String, Integer> comments = new HashMap<>();
                    comments.put("likes",commentsJson.getInt("likes"));
                    comments.put("dislikes",commentsJson.getInt("dislikes"));

                    JSONObject about = null;
                    String kitchens = null;
                    JSONObject infoJson = null;
                    String descriptionInfo = null;
                    String addressInfo = null;
                    String nameInfo = null;
                    String unpInfo = null;
                    String deliveryDescriptionInfo = null;
                    String commercialRegisterInfo = null;

                    try {
                        about = rest.getJSONObject("about");
                        kitchens = about.getString("kitchens");
                        infoJson = about.getJSONObject("info");
                        descriptionInfo = infoJson.getString("description");
                        addressInfo = infoJson.getString("address");
                        nameInfo = infoJson.getString("name");
                        unpInfo = infoJson.getString("unp");
                        deliveryDescriptionInfo = infoJson.getString("delivery_description");
                        commercialRegisterInfo = infoJson.getString("commercial_register");
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.i(TAG, "getAllRestaurants: for" + e);
                    }


                    InfoRestaurant info = new InfoRestaurant(descriptionInfo, addressInfo, nameInfo, unpInfo, deliveryDescriptionInfo, commercialRegisterInfo);

                    Restaurant restaurant = new Restaurant(slug, name, working_time, minimal_price, delivery_time, kitchens, info, urlImgPath+logo, comments, categories_slugs);
                    restaurants.add(restaurant);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "getAllRestaurants: error = " + e);
        }
        return restaurants;
    }

    private ArrayList<Restaurant> getRestaurantsBySlug(String slug) {

        ArrayList<Restaurant> restaurants = new ArrayList<>();
        ArrayList<Restaurant> allRestaurants = this.getAllRestaurants();

        for(int i=0; i < allRestaurants.size(); i++) {
            ArrayList<String> categories_slugs = allRestaurants.get(i).get_categoriesSlugs();
            if (categories_slugs.contains(slug)){
                restaurants.add(allRestaurants.get(i));
            }
        }

        return restaurants;
    }
}
