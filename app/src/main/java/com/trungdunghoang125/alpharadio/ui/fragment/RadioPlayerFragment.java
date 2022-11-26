package com.trungdunghoang125.alpharadio.ui.fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.ACTION_NEXT;
import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.ACTION_PLAY;
import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.ACTION_PREVIOUS;
import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.ACTION_STOP;
import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.RADIO_LAST_PLAYED;
import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.SEND_DATA_TO_PLAYER;
import static com.trungdunghoang125.alpharadio.utils.Constants.EXO_PLAYER_PLAYER_STATUS_ACTION;
import static com.trungdunghoang125.alpharadio.utils.Constants.STATE;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.databinding.FragmentRadioPlayerBinding;
import com.trungdunghoang125.alpharadio.service.RadioPlayerService;

public class RadioPlayerFragment extends Fragment implements ServiceConnection {

    private FragmentRadioPlayerBinding binding;

    private ImageView mMiniPlayerStationImage, mFullPlayerStationImage;

    private TextView mMiniPlayerStationTitle, mMiniPlayerStationCountry, mFullPlayerStationTitle, mFullPlayerStationTag;

    private ImageButton mMiniPlayerBtnPlayPause, mFullPlayerBtnPlayPause, mFullPlayerBtnNext, mFullPlayerBtnPrevious;

    private ProgressBar mFullPlayerLoading;

    private static ConstraintLayout mLayoutMiniPlayer;

    private FrameLayout mLayoutBottomSheet;

    private BottomSheetBehavior mBottomSheetBehavior;

    private LinearLayout mLayoutFullScreenPlayer;

    private CheckBox mMiniPlayerCbFavStation;

    private RadioPlayerService mRadioService;

    private RadioStation station;

    private boolean isPlaying;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == EXO_PLAYER_PLAYER_STATUS_ACTION) {
                int playerState = intent.getIntExtra(STATE, 0);
                if (playerState == PlaybackStateCompat.STATE_BUFFERING) {
                    mFullPlayerLoading.setVisibility(View.VISIBLE);
                } else {
                    mFullPlayerLoading.setVisibility(View.GONE);
                }
            }

            if (intent.getAction() == SEND_DATA_TO_PLAYER) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    station = bundle.getParcelable("station");
                    isPlaying = bundle.getBoolean("isPlaying");
                    String action = bundle.getString("action");
                    handleAction(action);
                }
            }
        }
    };

    public RadioPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        RadioPlayerService.ServiceBinder serviceBinder = (RadioPlayerService.ServiceBinder) service;
        mRadioService = serviceBinder.getRadioPlayerService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRadioPlayerBinding.inflate(inflater, container, false);

        // Data binding for mini player
        mMiniPlayerStationImage = binding.layoutMiniPlayer.imageMiniPlayer;
        mMiniPlayerStationTitle = binding.layoutMiniPlayer.textMiniPlayerStationName;
        mMiniPlayerStationCountry = binding.layoutMiniPlayer.textMiniPlayerStationCountry;
        mMiniPlayerBtnPlayPause = binding.layoutMiniPlayer.buttonPlayMiniPlayer;
        mMiniPlayerCbFavStation = binding.layoutMiniPlayer.cbFavStationMiniPlayer;
        mLayoutMiniPlayer = binding.layoutMiniPlayer.constraintMiniPlayer;
        mLayoutBottomSheet = binding.bottomNavigationContainer;
        mBottomSheetBehavior = BottomSheetBehavior.from(mLayoutBottomSheet);

        mBottomSheetBehavior.setDraggable(false);

        // Data binding for full screen player
        mLayoutFullScreenPlayer = binding.layoutFullPlayer.layoutRadioFullScreen;
        mFullPlayerBtnNext = binding.layoutFullPlayer.buttonNext;
        mFullPlayerBtnPlayPause = binding.layoutFullPlayer.buttonPlayPause;
        mFullPlayerBtnPrevious = binding.layoutFullPlayer.buttonPrevious;
        mFullPlayerStationTitle = binding.layoutFullPlayer.textRadioPlayerTitle;
        mFullPlayerStationTag = binding.layoutFullPlayer.textRadioPlayerTags;
        mFullPlayerStationImage = binding.layoutFullPlayer.imageRadioStationPlayer;
        mFullPlayerLoading = binding.layoutFullPlayer.progressBarRadioLoading;

        getDataFromSharedPreferences();

        // listener for playPause button
        mMiniPlayerBtnPlayPause.setOnClickListener(buttonView -> {
            playPauseButtonClicked();
        });

        mFullPlayerBtnPlayPause.setOnClickListener(buttonView -> {
            playPauseButtonClicked();
        });

        mFullPlayerBtnPrevious.setOnClickListener(buttonView -> {
            mRadioService.playPreviousRadioStation();
            setMetaData(station);
        });

        mFullPlayerBtnNext.setOnClickListener(buttonView -> {
            mRadioService.playNextRadioStation();
            setMetaData(station);
        });

        mLayoutMiniPlayer.setOnClickListener(view -> {
            if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                expandedMusicPlayer();
            } else {
                hideMusicPlayer();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SEND_DATA_TO_PLAYER);
        filter.addAction(EXO_PLAYER_PLAYER_STATUS_ACTION);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, filter);
        Intent intent = new Intent(requireContext(), RadioPlayerService.class);
        requireContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(requireContext(), RadioPlayerService.class);
        requireContext().stopService(intent);
        // unbind service
        requireContext().unbindService(this);
    }

    private void expandedMusicPlayer() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mLayoutFullScreenPlayer.setVisibility(View.VISIBLE);
        mLayoutMiniPlayer.setAlpha(0);
        // Disable button of mini player
        mMiniPlayerBtnPlayPause.setClickable(false);
        mMiniPlayerCbFavStation.setClickable(false);
    }

    private void hideMusicPlayer() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mLayoutFullScreenPlayer.setVisibility(View.GONE);
        mLayoutMiniPlayer.setAlpha(1);
        mMiniPlayerBtnPlayPause.setClickable(true);
        mMiniPlayerCbFavStation.setClickable(true);
    }

    private void playPauseButtonClicked() {
        if (!isPlaying) {
            if (mRadioService.currentStation == null) {
                mRadioService.getStationDataFromPreferences();
                mRadioService.initRadioFromPreferences();
            }
            mRadioService.startExoPlayer();
        } else {
            mRadioService.stopExoPlayer();
        }
    }

    private void getDataFromSharedPreferences() {
        SharedPreferences preferences = getContext().getSharedPreferences(RADIO_LAST_PLAYED, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("Station", null);
        if (json != null) {
            station = gson.fromJson(json, RadioStation.class);
            setMetaData(station);
        }
    }

    private void setMetaData(RadioStation station) {
        if (station == null) {
            return;
        }
        // meta data for mini player
        mMiniPlayerStationTitle.setText(station.getName());
        mMiniPlayerStationCountry.setText(station.getCountry());
        Glide.with(requireContext())
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.ic_radio))
                .load(station.getFavicon())
                .into(mMiniPlayerStationImage);
        // metadata for full screen player
        Glide.with(requireContext())
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.ic_radio))
                .load(station.getFavicon())
                .into(mFullPlayerStationImage);
        mFullPlayerStationTitle.setText(station.getName());
        if (station.getTags().length() >= 40) {
            mFullPlayerStationTag.setText(station.getTags().substring(0, 40) + "...");
        } else {
            mFullPlayerStationTag.setText(station.getTags());
        }
    }

    private void handleAction(String action) {
        setMetaData(station);
        switch (action) {
            case ACTION_PLAY:
            case ACTION_STOP:
                setPlayPauseBtn();
                break;
            case ACTION_NEXT:
            case ACTION_PREVIOUS:
                break;
        }
    }

    private void setPlayPauseBtn() {
        if (isPlaying) {
            mMiniPlayerBtnPlayPause.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_pause));
            mFullPlayerBtnPlayPause.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_pause));
        } else {
            mMiniPlayerBtnPlayPause.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_play));
            mFullPlayerBtnPlayPause.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_play));
        }
    }
}