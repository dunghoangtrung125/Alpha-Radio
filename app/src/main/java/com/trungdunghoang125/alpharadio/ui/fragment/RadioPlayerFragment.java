package com.trungdunghoang125.alpharadio.ui.fragment;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.MODE_PRIVATE;
import static com.trungdunghoang125.alpharadio.service.RadioBroadcastReceiver.ACTION_NAME;
import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.ACTION_PLAY;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.databinding.FragmentRadioPlayerBinding;
import com.trungdunghoang125.alpharadio.service.RadioBroadcastReceiver;
import com.trungdunghoang125.alpharadio.service.RadioPlayerService;

public class RadioPlayerFragment extends Fragment implements ServiceConnection {
    private FragmentRadioPlayerBinding binding;

    private ImageView mImageStation;

    private TextView mTextStationTitle;

    private TextView mTextStationCountry;

    private ImageButton mButtonPlayPause;

    private RadioPlayerService mRadioService;

    private ConstraintLayout constraintLayoutMini;

    private FrameLayout layoutBottomSheet;

    private BottomSheetBehavior bottomSheetBehavior;

    LinearLayout radioPlayerFullScreen;

    private RadioStation station;

    private boolean isPlay = false;

    private final BroadcastReceiver broadcastReceiverNotificationAction = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(ACTION_NAME);

            switch (action) {
                case ACTION_PLAY:
                    //playPauseButtonClicked();
                    break;
            }
        }
    };

    public RadioPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRadioPlayerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mImageStation = binding.layoutMiniPlayer.imageMiniPlayer;
        mTextStationTitle = binding.layoutMiniPlayer.textMiniPlayerStationName;
        mTextStationCountry = binding.layoutMiniPlayer.textMiniPlayerStationCountry;
        mButtonPlayPause = binding.layoutMiniPlayer.buttonPlayMiniPlayer;
        radioPlayerFullScreen = binding.layoutFullScreenPlayer.layoutRadioFullScreen;


        constraintLayoutMini = binding.layoutMiniPlayer.constraintMiniPlayer;
        layoutBottomSheet = binding.bottomNavigationContainer;
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        // listener for playPause button
        mButtonPlayPause.setOnClickListener(buttonView -> {
            playPauseButtonClicked();
        });

        constraintLayoutMini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    radioPlayerFullScreen.setVisibility(View.VISIBLE);
                    constraintLayoutMini.setAlpha(0);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    radioPlayerFullScreen.setVisibility(View.GONE);
                    constraintLayoutMini.setAlpha(1);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getContext(), RadioPlayerService.class);
        getContext().bindService(intent, this, BIND_AUTO_CREATE);
        getContext().registerReceiver(broadcastReceiverNotificationAction, new IntentFilter(RadioBroadcastReceiver.NOTIFICATION_CONTROL_ACTION));
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