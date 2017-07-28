package by.vkus.alexandrzanko.mobile_6vkusov;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexandrzanko on 7/28/17.
 */

public class ApiController {

    public static final String BASE_URL = "https://6vkusov.by/";

    public static IApi getApi() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IApi api = retrofit.create(IApi.class);

        return api;
    }
}
