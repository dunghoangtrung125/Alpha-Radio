package com.trungdunghoang125.alpharadio.viewmodel.home;

import androidx.lifecycle.ViewModel;

import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

/**
 * Created by trungdunghoang125 on 11/4/2022.
 */
public class HomeViewModel extends ViewModel {
    private final RadioRepository radioRepository;

    public HomeViewModel(RadioRepository radioRepository) {
        this.radioRepository = radioRepository;
    }
}
