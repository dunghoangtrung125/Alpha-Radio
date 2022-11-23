package com.trungdunghoang125.alpharadio.ui.fragment;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.MODE_PRIVATE;
import static com.trungdunghoang125.alpharadio.service.NotificationActionBroadcastReceiver.ACTION_NAME;
import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.ACTION_NEXT;
import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.ACTION_PLAY;
import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.ACTION_PREVIOUS;
import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.RADIO_LAST_PLAYED;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.databinding.FragmentMiniPlayerBinding;
import com.trungdunghoang125.alpharadio.service.NotificationActionBroadcastReceiver;
import com.trungdunghoang125.alpharadio.service.RadioPlayerService;

public class MiniPlayerFragment extends Fragment implements ServiceConnection {
    private FragmentMiniPlayerBinding binding;

    private ImageView mImageStation;

    private TextView mTextStationTitle;

    private TextView mTextStationCountry;

    private ImageButton mButtonPlayPause;

    private RadioPlayerService mRadioService;

    private RadioStation station;

    private final BroadcastReceiver broadcastReceiverNotificationAction = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(ACTION_NAME);

            switch (action) {
                case ACTION_PREVIOUS:
                case ACTION_NEXT:
                    break;
                case ACTION_PLAY:
                    playPauseButtonClicked();
                    break;
            }
        }
    };

    public MiniPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMiniPlayerBinding.inflate(inflater, container, false);
        mImageStation = binding.imageMiniPlayer;
        mTextStationTitle = binding.textMiniPlayerStationName;
        mTextStationCountry = binding.textMiniPlayerStationCountry;
        mButtonPlayPause = binding.buttonPlayMiniPlayer;
        View view = binding.getRoot();

        // listener for playPause button
        mButtonPlayPause.setOnClickListener(buttonView -> {
            playPauseButtonClicked();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getContext(), RadioPlayerService.class);
        getContext().bindService(intent, this, BIND_AUTO_CREATE);
        getContext().registerReceiver(broadcastReceiverNotificationAction, new IntentFilter(NotificationActionBroadcastReceiver.NOTIFICATION_CONTROL_ACTION));
    }

    @Override
    public void onResume() {
        super.onResume();
        setMetaData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(broadcastReceiverNotificationAction);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        RadioPlayerService.ServiceBinder serviceBinder = (RadioPlayerService.ServiceBinder) service;
        mRadioService = serviceBinder.getRadioPlayerService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mRadioService = null;
    }

    private void playPauseButtonClicked() {
        if (!mRadioService.isPlaying()) {
            if (mRadioService.station == null) {
                mRadioService.getStationDataFromPreferences();
            } else {
                mRadioService.playExoPlayer();
            }
        } else {
            mRadioService.stopExoPlayer();
        }
    }

    private void setMetaData() {
        SharedPreferences preferences = getContext().getSharedPreferences(RADIO_LAST_PLAYED, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("Station", null);
        if (json != null) {
            station = gson.fromJson(json, RadioStation.class);
            Glide.with(this).load(station.getFavicon()).into(mImageStation);
            mTextStationTitle.setText(station.getName());
            mTextStationCountry.setText(station.getCountry());
        }
        // set icon for playPause Button
        if (mRadioService != null) {
            if (mRadioService.isPlaying()) {
                mButtonPlayPause.setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.ic_pause));
            } else {
                mButtonPlayPause.setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.ic_play));
            }
        }
    }
}