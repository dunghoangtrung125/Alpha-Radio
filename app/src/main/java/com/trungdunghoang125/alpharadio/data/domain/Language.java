package com.trungdunghoang125.alpharadio.data.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

/**
 * Created by trungdunghoang125 on 11/28/2022.
 */
@Entity(tableName = "languages")
public class Language {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    @Expose
    private String name;

    @ColumnInfo(name = "stationcount")
    @Expose
    private String stationCount;

    public Language(int id, String name, String stationCount) {
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
