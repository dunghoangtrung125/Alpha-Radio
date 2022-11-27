package com.trungdunghoang125.alpharadio.viewmodel.radioplayer;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

/**
 * Created by trungdunghoang125 on 11/26/2022.
 */
public class RadioPlayerViewModelFactory implements ViewModelProvider.Factory {
    private final RadioRepository radioRepository;

    public RadioPlayerViewModelFactory(RadioRepository radioRepository) {
        this.radioRepository = radioRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RadioPlayerViewModel.class)) {
            return (T) new RadioPlayerViewModel(radioRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}