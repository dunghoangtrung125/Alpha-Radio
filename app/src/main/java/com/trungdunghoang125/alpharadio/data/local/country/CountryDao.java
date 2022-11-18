package com.trungdunghoang125.alpharadio.data.local.country;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.trungdunghoang125.alpharadio.data.domain.Country;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/12/2022.
 */

@Dao
public interface CountryDao {

    @Query("SELECT * FROM countries")
    List<Country> getCountries();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCountries(List<Country> countries);

    @Query("DELETE FROM countries")
    void deleteCountries();
}
