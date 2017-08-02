package by.vkus.alexandrzanko.mobile_6vkusov.Models;


import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;

/**
 * Created by alexandrzanko on 8/2/17.
 */

public class UserGeneral implements IUser
{
    private BasketUser basket;

    @Override
    public STATUS getStatus() {
        return STATUS.GENERAL;
    }

    @Override
    public BasketUser getBasket() {
        return null;
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
    public void setBasketUser(BasketUser basket) {
        this.basket = basket;
    }
}
