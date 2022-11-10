package com.trungdunghoang125.alpharadio.viewmodel.countrydetail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

/**
 * Created by trungdunghoang125 on 11/10/2022.
 */
public class CountryDetailViewModelFactory implements ViewModelProvider.Factory {
    private final RadioRepository radioRepository;

    public CountryDetailViewModelFactory(RadioRepository radioRepository) {
        this.radioRepository = radioRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CountryDetailViewModel.class)) {
            return (T) new CountryDetailViewModel(radioRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
