package com.trungdunghoang125.alpharadio.data.local.favorite;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.trungdunghoang125.alpharadio.data.model.RadioStation;

import java.util.List;


/**
 * Created by trungdunghoang125 on 11/25/2022.
 */
@Dao
public interface FavStationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RadioStation station);

    @Update
    void update(RadioStation station);

    @Delete
    void delete(RadioStation station);

    @Query("SELECT * FROM fav_stations")
    List<RadioStation> getFavStations();
}
