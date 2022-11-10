package com.trungdunghoang125.alpharadio.data.remote;

import com.trungdunghoang125.alpharadio.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by trungdunghoang125 on 11/9/2022.
 */
public class RetrofitClient {
    public static RetrofitClient instance = null;

    private RadioBrowserApi api;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(RadioBrowserApi.class);
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public RadioBrowserApi getApi() {
        return api;
    }
}
