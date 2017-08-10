package by.vkus.alexandrzanko.mobile_6vkusov.Models;


import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.SessionStoreV2;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;

/**
 * Created by alexandrzanko on 8/2/17.
 */

public class UserGeneral implements IUser
{

    @Override
    public STATUS getStatus() {
        return STATUS.GENERAL;
    }

    @Override
    public String getCurrentOrderRestaurantSlug() {
        SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
        return store.getStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_RESTAURANT_SLUG);
    }

    @Override
    public void setCurrentOrderRestaurantSlug(String currentOrderRestaurantSlug) {
        SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
        store.setStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_RESTAURANT_SLUG, currentOrderRestaurantSlug);
    }

    @Override
    public String getFirstName() {
        return null;
    }

    @Override
    public String getLastName() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public Integer getPoints() {
        return 0;
    }

    @Override
    public String getSession() {
        return null;
    }

    @Override
    public Integer getPhoneCode() {
        return null;
    }

    @Override
    public Integer getPhoneNumber() {
        return null;
    }

    @Override
    public String getAvatar() {
        return null;
    }

    @Override
    public void setPhone_number(Integer phone_number) {

    }

    @Override
    public void setAvatar(String avatar) {

    }

    @Override
    public void setPhone_code(Integer phone_code) {

    }

    @Override
    public void setSession(String session) {

    }

    @Override
    public void setPoints(Integer points) {

    }

    @Override
    public void setEmail(String email) {

    }

    @Override
    public void setLast_name(String last_name) {

    }

    @Override
    public void setFirst_name(String first_name) {

    }


}
