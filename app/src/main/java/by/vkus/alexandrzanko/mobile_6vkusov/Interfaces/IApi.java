package by.vkus.alexandrzanko.mobile_6vkusov.Interfaces;

import org.json.JSONObject;

import java.util.List;

import by.vkus.alexandrzanko.mobile_6vkusov.Models.MCategory;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.UserRegister;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.UserInterface;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by alexandrzanko on 7/28/17.
 */

public interface IApi {

    @GET("/rest/api/categories")
    Call<List<MCategory>> getCategory();

    @FormUrlEncoded
    @POST("/rest/api/facebook")
    Call<UserRegister> loginFacebook(@Field("facebook") String jsonObjectStr);

}
