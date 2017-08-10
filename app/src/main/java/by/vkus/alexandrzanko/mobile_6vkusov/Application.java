package by.vkus.alexandrzanko.mobile_6vkusov;

import android.content.Intent;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.AuthActivities.LoginActivityV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Settings.DiscreteScrollViewOptions;

/**
 * Created by alexandrzanko on 8/3/17.
 */

public class Application extends android.app.Application {

    private static Application instance;

    public static Application getInstance() {
        return instance;
    }


    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                Intent intent = new Intent(Application.this, LoginActivityV2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DiscreteScrollViewOptions.init(this);
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
    }
}
