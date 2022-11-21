package com.trungdunghoang125.alpharadio.service;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static com.trungdunghoang125.alpharadio.utils.Constants.EXO_PLAYER_PLAYER_STATUS_ACTION;
import static com.trungdunghoang125.alpharadio.utils.Constants.STATE;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioCacheDataSource;
import com.trungdunghoang125.alpharadio.ui.activity.RadioPlayerActivity;

/**
 * Created by trungdunghoang125 on 11/16/2022.
 */
public class RadioPlayerService extends Service {

    private final IBinder mIBinder = new ServiceBinder();

    private ExoPlayer player;

    private PlayerNotificationManager playerNotificationManager;

    private RadioStation station;

    private Bitmap image = null;

    private LocalBroadcastManager localBroadcast;

    // MediaSession
    private MediaSessionCompat mediaSession;
    private MediaSessionConnector mediaSessionConnector;

    @Override
    public void onCreate() {
        super.onCreate();
        player = new ExoPlayer.Builder(this).build();
        localBroadcast = LocalBroadcastManager.getInstance(this);

        // MediaSession
        mediaSession = new MediaSessionCompat(this, "sample");
        mediaSession.setActive(true);
        mediaSessionConnector = new MediaSessionConnector(mediaSession);
        mediaSessionConnector.setPlayer(player);

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
        playerNotificationManager.setPlayer(null);
    }

    private void playRadio(String url) {
        Uri uri = Uri.parse(url);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        createPlayerNotificationManager();
        player.setPlayWhenReady(true);
        player.prepare();
    }

    // set notification description
    PlayerNotificationManager.MediaDescriptionAdapter descriptionAdapter = new PlayerNotificationManager.MediaDescriptionAdapter() {
        @Override
        public CharSequence getCurrentContentTitle(Player player) {
            return station.getName();
        }

        @Nullable
        @Override
        public PendingIntent createCurrentContentIntent(Player player) {
            Intent intent = new Intent(getApplicationContext(), RadioPlayerActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
            return pendingIntent;
        }

        @Nullable
        @Override
        public CharSequence getCurrentContentText(Player player) {
            return null;
        }

        @Nullable
        @Override
        public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
            Glide.with(getApplicationContext()).asBitmap().load(station.getFavicon()).error(R.drawable.ic_radio).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    image = resource;
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
            return image;
        }
    };

    private void createPlayerNotificationManager() {
        String channelId = getResources().getString(R.string.app_name) + " radio channel";
        int notificationId = 1811;
        playerNotificationManager = new PlayerNotificationManager.Builder(this, notificationId, channelId)
                .setMediaDescriptionAdapter(descriptionAdapter)
                .setChannelImportance(IMPORTANCE_DEFAULT)
                .setSmallIconResourceId(R.drawable.ic_radio)
                .setPlayActionIconResourceId(R.drawable.ic_play)
                .setPauseActionIconResourceId(R.drawable.ic_pause)
                .setNextActionIconResourceId(R.drawable.ic_skip_next)
                .setPreviousActionIconResourceId(R.drawable.ic_skip_previous)
                .setChannelDescriptionResourceId(R.string.app_name)
                .setChannelNameResourceId(R.string.app_name)
                .build();

        playerNotificationManager.setPriority(NotificationCompat.PRIORITY_MAX);
        playerNotificationManager.setUseRewindAction(false);
        playerNotificationManager.setUseFastForwardAction(false);
        playerNotificationManager.setPlayer(player);
        playerNotificationManager.setMediaSessionToken();
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
        if (mediaSession != null) {
            mediaSession.release();
        }
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
}