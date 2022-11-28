package com.trungdunghoang125.alpharadio.data.local.language;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.trungdunghoang125.alpharadio.data.domain.Language;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by trungdunghoang125 on 11/28/2022.
 */
@Database(entities = {Language.class}, version = 1, exportSchema = false)
public abstract class LanguageDatabase extends RoomDatabase {
    public abstract LanguageDao languageDao();

    private static volatile LanguageDatabase sInstance;

    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static LanguageDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LanguageDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(), LanguageDatabase.class, "languages").build();
                }
            }
        }
        return sInstance;
    }
}
