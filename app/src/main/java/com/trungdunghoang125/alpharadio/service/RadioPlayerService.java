package com.trungdunghoang125.alpharadio.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

/**
 * Created by trungdunghoang125 on 11/16/2022.
 */
public class RadioPlayerService extends Service {

    private final IBinder serviceIBinder = new ServiceBinder();

    private ExoPlayer player;

    private boolean playWhenReady = true;

    private String urlStream;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        urlStream = intent.getStringExtra("url_radio");
        initializePlayer(urlStream);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceIBinder;
    }

    private void initializePlayer(String url) {
        player = new ExoPlayer.Builder(this)
                .build();
        Uri uri = Uri.parse(url);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);

        player.setPlayWhenReady(playWhenReady);
        player.prepare();
    }

    public class ServiceBinder extends Binder {
        public RadioPlayerService getRadioPlayerService() {
            return RadioPlayerService.this;
        }
    }
}
