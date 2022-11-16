package com.trungdunghoang125.alpharadio.data.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

/**
 * Created by trungdunghoang125 on 11/12/2022.
 */

@Entity(tableName = "countries")
public class Country {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @Expose
    @ColumnInfo(name = "name")
    private String name;

    @Expose
    @ColumnInfo(name = "stationcount")
    private String stationCount;

    public Country(int id, String name, String stationCount) {
        this.id = id;
        this.name = name;
        this.stationCount = stationCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStationCount() {
        return stationCount;
    }
}
