package com.trungdunghoang125.alpharadio.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.trungdunghoang125.alpharadio.databinding.ActivityLanguageBinding;

public class LanguageActivity extends AppCompatActivity {

    private ActivityLanguageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LanguageActivity.class);
        context.startActivity(starter);
    }
}