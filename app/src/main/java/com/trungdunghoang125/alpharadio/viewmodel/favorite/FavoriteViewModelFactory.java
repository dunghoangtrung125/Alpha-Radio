package com.trungdunghoang125.alpharadio.viewmodel.favorite;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

/**
 * Created by trungdunghoang125 on 11/26/2022.
 */
public class FavoriteViewModelFactory implements ViewModelProvider.Factory {
    private final RadioRepository radioRepository;

    public FavoriteViewModelFactory(RadioRepository radioRepository) {
        this.radioRepository = radioRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FavoriteViewModel.class)) {
            return (T) new FavoriteViewModel(radioRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
