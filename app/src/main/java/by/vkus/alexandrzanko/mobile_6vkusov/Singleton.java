package by.vkus.alexandrzanko.mobile_6vkusov;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.MainActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.UserGeneral;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.UserRegister;
import by.vkus.alexandrzanko.mobile_6vkusov.Settings.SettingsApp;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.UserInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexandrzanko on 3/1/17.
 */

public class Singleton{

    private final String TAG = this.getClass().getSimpleName();

    private static Singleton instance;

    private IUser iUser;
    public IUser getIUser() {
        return iUser;
    }
    private SessionStore sessionStore;
    private SettingsApp settingsApp;
    public SettingsApp getSettingsApp() {
        return settingsApp;
    }
    public SessionStore getSessionStore() {
        return sessionStore;
    }



    private LocalStorage store;
    private UserInterface user;


    private Singleton(){}

    public static Singleton currentState(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    public void initStore(Context context) {
        ((MainActivity)context).lunchScreen.setVisibility(View.VISIBLE);
        ((MainActivity)context).logoViewCircle.setVisibility(View.VISIBLE);
        ((MainActivity)context).showAnimation();
        ((MainActivity)context).logoView.setVisibility(View.VISIBLE);
        this.sessionStore = new SessionStore(context);
        final Context contextF = context;
        ApiController.getApi().getSettings().enqueue(new Callback<SettingsApp>() {
            @Override
            public void onResponse(Call<SettingsApp> call, Response<SettingsApp> response) {
                settingsApp = response.body();
            }
            @Override
            public void onFailure(Call<SettingsApp> call, Throwable t) {
                Toast.makeText(contextF, "Ошибка соединения", Toast.LENGTH_SHORT).show();
            }
        });
        String session = sessionStore.getStringValueStorage(sessionStore.USER_SESSION);
        if (session != null){
            ApiController.getApi().getUserBySession(session).enqueue(new Callback<UserRegister>() {
                @Override
                public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {
                    if (response.code() == 200){
                        IUser user = response.body();
                        iUser = user != null ? user : new UserGeneral();
                    }else{
                        iUser = new UserGeneral();
                    }
                }
                @Override
                public void onFailure(Call<UserRegister> call, Throwable t) {
                    iUser = new UserGeneral();
                }
            });
        }else{
            this.iUser = new UserGeneral();
        }



        store = new LocalStorage(context);
    }




    public UserInterface getUser() {
        return user;
    }

    public void setUser(UserInterface user) {this.user = user;}

    public LocalStorage getStore() {
        return store;
    }

}
