package com.trungdunghoang125.alpharadio.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

/**
 * Created by trungdunghoang125 on 11/16/2022.
 */
public class RadioPlayerService extends Service {

    private final IBinder mIBinder = new ServiceBinder();

    private ExoPlayer player;

    private PlayerNotificationManager playerNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        player = new ExoPlayer.Builder(this)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }

    public void initializePlayer(String url) {
        Uri uri = Uri.parse(url);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        player.setPlayWhenReady(true);
        player.prepare();
    }

    public void playExoPlayer() {
        player.setPlayWhenReady(true);
        player.prepare();
    }

    public void stopExoPlayer() {
        player.setPlayWhenReady(false);
        player.stop();
    }

    public void releaseExoPlayer() {
        player.release();
    }

    public boolean isPlaying() {
        if (player != null) {
            return player.getPlaybackState() == Player.STATE_READY;
        }
        return false;
    }

    public class ServiceBinder extends Binder {
        public RadioPlayerService getRadioPlayerService() {
            return RadioPlayerService.this;
        }
    }
}
