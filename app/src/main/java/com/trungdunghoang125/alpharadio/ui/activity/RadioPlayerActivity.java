package com.trungdunghoang125.alpharadio.ui.activity;

import static com.trungdunghoang125.alpharadio.service.RadioBroadcastReceiver.ACTION_NAME;
import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.ACTION_NEXT;
import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.ACTION_PLAY;
import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.ACTION_PREVIOUS;
import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.getStationCache;
import static com.trungdunghoang125.alpharadio.utils.Constants.STATE;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioCacheDataSource;
//import com.trungdunghoang125.alpharadio.databinding.ActivityRadioPlayerBinding;
import com.trungdunghoang125.alpharadio.service.RadioBroadcastReceiver;
import com.trungdunghoang125.alpharadio.service.RadioPlayerService;
import com.trungdunghoang125.alpharadio.utils.Constants;

public class RadioPlayerActivity extends AppCompatActivity implements ServiceConnection {

   // private ActivityRadioPlayerBinding binding;

    private ImageView mButtonHidePlayer;

    private ImageView mImageRadioStationPlayer;

    private ImageView mButtonPrevious;

    private ImageView mButtonNext;

    private FloatingActionButton mButtonPlayPause;

    private ProgressBar mProgressBarRadioLoading;

    private TextView mRadioPlayerTitle;

    private TextView mRadioPlayerTag;

    private RadioPlayerService mRadioService;

    private BroadcastReceiver broadcastReceiver;

    private int position;

    private final BroadcastReceiver broadcastReceiverNotificationAction = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(ACTION_NAME);

            switch (action) {
                case ACTION_PREVIOUS:
                    buttonPreviousClicked();
                    break;
                case ACTION_PLAY:
                    buttonPlayPauseClicked();
                    break;
                case ACTION_NEXT:
                    buttonNextClicked();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityRadioPlayerBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        getStationCache();
//        mImageRadioStationPlayer = binding.imageRadioStationPlayer;
//        mButtonHidePlayer = binding.imageHidePlayer;
//        mButtonPlayPause = binding.buttonPlayPause;
//        mProgressBarRadioLoading = binding.progressBarRadioLoading;
//        mButtonPrevious = binding.buttonPrevious;
//        mButtonNext = binding.buttonNext;
//        mRadioPlayerTitle = binding.textRadioPlayerTitle;
//        mRadioPlayerTag = binding.textRadioPlayerTags;

        mButtonHidePlayer.setOnClickListener(view -> {
            finish();
        });

        // Get intent object data
        Intent stationDataIntent = getIntent();
        position = stationDataIntent.getIntExtra(Constants.RADIO_STATION_EXTRA, 0);
        RadioStation station = RadioCacheDataSource.cacheStations.get(position);
        setMetaData(station);

        // listener
        mButtonPlayPause.setOnClickListener(view -> {
            buttonPlayPauseClicked();
        });
        mButtonPrevious.setOnClickListener(view -> {
            buttonPreviousClicked();
        });
        mButtonNext.setOnClickListener(view -> {
            buttonNextClicked();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, RadioPlayerService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        startService(intent);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(Constants.EXO_PLAYER_PLAYER_STATUS_ACTION));
        // register broadcast receiver for notification action
        registerReceiver(broadcastReceiverNotificationAction, new IntentFilter(RadioBroadcastReceiver.NOTIFICATION_CONTROL_ACTION));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRadioService != null) {
            if (mRadioService.isPlaying()) {
                mButtonPlayPause.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_pause));
            } else {
                mButtonPlayPause.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_play));
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        }
        unregisterReceiver(broadcastReceiverNotificationAction);
        super.onDestroy();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        RadioPlayerService.ServiceBinder serviceBinder = (RadioPlayerService.ServiceBinder) service;
        mRadioService = serviceBinder.getRadioPlayerService();
        // play audio right after the activity start
        if (mRadioService.isPlaying()) {
            mRadioService.stopExoPlayer();
        }
        mRadioService.getCurrentStation(position);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mRadioService = null;
    }

    private void buttonPlayPauseClicked() {
        if (!mRadioService.isPlaying()) {
            mRadioService.playExoPlayer();
        } else {
            mRadioService.stopExoPlayer();
        }
    }

    private void buttonPreviousClicked() {
        mRadioService.playPreviousRadioStation();
    }

    private void buttonNextClicked() {
        mRadioService.playNextRadioStation();
    }

    private void setMetaData(RadioStation station) {
        Glide.with(this).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_radio)).load(station.getFavicon()).into(mImageRadioStationPlayer);
        mRadioPlayerTitle.setText(station.getName());
        mRadioPlayerTag.setText(station.getTags());
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int playerState = intent.getIntExtra(STATE, 0);
                if (playerState == PlaybackStateCompat.STATE_BUFFERING) {
                    mProgressBarRadioLoading.setVisibility(View.VISIBLE);
                } else {
                    mProgressBarRadioLoading.setVisibility(View.GONE);
                    if (playerState == PlaybackStateCompat.STATE_PLAYING) {
                        mButtonPlayPause.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_pause));
                    }
                    if (playerState == PlaybackStateCompat.STATE_PAUSED) {
                        mButtonPlayPause.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_play));
                    }
                }
            }
        };
    }

    // starter for activity
    public static void start(Context context, int position) {
        Intent starter = new Intent(context, RadioPlayerActivity.class);
        starter.putExtra(Constants.RADIO_STATION_EXTRA, position);
        context.startActivity(starter);
    }
}