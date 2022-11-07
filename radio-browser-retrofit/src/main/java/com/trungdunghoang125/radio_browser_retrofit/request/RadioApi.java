package com.trungdunghoang125.radio_browser_retrofit.request;

import com.trungdunghoang125.radio_browser_retrofit.model.CountryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by trungdunghoang125 on 11/7/2022.
 */
public interface RadioApi {
    // Get list of country and country codes
    @Headers({
            "X-RapidAPI-Key: a0ffb895eamshb2ff9106aeaf436p1b9818jsn220519fbf2c3",
            "X-RapidAPI-Host: radio-browser.p.rapidapi.com"
    })
    @GET("/json/countrycodes")
    Call<List<CountryModel>> getCountryCode(
            @Query("reverse") boolean isReverse,
            @Query("hidebroken") boolean isHidebroken
    );
}
