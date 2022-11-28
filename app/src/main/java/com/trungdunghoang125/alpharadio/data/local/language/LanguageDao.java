package com.trungdunghoang125.alpharadio.data.local.language;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.trungdunghoang125.alpharadio.data.domain.Language;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/28/2022.
 */
@Dao
public interface LanguageDao {
    @Query("SELECT * FROM languages")
    List<Language> getLanguages();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveLanguages(List<Language> languages);

    @Query("DELETE FROM languages")
    void deleteLanguages();
}