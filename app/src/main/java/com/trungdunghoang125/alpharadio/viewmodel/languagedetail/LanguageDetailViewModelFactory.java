package com.trungdunghoang125.alpharadio.viewmodel.languagedetail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

/**
 * Created by trungdunghoang125 on 11/28/2022.
 */
public class LanguageDetailViewModelFactory implements ViewModelProvider.Factory {
    private final RadioRepository radioRepository;

    public LanguageDetailViewModelFactory(RadioRepository radioRepository) {
        this.radioRepository = radioRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LanguageDetailViewModel.class)) {
            return (T) new LanguageDetailViewModel(radioRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
