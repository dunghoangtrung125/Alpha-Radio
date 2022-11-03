package com.trungdunghoang125.alpharadio.model;

import com.google.gson.annotations.SerializedName;

public class CountryModel {
    private String name;

    @SerializedName("stationcount")
    private int numberOfStation;

    public String getName() {
        return name;
    }

    public int getNumberOfStation() {
        return numberOfStation;
    }
}
