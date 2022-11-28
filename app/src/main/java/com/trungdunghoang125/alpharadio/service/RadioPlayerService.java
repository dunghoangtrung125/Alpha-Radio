package com.trungdunghoang125.alpharadio.service;

import static com.trungdunghoang125.alpharadio.App.CHANNEL_ID;
import static com.trungdunghoang125.alpharadio.utils.Constants.ACTION_NAME;
import static com.trungdunghoang125.alpharadio.utils.Constants.EXO_PLAYER_PLAYER_STATUS_ACTION;
import static com.trungdunghoang125.alpharadio.utils.Constants.STATE;

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
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.gson.Gson;
import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioCacheDataSource;
import com.trungdunghoang125.alpharadio.ui.fragment.CountryDetailFragment;

/**
 * Created by trungdunghoang125 on 11/16/2022.
 */
public class RadioPlayerService extends Service {
    public static final String ACTION_PREVIOUS = "action-previous";
    public static final String ACTION_PLAY = "action-play";
    public static final String ACTION_STOP = "action-stop";
    public static final String ACTION_NEXT = "action-next";
    public static final String RADIO_LAST_PLAYED = "LAST_PLAYED";
    public static final String SEND_DATA_TO_PLAYER = "send-data-to-player";

    private static Bitmap image = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_radio);

    private final IBinder mIBinder = new ServiceBinder();
    public RadioStation currentStation = null;
    private ExoPlayer player;
    private LocalBroadcastManager localBroadcast;

    private int currentPosition;

    private PowerManager powerManager;

    private PowerManager.WakeLock wakeLock;

    private boolean isPlaying;

    @Override
    public void onCreate() {
        super.onCreate();
        player = new ExoPlayer.Builder(this).build();
        localBroadcast = LocalBroadcastManager.getInstance(this);
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Intent intent = new Intent(EXO_PLAYER_PLAYER_STATUS_ACTION);
                if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    intent.putExtra(STATE, PlaybackStateCompat.STATE_BUFFERING);
                }
                localBroadcast.sendBroadcast(intent);
            }
        });
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
        // handle action from notification
        String action = intent.getStringExtra(ACTION_NAME);
        if (action != null) {
            handleActionRadio(action);
        }

        int position = intent.getIntExtra(CountryDetailFragment.START_RADIO_EXTRAS, -1);
        if (position != -1) {
            currentPosition = position;
            initRadio(currentPosition);
        }

        return START_NOT_STICKY;
    }

    private void handleActionRadio(String action) {
        switch (action) {
            case ACTION_PREVIOUS:
                playPreviousRadioStation();
                break;
            case ACTION_PLAY:
                startExoPlayer();
                break;
            case ACTION_STOP:
                stopExoPlayer();
                break;
            case ACTION_NEXT:
                playNextRadioStation();
                break;
        }
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

    public void initRadio(int position) {
        getCurrentStation(position);
        Uri uri = Uri.parse(currentStation.getUrl());
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        startExoPlayer();
    }

    public void playNextRadioStation() {
        if (currentPosition < RadioCacheDataSource.cacheStations.size() - 1) {
            currentPosition++;
            if (isPlaying()) {
                stopExoPlayer();
            }
            initRadio(currentPosition);
        } else {
            Toast.makeText(getApplicationContext(), "Can not play next", Toast.LENGTH_SHORT).show();
        }
    }

    public void playPreviousRadioStation() {
        if (currentPosition > 0) {
            currentPosition--;
            if (isPlaying()) {
                stopExoPlayer();
            }
            initRadio(currentPosition);
        } else {
            Toast.makeText(getApplicationContext(), "Can not play previous", Toast.LENGTH_SHORT).show();
        }
    }

    public void startExoPlayer() {
        isPlaying = true;
        player.setPlayWhenReady(true);
        player.prepare();
        sendNotification(currentStation);
        sendDataToMusicPlayerFragment(ACTION_PLAY);
    }

    public void stopExoPlayer() {
        isPlaying = false;
        player.setPlayWhenReady(false);
        player.stop();
        sendNotification(currentStation);
        sendDataToMusicPlayerFragment(ACTION_STOP);
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public void getCurrentStation(int position) {
        if (currentPosition < RadioCacheDataSource.cacheStations.size()) {
            currentStation = RadioCacheDataSource.cacheStations.get(position);
        }

        // add this station to shared preferences for using in mini player view
        SharedPreferences.Editor editor = getSharedPreferences(RADIO_LAST_PLAYED, MODE_PRIVATE)
                .edit();
        Gson gson = new Gson();
        String json = gson.toJson(currentStation);
        editor.putString("Station", json);
        editor.apply();
    }

    public void getStationDataFromPreferences() {
        SharedPreferences preferences = getSharedPreferences(RADIO_LAST_PLAYED, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("Station", null);
        currentStation = gson.fromJson(json, RadioStation.class);
    }

    public void initRadioFromPreferences() {
        Uri uri = Uri.parse(currentStation.getUrl());
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
    }

    /**
     * Notification push
     */
    public void sendNotification(RadioStation station) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            MediaSessionCompat mediaSession = new MediaSessionCompat(this, "media-session");
            // set bitmap image for notification
            if (station.getFavicon() == null) {
                image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_radio);
            } else {
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
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_radio)
                    .setContentTitle(station.getName())
                    .setContentText(station.getCountry())
                    .setLargeIcon(image)
                    .setOnlyAlertOnce(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2)
                            .setMediaSession(mediaSession.getSessionToken()))
                    .setShowWhen(false)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            if (isPlaying) {
                notificationBuilder
                        .addAction(R.drawable.ic_skip_previous, "Previous", getPendingIntent(ACTION_PREVIOUS))
                        .addAction(R.drawable.ic_pause, "Pause", getPendingIntent(ACTION_STOP))
                        .addAction(R.drawable.ic_skip_next, "Next", getPendingIntent(ACTION_NEXT));
            } else {
                notificationBuilder
                        .addAction(R.drawable.ic_skip_previous, "Previous", getPendingIntent(ACTION_PREVIOUS))
                        .addAction(R.drawable.ic_play, "Play", getPendingIntent(ACTION_PLAY))
                        .addAction(R.drawable.ic_skip_next, "Next", getPendingIntent(ACTION_NEXT));
            }
            Notification notification = notificationBuilder.build();
            startForeground(1, notification);
        }
    }

    private PendingIntent getPendingIntent(String action) {
        Intent actionIntent = new Intent(this, RadioBroadcastReceiver.class)
                .setAction(action);
        return PendingIntent.getBroadcast(this, 0, actionIntent, PendingIntent.FLAG_IMMUTABLE);
    }

    /**
     * * Send data back to player activity
     */
    private void sendDataToMusicPlayerFragment(String action) {
        Intent intent = new Intent(SEND_DATA_TO_PLAYER);
        Bundle bundle = new Bundle();
        bundle.putParcelable("station", currentStation);
        bundle.putBoolean("isPlaying", isPlaying);
        bundle.putString("action", action);
        intent.putExtras(bundle);
        localBroadcast.sendBroadcast(intent);
    }

    public class ServiceBinder extends Binder {
        public RadioPlayerService getRadioPlayerService() {
            return RadioPlayerService.this;
        }
    }
}