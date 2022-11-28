package com.trungdunghoang125.alpharadio.viewmodel.nearyou;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

public class NearYouViewModelFactory implements ViewModelProvider.Factory {
    private final RadioRepository radioRepository;

    public NearYouViewModelFactory(RadioRepository radioRepository) {
        this.radioRepository = radioRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NearYouViewModel.class)) {
            return (T) new NearYouViewModel(radioRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
