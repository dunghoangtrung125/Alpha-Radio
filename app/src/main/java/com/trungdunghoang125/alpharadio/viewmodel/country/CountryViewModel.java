package com.trungdunghoang125.alpharadio.viewmodel.country;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trungdunghoang125.alpharadio.data.model.Country;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/10/2022.
 */
public class CountryViewModel extends ViewModel {
    private final RadioRepository radioRepository;

    private final MutableLiveData<List<Country>> countriesLiveData = new MutableLiveData<>();

    private final RadioCallback radioCallback = new RadioCallback();

    public CountryViewModel(RadioRepository radioRepository) {
        this.radioRepository = radioRepository;
    }

    public LiveData<List<Country>> getCountriesLiveData() {
        return countriesLiveData;
    }

    public void getCountries() {
        radioRepository.getCountries(radioCallback);
    }

    private void setCountriesLiveData(List<Country> countries) {
        countriesLiveData.postValue(countries);
    }

    private class RadioCallback implements RadioRepository.LoadCountriesCallback {

        @Override
        public void onCountriesLoad(List<Country> countries) {
            setCountriesLiveData(countries);
        }

        @Override
        public void onDataLoadFailed() {

        }

        @Override
        public void onError() {

        }
    }
}
