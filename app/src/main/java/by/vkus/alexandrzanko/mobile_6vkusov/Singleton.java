package by.vkus.alexandrzanko.mobile_6vkusov;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import by.vkus.alexandrzanko.mobile_6vkusov.Activities.MainActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.UserGeneral;
import by.vkus.alexandrzanko.mobile_6vkusov.Settings.SettingsApp;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.UserInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexandrzanko on 3/1/17.
 */

public class Singleton {

    private static Singleton instance;

    private IUser iUser;
    public IUser getIUser() {
        return iUser;
    }
    public void setIUser(IUser user) {
        this.iUser = user;
    }


    private SettingsApp settingsApp;

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
        final Context contextF = context;

        ApiController.getApi().getSettings().enqueue(new Callback<SettingsApp>() {
            @Override
            public void onResponse(Call<SettingsApp> call, Response<SettingsApp> response) {
                settingsApp = response.body();
                iUser = new UserGeneral();
            }
            @Override
            public void onFailure(Call<SettingsApp> call, Throwable t) {
                Toast.makeText(contextF, "Ошибка соединения", Toast.LENGTH_SHORT).show();
            }
        });


        if(context != null){
            this.context = context;
        }
        ((MainActivity)this.context).lunchScreen.setVisibility(View.VISIBLE);
        ((MainActivity)this.context).logoViewCircle.setVisibility(View.VISIBLE);
        ((MainActivity)this.context).showAnimation();
        ((MainActivity)this.context).logoView.setVisibility(View.VISIBLE);
        store = new LocalStorage(this.context);
    }

    public UserInterface getUser() {
        return user;
    }

    public void setUser(UserInterface user) {this.user = user;}

    public LocalStorage getStore() {
        return store;
    }


    public SettingsApp getSettingsApp() {
        return settingsApp;
    }
}
