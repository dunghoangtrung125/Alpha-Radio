package com.trungdunghoang125.alpharadio.service;

import static com.trungdunghoang125.alpharadio.App.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.gson.Gson;
import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioCacheDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trungdunghoang125 on 11/16/2022.
 */
public class RadioPlayerService extends Service {
    public static final String ACTION_PREVIOUS = "action-previous";
    public static final String ACTION_PLAY = "action-play";
    public static final String ACTION_PAUSE = "action-pause";
    public static final String ACTION_NEXT = "action-next";
    public static final String RADIO_LAST_PLAYED = "LAST_PLAYED";

    private static Bitmap image = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_radio);

    public static Notification notification;

    private final IBinder mIBinder = new ServiceBinder();

    private ExoPlayer player;

    private static List<RadioStation> stationList = new ArrayList<>();

    public RadioStation station = null;

    private int currentPosition;

    private PowerManager powerManager;

    private PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate() {
        super.onCreate();
        player = new ExoPlayer.Builder(this).build();
        // Keep the CPU awake to run service
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "AlphaRadio::MyWakeLockTag");
        wakeLock.acquire();
        // Log
        Log.d("tranle1811", "Service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("tranle1811", "Service onStartCommand");
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("tranle1811", "Service onBind");
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("tranle1811", "Service onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d("tranle1811", "Service onDestroy");
        super.onDestroy();
        player.release();
        wakeLock.release();
    }

    public static void getStationCache() {
        stationList.addAll(RadioCacheDataSource.cacheStations);
    }

    public void playRadio(String url) {
        Uri uri = Uri.parse(url);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        playExoPlayer();
    }

    public void playNextRadioStation() {
        if (currentPosition < stationList.size() - 1) {
            currentPosition++;
            RadioStation station = stationList.get(currentPosition);
            if (isPlaying()) {
                stopExoPlayer();
            }
            getCurrentStation(currentPosition);

        } else {
            Toast.makeText(getApplicationContext(), "Can not play next", Toast.LENGTH_SHORT).show();
        }
    }

    public void playPreviousRadioStation() {
        if (currentPosition > 0) {
            currentPosition--;
            RadioStation station = stationList.get(currentPosition);
            if (isPlaying()) {
                stopExoPlayer();
            }
            getCurrentStation(currentPosition);
        } else {
            Toast.makeText(getApplicationContext(), "Can not play previous", Toast.LENGTH_SHORT).show();
        }
    }

    public void playExoPlayer() {
        sendNotification(station, R.drawable.ic_pause);
        player.setPlayWhenReady(true);
        player.prepare();
    }

    public void stopExoPlayer() {
        sendNotification(station, R.drawable.ic_play);
        player.setPlayWhenReady(false);
        player.stop();
    }

    public boolean isPlaying() {
        if (player != null) {
            return player.isPlaying();
        }
        return false;
    }

    public void getCurrentStation(int position) {
        station = stationList.get(position);
        String url = station.getUrl();
        playRadio(url);
        // add this station to shared preferences for using in mini player view
        SharedPreferences.Editor editor = getSharedPreferences(RADIO_LAST_PLAYED, MODE_PRIVATE)
                .edit();
        Gson gson = new Gson();
        String json = gson.toJson(station);
        editor.putString("Station", json);
        editor.apply();
    }

    public void getStationDataFromPreferences() {
        SharedPreferences preferences = getSharedPreferences(RADIO_LAST_PLAYED, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("Station", null);
        station = gson.fromJson(json, RadioStation.class);
        playRadio(station.getUrl());
    }

    public class ServiceBinder extends Binder {
        public RadioPlayerService getRadioPlayerService() {
            return RadioPlayerService.this;
        }
    }

    /**
     * Notification push
     */
    public void sendNotification(RadioStation station, int playPauseDrawable) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            MediaSessionCompat mediaSession = new MediaSessionCompat(this, "media-session");
            // set bitmap image for notification
            Glide.with(this)
                    .asBitmap()
                    .load(station.getFavicon())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            image = resource;
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_radio);
                        }
                    });

            // pending intent
            Intent intentPrev = new Intent(this, RadioBroadcastReceiver.class)
                    .setAction(ACTION_PREVIOUS);
            PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(this, 0, intentPrev, PendingIntent.FLAG_IMMUTABLE);

            Intent intentPlay = new Intent(this, RadioBroadcastReceiver.class)
                    .setAction(ACTION_PLAY);
            PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(this, 0, intentPlay, PendingIntent.FLAG_IMMUTABLE);

            Intent intentNext = new Intent(this, RadioBroadcastReceiver.class)
                    .setAction(ACTION_NEXT);
            PendingIntent pendingIntentNext = PendingIntent.getBroadcast(this, 0, intentNext, PendingIntent.FLAG_IMMUTABLE);

            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_radio)
                    .setContentTitle(station.getName())
                    .setContentText(station.getCountry())
                    .setLargeIcon(image)
                    .setOnlyAlertOnce(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .addAction(R.drawable.ic_skip_previous, "Previous", pendingIntentPrev)
                    .addAction(playPauseDrawable, "Play", pendingIntentPlay)
                    .addAction(R.drawable.ic_skip_next, "Next", pendingIntentNext)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2)
                            .setMediaSession(mediaSession.getSessionToken()))
                    .setShowWhen(false)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();
            startForeground(1, notification);
        }
    }

    /**
     ** Send data back to player activity
     */

}