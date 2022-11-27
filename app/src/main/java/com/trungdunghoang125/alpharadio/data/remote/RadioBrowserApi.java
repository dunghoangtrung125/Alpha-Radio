package com.trungdunghoang125.alpharadio.data.remote;

import com.trungdunghoang125.alpharadio.data.model.CountryRemote;
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
    Call<List<CountryRemote>> getCountries(
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

    // Get Radio by country code
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

    // Get Radio By Name (Search)
    @Headers({
            "X-RapidAPI-Key: " + Constants.X_RAPIDAPI_KEY,
            "X-RapidAPI-Host: " + Constants.X_RAPIDAPI_HOST
    })
    @GET("stations/search")
    Call<List<RadioStation>> getRadioByName(
            @Query("name") String name,
            @Query("reverse") boolean reverse,
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("hidebroken") boolean hidebroken
    );

    // Get Popular Radio Station (top clicked) - get 30 items
    @Headers({
            "X-RapidAPI-Key: " + Constants.X_RAPIDAPI_KEY,
            "X-RapidAPI-Host: " + Constants.X_RAPIDAPI_HOST
    })
    @GET("stations/topclick/30")
    Call<List<RadioStation>> getPopStation(
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("hidebroken") boolean hidebroken
    );
}
