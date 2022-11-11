package com.trungdunghoang125.alpharadio.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.databinding.ActivityRadioPlayerBinding;

public class RadioPlayerActivity extends AppCompatActivity {

    private ActivityRadioPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRadioPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RadioStation station = getIntent().getParcelableExtra("TAG");
        Log.d("tranle1811", "onCreate: " + station.getName());
    }

    public static void start(Context context, RadioStation station) {
        Intent starter = new Intent(context, RadioPlayerActivity.class);
        starter.putExtra("TAG", station);
        context.startActivity(starter);
    }
}