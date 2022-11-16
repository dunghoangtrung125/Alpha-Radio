package com.trungdunghoang125.alpharadio.data.local.country;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.trungdunghoang125.alpharadio.data.domain.Country;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by trungdunghoang125 on 11/12/2022.
 */
@Database(entities = {Country.class}, version = 1, exportSchema = false)
public abstract class CountryDatabase extends RoomDatabase {

    public abstract CountryDao countryDao();

    private static volatile CountryDatabase sInstance;

    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CountryDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (CountryDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(), CountryDatabase.class, "countries").build();
                }
            }
        }
        return sInstance;
    }
}
