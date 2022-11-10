package com.trungdunghoang125.radiobrowserokhttp;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by trungdunghoang125 on 11/4/2022.
 */
public class AppExecutors {
    private static AppExecutors sInstance;

    private static final ScheduledExecutorService executorInstance = Executors.newScheduledThreadPool(3);

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            sInstance = new AppExecutors();
        }
        return sInstance;
    }

    public static ScheduledExecutorService getExecutorInstance() {
        return executorInstance;
    }
}
