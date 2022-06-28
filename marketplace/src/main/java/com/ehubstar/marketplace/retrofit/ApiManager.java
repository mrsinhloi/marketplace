package com.ehubstar.marketplace.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiManager {

    //region ----INIT----
    ////////////////////////////////////////////////////////////////////////////////////////
    public static ApiWebservice services;
    private static ApiManager apiManager;

    private ApiManager(String url) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))

                .build();
        services = retrofit.create(ApiWebservice.class);
    }

    public static ApiManager getIntance(String url) {
        if (apiManager == null) {
            apiManager = new ApiManager(url);
        }
        return apiManager;
    }

    public static ApiManager newIntance(String url) {
        return apiManager = new ApiManager(url);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////
    //endregion

    //region ----APIs----
    public void addImageItem(String token,
                             JsonObject json,
                             Callback<JsonObject> callback) {
        Call<JsonObject> call = services.addImageItem(token,json);
        call.enqueue(callback);
    }




    //endregion




}
