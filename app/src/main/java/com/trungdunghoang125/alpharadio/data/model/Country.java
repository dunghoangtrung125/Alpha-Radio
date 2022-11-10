package com.trungdunghoang125.alpharadio.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by trungdunghoang125 on 11/9/2022.
 */
public class Country {
    private String name;

    @SerializedName("stationcount")
    private String stationCount;

    public Country(String name, String stationCount) {
        this.name = name;
        this.stationCount = stationCount;
    }

    public String getName() {
        return name;
    }

    public String getStationCount() {
        return stationCount;
    }
}
