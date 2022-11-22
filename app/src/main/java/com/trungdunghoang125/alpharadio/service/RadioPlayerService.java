package com.trungdunghoang125.alpharadio.service;

import static com.trungdunghoang125.alpharadio.utils.Constants.EXO_PLAYER_PLAYER_STATUS_ACTION;
import static com.trungdunghoang125.alpharadio.utils.Constants.STATE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

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
import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioCacheDataSource;

/**
 * Created by trungdunghoang125 on 11/16/2022.
 */
public class RadioPlayerService extends Service {
    public static final String CHANNEL_ID = "channel1";
    public static final String ACTION_PREVIOUS = "action-previous";
    public static final String ACTION_PLAY = "action-play";
    public static final String ACTION_NEXT = "action-next";

    private static Bitmap image = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_radio);

    public static Notification notification;

    private final IBinder mIBinder = new ServiceBinder();

    private ExoPlayer player;

    private RadioStation station;

    private LocalBroadcastManager localBroadcast;

    @Override
    public void onCreate() {
        super.onCreate();
        player = new ExoPlayer.Builder(this).build();
        localBroadcast = LocalBroadcastManager.getInstance(this);
        createNotificationChannel();

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Intent intent = new Intent(EXO_PLAYER_PLAYER_STATUS_ACTION);
                if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    intent.putExtra(STATE, PlaybackStateCompat.STATE_BUFFERING);
                } else {
                    if (player.isPlaying()) {
                        intent.putExtra(STATE, PlaybackStateCompat.STATE_PLAYING);
                    } else {
                        intent.putExtra(STATE, PlaybackStateCompat.STATE_PAUSED);
                    }
                }
                localBroadcast.sendBroadcast(intent);
            }
        });
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

    private void playRadio(String url) {
        createNotification(this, station, R.drawable.ic_pause);
        Uri uri = Uri.parse(url);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        player.setPlayWhenReady(true);
        player.prepare();
    }

    public void playExoPlayer() {
        createNotification(this, station, R.drawable.ic_pause);
        player.setPlayWhenReady(true);
        player.prepare();
    }

    public void stopExoPlayer() {
        createNotification(this, station, R.drawable.ic_play);
        player.setPlayWhenReady(false);
        player.stop();
    }

    public void releaseExoPlayer() {
        player.release();
    }

    public boolean isPlaying() {
        if (player != null) {
            return player.isPlaying();
        }
        return false;
    }

    public void getCurrentStation(int position) {
        station = RadioCacheDataSource.cacheStations.get(position);
        String url = station.getUrl();
        playRadio(url);
    }

    public class ServiceBinder extends Binder {
        public RadioPlayerService getRadioPlayerService() {
            return RadioPlayerService.this;
        }
    }

    /**
     *  Notification
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void createNotification(Context context, RadioStation station, int playPauseDrawable) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSession = new MediaSessionCompat(context, "media-session");

            // set bitmap image for notification
            Glide.with(context)
                    .asBitmap()
                    .load(station.getFavicon())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            image = resource;
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_radio);
                        }
                    });

            // pending intent
            Intent intentPrev = new Intent(context, NotificationActionBroadcastReceiver.class)
                    .setAction(ACTION_PREVIOUS);
            PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(context, 0, intentPrev, PendingIntent.FLAG_IMMUTABLE);

            Intent intentPlay = new Intent(context, NotificationActionBroadcastReceiver.class)
                    .setAction(ACTION_PLAY);
            PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context, 0, intentPlay, PendingIntent.FLAG_IMMUTABLE);

            Intent intentNext = new Intent(context, NotificationActionBroadcastReceiver.class)
                    .setAction(ACTION_NEXT);
            PendingIntent pendingIntentNext = PendingIntent.getBroadcast(context, 0, intentNext, PendingIntent.FLAG_IMMUTABLE);

            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
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
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();
            notificationManagerCompat.notify(1, notification);
        }
    }
}