package by.netbix.alexandrzanko.mobile_6vkusov.Users;

import by.netbix.alexandrzanko.mobile_6vkusov.Models.Product;
import by.netbix.alexandrzanko.mobile_6vkusov.Models.ProductItem;

/**
 * Created by alexandrzanko on 4/26/17.
 */

public interface BasketViewInterface {
    void updateBasket(int count);
    void showAlert(ProductItem product, String slug);
    void showAlertNewOrder(Product product, String slug);

}
