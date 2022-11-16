package com.trungdunghoang125.alpharadio.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class RadioPlayerActivity extends AppCompatActivity {

    private static final String RADIO_STATION_EXTRA = "radioStationCode";

    private ActivityRadioPlayerBinding binding;

    private ImageView mImageHidePlayer;

    private ImageView mImageRadioStationPlayer;

    private FloatingActionButton buttonPlayPause;

    private TextView mRadioPlayerTitle;

    private TextView mRadioPlayerTag;

    private boolean play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRadioPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mImageRadioStationPlayer = binding.imageRadioStationPlayer;
        mImageHidePlayer = binding.imageHidePlayer;
        buttonPlayPause = binding.buttonPlayPause;
        mRadioPlayerTitle = binding.textRadioPlayerTitle;
        mRadioPlayerTag = binding.textRadioPlayerTags;

        mImageHidePlayer.setOnClickListener(view -> {
            finish();
        });

        play = true;
        buttonPlayPause.setOnClickListener(view -> {
            if (!play) {
                buttonPlayPause.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_pause));
                play = true;
            } else {
                buttonPlayPause.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_play));
                play = false;
            }
        });

        // Get intent object data
        RadioStation station = (RadioStation) getIntent().getParcelableExtra(RADIO_STATION_EXTRA);
        setDataForPlayerScreen(station);
    }

    private void setDataForPlayerScreen(RadioStation station) {
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
        starter.putExtra(RADIO_STATION_EXTRA, station);
        context.startActivity(starter);
    }
}