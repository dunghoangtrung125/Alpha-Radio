package com.trungdunghoang125.alpharadio.viewmodel.country;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

/**
 * Created by trungdunghoang125 on 11/10/2022.
 */
public class CountryViewModelFactory implements ViewModelProvider.Factory {
    private final RadioRepository radioRepository;

    public CountryViewModelFactory(RadioRepository radioRepository) {
        this.radioRepository = radioRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CountryViewModel.class)) {
            return (T) new CountryViewModel(radioRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
