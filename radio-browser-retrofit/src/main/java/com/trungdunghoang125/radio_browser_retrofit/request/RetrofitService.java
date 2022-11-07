package com.trungdunghoang125.radio_browser_retrofit.request;

import com.trungdunghoang125.radio_browser_retrofit.utils.Credentials;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by trungdunghoang125 on 11/7/2022.
 */
public class RetrofitService {
    private static final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(Credentials.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static final Retrofit retrofit = retrofitBuilder.build();

    private static final RadioApi radioApi = retrofit.create(RadioApi.class);

    public static RadioApi getRadioApi() {
        return radioApi;
    }
}
