package com.trungdunghoang125.alpharadio.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.databinding.ActivityRadioPlayerBinding;
import com.trungdunghoang125.alpharadio.service.RadioPlayerService;
import com.trungdunghoang125.alpharadio.utils.Constants;

public class RadioPlayerActivity extends AppCompatActivity implements ServiceConnection {

    private ActivityRadioPlayerBinding binding;

    private ImageView mImageHidePlayer;

    private ImageView mImageRadioStationPlayer;

    private ImageView mButtonPrevious;

    private ImageView mButtonNext;

    private FloatingActionButton mButtonPlayPause;

    private TextView mRadioPlayerTitle;

    private TextView mRadioPlayerTag;

    private String urlStream = "";

    private RadioPlayerService mRadioService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRadioPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mImageRadioStationPlayer = binding.imageRadioStationPlayer;
        mImageHidePlayer = binding.imageHidePlayer;
        mButtonPlayPause = binding.buttonPlayPause;
        mButtonPrevious = binding.buttonPrevious;
        mButtonNext = binding.buttonNext;
        mRadioPlayerTitle = binding.textRadioPlayerTitle;
        mRadioPlayerTag = binding.textRadioPlayerTags;

        mImageHidePlayer.setOnClickListener(view -> {
            Intent intent = new Intent(this, RadioPlayerService.class);
            stopService(intent);
            finish();
        });

        // Get intent object data
        Intent stationDataIntent = getIntent();
        RadioStation station = stationDataIntent.getParcelableExtra(Constants.RADIO_STATION_EXTRA);
        setMetaData(station);
        buttonPlayPauseClickedListener();
        // set urlStream data
        urlStream = station.getUrl();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, RadioPlayerService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        unbindService(this);
//    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        RadioPlayerService.ServiceBinder serviceBinder = (RadioPlayerService.ServiceBinder) service;
        mRadioService = serviceBinder.getRadioPlayerService();
        // play audio right after the activity start
        if (mRadioService.isPlaying()) {
            mRadioService.releaseExoPlayer();
        }
        mRadioService.initializePlayer(urlStream);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mRadioService = null;
    }

    private void buttonPlayPauseClickedListener() {
        mButtonPlayPause.setOnClickListener(view -> {
            if (!mRadioService.isPlaying()) {
                mButtonPlayPause.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_pause));
                mRadioService.playExoPlayer();
            } else {
                mButtonPlayPause.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_play));
                mRadioService.stopExoPlayer();
            }
        });
    }

    private void setMetaData(RadioStation station) {
        Glide.with(this)
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.ic_radio))
                .load(station.getFavicon())
                .into(mImageRadioStationPlayer);
        mRadioPlayerTitle.setText(station.getName());
        mRadioPlayerTag.setText(station.getTags());
    }

    public static void start(Context context, RadioStation station) {
        Intent starter = new Intent(context, RadioPlayerActivity.class);
        starter.putExtra(Constants.RADIO_STATION_EXTRA, station);
        context.startActivity(starter);
    }
}