package com.trungdunghoang125.alpharadio.data.remote;

import com.trungdunghoang125.alpharadio.data.model.Country;
import com.trungdunghoang125.alpharadio.data.model.Language;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by trungdunghoang125 on 11/9/2022.
 */
public interface RadioBrowserApi {
    // Get countries list
    @Headers({
            "X-RapidAPI-Key: " + Constants.X_RAPIDAPI_KEY,
            "X-RapidAPI-Host: " + Constants.X_RAPIDAPI_HOST
    })
    @GET("countrycodes")
    Call<List<Country>> getCountries(
            @Query("reverse") boolean reverse,
            @Query("hidebroken") boolean hidebroken
    );

    // Get languages
    @Headers({
            "X-RapidAPI-Key: " + Constants.X_RAPIDAPI_KEY,
            "X-RapidAPI-Host: " + Constants.X_RAPIDAPI_HOST
    })
    @GET("languages")
    Call<List<Language>> getLanguages();

    // Get VN Radio
    @Headers({
            "X-RapidAPI-Key: " + Constants.X_RAPIDAPI_KEY,
            "X-RapidAPI-Host: " + Constants.X_RAPIDAPI_HOST
    })
    @GET("stations/search")
    Call<List<RadioStation>> getCountryRadioStation(
            @Query("countrycode") String code,
            @Query("reverse") boolean reverse,
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("hidebroken") boolean hidebroken
    );
}
