package by.vkus.alexandrzanko.mobile_6vkusov;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.MainActivityV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.UserGeneral;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.UserRegister;
import by.vkus.alexandrzanko.mobile_6vkusov.Settings.SettingsApp;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.UserInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexandrzanko on 3/1/17.
 */

public class SingletonV2 {

    private static SingletonV2 instance;

    private IUser iUser;
    public IUser getIUser() {
        return iUser;
    }
    private SessionStoreV2 sessionStoreV2;
    private SettingsApp settingsApp;
    public SettingsApp getSettingsApp() {
        return settingsApp;
    }
    public SessionStoreV2 getSessionStoreV2() {
        return sessionStoreV2;
    }

    private SingletonV2(){}

    public static SingletonV2 currentState(){
        if(instance == null){
            instance = new SingletonV2();
        }
        return instance;
    }

    public void initStore(Context context) {
        ((MainActivityV2)context).lunchScreen.setVisibility(View.VISIBLE);
        ((MainActivityV2)context).logoViewCircle.setVisibility(View.VISIBLE);
        ((MainActivityV2)context).showAnimation();
        ((MainActivityV2)context).logoView.setVisibility(View.VISIBLE);
        this.sessionStoreV2 = new SessionStoreV2(context);
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
        String session = sessionStoreV2.getStringValueStorage(sessionStoreV2.USER_SESSION);
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
                    loadUserBasket(contextF);
                }

                @Override
                public void onFailure(Call<UserRegister> call, Throwable t) {
                    iUser = new UserGeneral();
                    loadUserBasket(contextF);
                }
            });
        }else{
            this.iUser = new UserGeneral();
            loadUserBasket(contextF);
        }
    }

    private void loadUserBasket(Context context) {

        if(iUser.getStatus().equals(STATUS.REGISTER)){

        }else{

        }


        ((MainActivityV2)context).loadComplete();

    }

    public String getDate_time(long date_time) {
        long time = date_time * 1000;
        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    private UserInterface user;
    public UserInterface getUser() {
        return user;
    }
    public void setUser(UserInterface user) {this.user = user;}
}
