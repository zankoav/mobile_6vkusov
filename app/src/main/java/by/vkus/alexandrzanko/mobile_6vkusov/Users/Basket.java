package by.vkus.alexandrzanko.mobile_6vkusov.Users;

import android.util.Log;

import by.vkus.alexandrzanko.mobile_6vkusov.Models.Product;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.ProductItem;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Restaurant;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Variant;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexandrzanko on 3/1/17.
 */

public class Basket implements LoadJson {

    private final String TAG = this.getClass().getSimpleName();
    private BasketViewInterface delegateContext;

    private ArrayList<ProductItem> productItems;

    public String getSlugRestaurant() {
        return slugRestaurant;
    }

    private String slugRestaurant;

    public void setFreeFoodExist(boolean freeFoodExist) {
        isFreeFoodExist = freeFoodExist;
    }

    private boolean isFreeFoodExist;

    public boolean isFreeFoodExist() {
        return isFreeFoodExist;
    }

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        if (obj != null){
            String status = null;
            try {
                status = obj.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (status.equals("successful")){
                if (sessionName.equals("add")){
                    try {
                        boolean empty = obj.getBoolean("empty");
                        if (!empty){
                            this.productItems = new ArrayList<>();
                            slugRestaurant = obj.getString("slug");
                            String url = Singleton.currentState().getStore().getContext().getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.api_base);
                            String img_path = obj.getString("img_path") + "/";
                            JSONArray basket = obj.getJSONArray("basket");
                            boolean freeFoodContains = false;
                            for (int i = 0; i < basket.length(); i++) {
                                JSONObject item = basket.getJSONObject(i);
                                int idProduct = item.getInt("id");
                                String nameProduct = item.getString("name");
                                String iconProduct = item.getString("icon");
                                String iconDescription = item.getString("description");
                                int count = item.getInt("count");

                                int points;
                                String pointsStr = item.getString("points");
                                if (!pointsStr.equals("null")){
                                    points = Integer.parseInt(pointsStr);
                                    freeFoodContains = true;
                                }else{
                                    points = -1;
                                }

                                JSONObject categoryData = item.getJSONObject("category");
                                HashMap<String, String> category = new HashMap<>();
                                category.put("name", categoryData.getString("name"));
                                category.put("slug", categoryData.getString("slug"));



                                JSONObject variantData = item.getJSONObject("variant");
                                int variantId = variantData.getInt("id");
                                double variantPrice = variantData.getDouble("price");
                                String variantSize = variantData.getString("size");
                                String variantWeight = variantData.getString("weight");

                                Variant variant = new Variant(variantId, variantPrice, variantSize, variantWeight, 0);

                                ProductItem productItem = new ProductItem(idProduct, nameProduct, url + img_path + iconProduct,iconDescription, points, category, variant);
                                productItem.set_count(count);
                                productItems.add(productItem);
                                isFreeFoodExist = freeFoodContains;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i(TAG, "loadComplete: error = " + e);
                    }
                }else if (sessionName.equals("removeItem")){
                    Log.i(TAG, "loadComplete: sessionName = " + sessionName + " obj = " +  obj.toString());
                }else if (sessionName.equals("minusItem")){
                    Log.i(TAG, "loadComplete: sessionName = " + sessionName + " obj = " +  obj.toString());
                }
                else if (sessionName.equals("addProduct")){
                    Log.i(TAG, "loadComplete: sessionName = " + sessionName + " obj = " +  obj.toString());
                }
            }
        }

        if (delegateContext != null){
            delegateContext.updateBasket(0);
        }
    }

    public void setDelegateContext(BasketViewInterface delegateContext) {
        this.delegateContext = delegateContext;
    }

    public BasketViewInterface getDelegateContext() {
        return delegateContext;
    }

    public Basket() {
        this.isFreeFoodExist = false;
        this.productItems = new ArrayList<>();
    }

    public void initBasketFromRegisterUser(){
        if (Singleton.currentState().getUser().getStatus() == STATUS.REGISTER){
            String url = Singleton.currentState().getStore().getContext().getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.api_basket_items);
            JSONObject params = new JSONObject();
            try {
                JSONObject userProfileJson = Singleton.currentState().getUser().getProfile();
                String session = userProfileJson.getString("session");
                params.put("session", session);
                new JsonHelperLoad( url, params, this, "add").execute();
            } catch (JSONException e) {
                Log.i(TAG,"initial Basket error");
                e.printStackTrace();
            }
        }
    }

    public int getTotalCount(){
        int count = 0;
        if (productItems != null){
            for(int i=0; i < productItems.size(); i++ ){
                count += productItems.get(i).get_count();
            }
        }
        return count;
    }

    public ArrayList<ProductItem> getProductItems() {
        return productItems;
    }

    public void setProductItems(ArrayList<ProductItem> productItems) {
        this.productItems = productItems;
    }


    private void addProduct(Product product){
        Log.i(TAG, "addProduct: " + product.get_points());
        boolean points = product.get_points() > 0;
        if (points){
            if (isFreeFoodExist) {
                return;
            }else{
                isFreeFoodExist = true;
            }
        }
        Log.i(TAG, "addProduct: points = " + points);

        String url = Singleton.currentState().getStore().getContext().getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.api_add_variants);
        JSONObject params = new JSONObject();
        try {
            JSONArray variants = new JSONArray();
            for (int i = 0; i < product.get_variants().size(); i++) {
                Variant v = product.get_variants().get(i);
                if (v.get_count() == 0){
                    continue;
                }
                JSONObject variant = new JSONObject();
                variant.put("id", v.get_id());
                variant.put("count", v.get_count());
                variants.put(variant);
            }
            JSONObject userProfileJson = Singleton.currentState().getUser().getProfile();
            String session = userProfileJson.getString("session");
            params.put("session", session);
            params.put("variants", variants);
            params.put("slug", slugRestaurant);
            params.put("points", points);

            new JsonHelperLoad( url, params, this, "addProduct").execute();
        } catch (JSONException e) {
            Log.i(TAG,"addProductItemRegister error");
            e.printStackTrace();
        }
        initBasketFromRegisterUser();
    }


    public void addProductItemRegister(Product product, String slug){
        Log.i(TAG, "addProductItemRegister: slug = " + slug );
        if (this.productItems.size() == 0){
            slugRestaurant = slug;
            addProduct(product);
        }else{
            if (slugRestaurant.equals(slug)) {
                addProduct(product);
            }else{
                if (delegateContext != null){
                    delegateContext.showAlertNewOrder(product, slug);
                }
            }
        }
    }

    public void addProductItemOneRegister(ProductItem item){
        if(Singleton.currentState().getUser().getStatus() == STATUS.REGISTER) {
            boolean points = item.get_points() > 0;
            if (item.get_points() > 0) {
                if (isFreeFoodExist) {
                    return;
                } else {
                    isFreeFoodExist = true;
                }
            }
            String url = Singleton.currentState().getStore().getContext().getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.api_add_variants);
            JSONObject params = new JSONObject();
            try {
                JSONArray variants = new JSONArray();
                Variant v = item.get_variant();
                JSONObject variant = new JSONObject();
                variant.put("id", v.get_id());
                variant.put("count", 1);
                variants.put(variant);
                JSONObject userProfileJson = Singleton.currentState().getUser().getProfile();
                String session = userProfileJson.getString("session");
                params.put("session", session);
                params.put("variants", variants);
                params.put("slug", slugRestaurant);
                params.put("points", points);

                new JsonHelperLoad(url, params, this, "addProduct").execute();
            } catch (JSONException e) {
                Log.i(TAG, "addProductItemRegister error");
                e.printStackTrace();
            }
        }
    }

    public void addProductItem(ProductItem item, String slug){
        if (this.productItems.size() == 0){
            slugRestaurant = slug;
            productItems.add(item);
        }else{
            if (slugRestaurant.equals(slug)) {
                boolean isContain = false;
                for(int j = 0; j < productItems.size(); j++){
                    if(productItems.get(j).equals(item)){
                        productItems.get(j).addCountTo(item.get_count());
                        isContain = true;
                    }
                }
                if (!isContain){
                    productItems.add(item);
                }
            }else{
                if (delegateContext != null){
                    delegateContext.showAlert(item, slug);
                }
            }
        }
        if (delegateContext != null){
            delegateContext.updateBasket(productItems.size());
        }
    }

    public void resetRegisterBasket(Product product, String slug){
        slugRestaurant = slug;
        addProduct(product);
    }

    public double getPrice() {
        double price = 0;
        for (int i = 0; i < productItems.size(); i++) {
            price += productItems.get(i).get_variant().get_price()*productItems.get(i).get_count();
        }
        return price;
    }

    public double getDeliveryPrice() {
        return 0;
    }

    public int getPoints() {
        double price = getPrice();
        return (int)(price*5);
    }

    public double getMinimumPrice() {
        Restaurant restaurant = Singleton.currentState().getStore().getRestaurantBySlug(slugRestaurant);
        return restaurant.get_minimal_price();
    }

    public JSONArray getItemsJson() {
        JSONArray variants = new JSONArray();
        for (int i = 0; i < productItems.size(); i++) {
            JSONObject variant = new JSONObject();
            try {
                variant.put("id", productItems.get(i).get_variant().get_id());
                variant.put("count", productItems.get(i).get_count());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            variants.put(variant);
        }
        return variants;
    }

    public void removeProductItem(ProductItem item){
        if(Singleton.currentState().getUser().getStatus() == STATUS.REGISTER){

            String url = Singleton.currentState().getStore().getContext().getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.api_remove_item);
            JSONObject params = new JSONObject();
            try {
                JSONObject userProfileJson = Singleton.currentState().getUser().getProfile();
                String session = userProfileJson.getString("session");
                params.put("session", session);
                params.put("variant_id", item.get_variant().get_id());
                new JsonHelperLoad( url, params, this, "removeItem").execute();
            } catch (JSONException e) {
                Log.i(TAG,"initial Basket error");
                e.printStackTrace();
            }

            if (item.get_points()  > -1){
                isFreeFoodExist = false;
            }
        }
    }

    public void minusProductItem(ProductItem item){
        if(Singleton.currentState().getUser().getStatus() == STATUS.REGISTER){

            String url = Singleton.currentState().getStore().getContext().getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.api_remove_variant);
            JSONObject params = new JSONObject();
            try {
                JSONObject userProfileJson = Singleton.currentState().getUser().getProfile();
                String session = userProfileJson.getString("session");
                params.put("session", session);
                params.put("variant_id", item.get_variant().get_id());
                new JsonHelperLoad( url, params, this, "minusItem").execute();
            } catch (JSONException e) {
                Log.i(TAG,"initial Basket error");
                e.printStackTrace();
            }

            if (item.get_points()  > -1){
                isFreeFoodExist = false;
            }
        }
    }

}
