package com.trungdunghoang125.alpharadio.utils;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by trungdunghoang125 on 11/15/2022.
 */
public class DiskExecutor implements Executor {

    private final Executor diskExecutor;

    public DiskExecutor() {
        this.diskExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        diskExecutor.execute(runnable);
    }
}
