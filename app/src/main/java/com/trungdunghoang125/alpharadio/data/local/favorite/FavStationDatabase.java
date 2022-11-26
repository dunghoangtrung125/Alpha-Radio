package com.trungdunghoang125.alpharadio.data.local.favorite;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.trungdunghoang125.alpharadio.data.model.RadioStation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by trungdunghoang125 on 11/25/2022.
 */
@Database(entities = {RadioStation.class}, version = 1, exportSchema = false)
public abstract class FavStationDatabase extends RoomDatabase {
    public abstract FavStationDao stationDao();

    private static volatile FavStationDatabase sInstance;

    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static FavStationDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (FavStationDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(), FavStationDatabase.class, "fav_stations").build();
                }
            }
        }
        return sInstance;
    }
}
