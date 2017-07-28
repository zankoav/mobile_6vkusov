package by.vkus.alexandrzanko.mobile_6vkusov.Interfaces;

import java.util.List;

import by.vkus.alexandrzanko.mobile_6vkusov.Models.MCategory;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by alexandrzanko on 7/28/17.
 */

public interface IApi {

    @GET("/rest/api/categories")
    Call<List<MCategory>> getCategory();

}
