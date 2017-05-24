package by.netbix.alexandrzanko.mobile_6vkusov;

import android.content.Context;
import android.view.View;

import by.netbix.alexandrzanko.mobile_6vkusov.Activities.MainActivity;
import by.netbix.alexandrzanko.mobile_6vkusov.Users.UserInterface;

/**
 * Created by alexandrzanko on 3/1/17.
 */

public class Singleton {

    private final String TAG = this.getClass().getSimpleName();

    private static Singleton instance;
    private LocalStorage store;
    private UserInterface user;
    private Context context;

    private Singleton(){}

    public static Singleton currentState(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    public void initStore(Context context) {
        if(context != null){
            this.context = context;
        }
        ((MainActivity)this.context).lunchScreen.setVisibility(View.VISIBLE);
        store = new LocalStorage(this.context);
    }

    public UserInterface getUser() {
        return user;
    }

    public void setUser(UserInterface user) {this.user = user;}

    public LocalStorage getStore() {
        return store;
    }
}
