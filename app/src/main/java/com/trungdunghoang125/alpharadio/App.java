package com.trungdunghoang125.alpharadio;

import android.app.Application;

/**
 * Created by trungdunghoang125 on 11/16/2022.
 */
public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }
}
