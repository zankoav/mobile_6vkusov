package by.vkus.alexandrzanko.mobile_6vkusov.Interfaces;

import java.util.List;

import by.vkus.alexandrzanko.mobile_6vkusov.Models.FoodByPoint;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MCategory;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MComment;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MProduct;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MRestaurant;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MenuRestaurant;
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

    @FormUrlEncoded
    @POST("/rest/api/get_comments_by_restaurant_slug")
    Call<List<MComment>> getCommentsByRestaurantSlug(@Field("slug") String slug);

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
