package com.example.alexandrzanko.mobile_6vkusov.Users;

import com.example.alexandrzanko.mobile_6vkusov.Models.ProductItem;
import com.example.alexandrzanko.mobile_6vkusov.Models.Restaurant;
import com.example.alexandrzanko.mobile_6vkusov.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alexandrzanko on 3/1/17.
 */

public class Basket {

    private BasketViewInterface delegateContext;

    private ArrayList<ProductItem> productItems;
    private String slugRestaurant;

    public void setDelegateContext(BasketViewInterface delegateContext) {
        this.delegateContext = delegateContext;
    }

    public BasketViewInterface getDelegateContext() {
        return delegateContext;
    }

    public Basket() {
        this.productItems = new ArrayList<>();
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

    public void addProductItem(ProductItem item, String slug){
        if (Singleton.currentState().getUser().getStatus() == STATUS.REGISTER){

        }else{
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
        }
        if (delegateContext != null){
            delegateContext.updateBasket(productItems.size());
        }
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
}
