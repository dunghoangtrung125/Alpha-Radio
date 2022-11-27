package com.trungdunghoang125.alpharadio.viewmodel.radioplayer;

import androidx.lifecycle.ViewModel;

import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

/**
 * Created by trungdunghoang125 on 11/26/2022.
 */
public class RadioPlayerViewModel extends ViewModel {
    private final RadioRepository repository;

    public RadioPlayerViewModel(RadioRepository repository) {
        this.repository = repository;
    }

    public void addFavStation(RadioStation station) {
        repository.addFavStation(station);
    }
}
