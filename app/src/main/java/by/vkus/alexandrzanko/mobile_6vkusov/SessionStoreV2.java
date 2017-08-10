package by.vkus.alexandrzanko.mobile_6vkusov;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by alexandrzanko on 8/4/17.
 */

public class SessionStoreV2 {

    public final String USER_SESSION = "session";
    public final String USER_FAVORITES = "favorites";
    public final String USER_GENERAL_CURRENT_ORDER_RESTAURANT_SLUG = "currentOrderRestaurantSlug";
    public final String USER_GENERAL_CURRENT_ORDER_VARIANTS= "currentOrderVariants";

    private final String SESSION_STORE = "sessionStore";

    private SharedPreferences sessionStore;

    public SessionStoreV2(Context context){
        this.sessionStore = context.getSharedPreferences(SESSION_STORE, Context.MODE_PRIVATE);
    }

    public String getStringValueStorage(String key) {
        if(this.sessionStore.contains(key)) {
            return this.sessionStore.getString(key, "");
        }else{
            return null;
        }
    }

    public void setStringSetValueStorage(String key, Set<String> value){
        SharedPreferences.Editor editor = this.sessionStore.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public Set<String> getStringSetValueStorage(String key){
        if(this.sessionStore.contains(key)) {
            return this.sessionStore.getStringSet(key, null);
        }else{
            return null;

        }
    }

    public void setStringValueStorage(String key, String value){
        SharedPreferences.Editor editor = this.sessionStore.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void clearKeyStorage(String key) {
        SharedPreferences.Editor editor = this.sessionStore.edit();
        editor.remove(key);
        editor.apply();
    }
}
