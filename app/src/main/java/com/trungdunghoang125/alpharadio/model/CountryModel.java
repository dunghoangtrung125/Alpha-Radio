package com.trungdunghoang125.alpharadio.model;

import com.google.gson.annotations.SerializedName;

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
