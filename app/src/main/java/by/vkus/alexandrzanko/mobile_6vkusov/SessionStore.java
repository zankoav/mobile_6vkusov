package by.vkus.alexandrzanko.mobile_6vkusov;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by alexandrzanko on 8/4/17.
 */

public class SessionStore {

    public final String USER_SESSION = "session";
    private final String SESSION_STORE = "sessionStore";
    private SharedPreferences sessionStore;

    public SessionStore(Context context){
        this.sessionStore = context.getSharedPreferences(SESSION_STORE, Context.MODE_PRIVATE);
    }

    public String getStringValueStorage(String key) {
        if(this.sessionStore.contains(key)) {
            return this.sessionStore.getString(key, "");
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
