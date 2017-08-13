package by.vkus.alexandrzanko.mobile_6vkusov.Interfaces;

import java.util.List;

import by.vkus.alexandrzanko.mobile_6vkusov.Models.Discounts.MOurDiscountCategory;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.FoodByPoint;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MCategory;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MComment;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MOrderItem;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MProduct;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MRestaurant;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MRestaurantDeliveryInfo;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MenuRestaurant;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Order.MOrder;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.UserRegister;
import by.vkus.alexandrzanko.mobile_6vkusov.Settings.SettingsApp;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by alexandrzanko on 7/28/17.
 */

public interface IApi {

    @GET("/rest/api/categories")
    Call<List<MCategory>> getCategory();

    @GET("/rest/api/food_by_points")
    Call<List<FoodByPoint>> getFoodsByPoint();

    @GET("/rest/api/settings")
    Call<SettingsApp> getSettings();

    @GET("/rest/api/get_our_points")
    Call<List<MOurDiscountCategory>> getOurPoints();

    @FormUrlEncoded
    @POST("/rest/api/get_orders_user")
    Call<List<MOrder>> getOrdersUser(@Field("session") String session);

    @FormUrlEncoded
    @POST("/rest/api/send_comment_by_order")
    Call<String> sendCommentByOrder(@Field("session") String session, @Field("id_order") int id_order, @Field("type_comment") int type_comment, @Field("message") String message);

    @FormUrlEncoded
    @POST("/rest/api/get_comments_by_restaurant_slug")
    Call<List<MComment>> getCommentsByRestaurantSlug(@Field("slug") String slug);

    @FormUrlEncoded
    @POST("/rest/api/get_restaurants_by_slug")
    Call<List<MRestaurant>> getRestaurantsBySlug(@Field("slug") String slug);

    @FormUrlEncoded
    @POST("/rest/api/get_favorite_restaurants_by_user")
    Call<List<MRestaurant>> getFavoriteRestaurantsByUser(@Field("session") String session);

    @FormUrlEncoded
    @POST("/rest/api/get_count_order_items_by_user")
    Call<Integer> getCountOrderItemsByUser(@Field("session") String session);

    @FormUrlEncoded
    @POST("/rest/api/get_info_delivery_restaurant")
    Call<MRestaurantDeliveryInfo> getInfoDeliveryRestaurant(@Field("session") String session, @Field("restaurant_slug") String restaurant_slug);

    @FormUrlEncoded
    @POST("/rest/api/create_new_order_by_register_user")
    Call<Boolean> createNewOrderByRegisterUser(@Field("session") String session, @Field("id") int idVariant);

    @FormUrlEncoded
    @POST("/rest/api/add_item_order_by_register_user")
    Call<Boolean> addItemOrderByRegisterUser(@Field("session") String session, @Field("id") int idVariant);

    @FormUrlEncoded
    @POST("/rest/api/minus_item_order_by_register_user")
    Call<Boolean> minusItemOrderByRegisterUser(@Field("session") String session, @Field("id") int idVariant);

    @FormUrlEncoded
    @POST("/rest/api/remove_item_order_by_register_user")
    Call<Boolean> removeItemOrderByRegisterUser(@Field("session") String session, @Field("id") int orderItemId);

    @FormUrlEncoded
    @POST("/rest/api/add_free_food_order_by_register_user")
    Call<Boolean> addFreeFoodOrderByRegisterUser(@Field("session") String session, @Field("id") int idVariant);

    @FormUrlEncoded
    @POST("/rest/api/create_new_order_free_food_by_register_user")
    Call<Boolean> createNewOrderFreeFoodByRegisterUser(@Field("session") String session, @Field("id") int idVariant);

    @FormUrlEncoded
    @POST("/rest/api/get_favorite_restaurants_by_slugs")
    Call<List<MRestaurant>> getFavoriteRestaurantsBySlugs(@Field("slugs") String slugs);

    @FormUrlEncoded
    @POST("/rest/api/get_order_items_for_general_by_restaurant_slug")
    Call<List<MOrderItem>> getOrderItemsForGeneralByRestaurantsSlug(@Field("slug") String slug, @Field("variants") String variants);

    @FormUrlEncoded
    @POST("/rest/api/get_order_items_by_register_user")
    Call<List<MOrderItem>> getOrderItemsByRegisterUser(@Field("session") String session);

    @FormUrlEncoded
    @POST("/rest/api/check_favorite_restaurants_by_user")
    Call<Boolean> checkFavoriteRestaurantsByUser(@Field("session") String session, @Field("slug") String slug);

    @FormUrlEncoded
    @POST("/rest/api/change_favorite_restaurants_by_user")
    Call<Boolean> changeFavoriteRestaurantsByUser(@Field("session") String session, @Field("slug") String slug);

    @FormUrlEncoded
    @POST("/rest/api/get_menu_by_restaurant_slug")
    Call<List<MenuRestaurant>> getMenuByRestaurantSlug(@Field("slug") String slug);

    @FormUrlEncoded
    @POST("/rest/api/get_products_by_menu_slug")
    Call<List<MProduct>> getProductsByMenuSlug(@Field("menu_slug") String menu_slug, @Field("restaurant_slug") String restaurant_slug);

    @FormUrlEncoded
    @POST("/rest/api/get_info_by_restaurant_slug")
    Call<String> getInfoByRestaurantSlug(@Field("slug") String slug);

    @FormUrlEncoded
    @POST("/rest/api/get_user_by_session")
    Call<UserRegister> getUserBySession(@Field("session") String session);

    @FormUrlEncoded
    @POST("/rest/api/get_restaurant_by_slug")
    Call<MRestaurant> getRestaurantBySlug(@Field("slug") String slug);

    @FormUrlEncoded
    @POST("/rest/api/set_user_profile")
    Call<UserRegister> setUserProfile(@Field("session") String session, @Field("first_name") String first_name, @Field("last_name") String last_name);

    @FormUrlEncoded
    @POST("/rest/api/call_friend")
    Call<String> callFriend(@Field("session") String session, @Field("email") String email);

    @FormUrlEncoded
    @POST("/rest/api/facebook")
    Call<UserRegister> loginFacebook(@Field("facebook") String jsonObjectStr);

    @FormUrlEncoded
    @POST("/rest/api/vk")
    Call<UserRegister> loginVk(@Field("vk") String jsonObjectStr);

    @FormUrlEncoded
    @POST("/rest/api/login")
    Call<UserRegister> login(@Field("email") String email, @Field("password") String password);

}
