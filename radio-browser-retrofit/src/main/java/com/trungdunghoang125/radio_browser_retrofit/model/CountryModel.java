package com.trungdunghoang125.radio_browser_retrofit.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by trungdunghoang125 on 11/7/2022.
 */
public class CountryModel {
    @SerializedName("name")
    private String mCountryName;

    @SerializedName("stationcount")
    private int mNumberOfStation;

    public String getCountryName() {
        return mCountryName;
    }

    public int getNumberOfStation() {
        return mNumberOfStation;
    }
}
