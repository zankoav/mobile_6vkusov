package by.vkus.alexandrzanko.mobile_6vkusov.Users;

import org.json.JSONObject;

/**
 * Created by alexandrzanko on 3/1/17.
 */

public class Register implements UserInterface {

    private final String TAG = this.getClass().getSimpleName();

    private Basket basket;
    private int _points;

    public Register(int points){
        this.basket = new Basket();
        this._points = points;
    }

    @Override
    public STATUS getStatus() {
        return STATUS.REGISTER;
    }

    @Override
    public JSONObject getProfile() {
//        LocalStorage store = SingletonV2.currentState().getStore();
//        JSONObject profile = null;
//        try {
//            profile = new JSONObject(store.getStringValueStorage(store.APP_PROFILE));
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.d(TAG, "load profile from local storage error");
//        }
        return null;
    }

    @Override
    public Basket getBasket() {
        return basket;
    }

    @Override
    public int getPoints() {
        return this._points;
    }

    @Override
    public void setPoints(int points) {
        this._points = points;
    }
}
