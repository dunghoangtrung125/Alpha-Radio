package com.trungdunghoang125.alpharadio.viewmodel.country;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trungdunghoang125.alpharadio.data.domain.Country;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/10/2022.
 */
public class CountryViewModel extends ViewModel {

    private final MutableLiveData<List<Country>> countriesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> showLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> hideLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();

    private final RadioRepository radioRepository;

    private final RadioCallback radioCallback = new RadioCallback();

    public CountryViewModel(RadioRepository radioRepository) {
        this.radioRepository = radioRepository;
    }

    public LiveData<List<Country>> getCountriesLiveData() {
        return countriesLiveData;
    }

    public MutableLiveData<Void> getShowLoadingLiveData() {
        return showLoadingLiveData;
    }

    public MutableLiveData<Void> getHideLoadingLiveData() {
        return hideLoadingLiveData;
    }

    public MutableLiveData<String> getErrorMessageLiveData() {
        return errorMessageLiveData;
    }

    public void getCountries() {
        setIsLoading(true);
        radioRepository.getCountries(radioCallback);
    }

    private void setCountriesLiveData(List<Country> countries) {
        setIsLoading(false);
        countriesLiveData.postValue(countries);
    }

    private void setIsLoading(boolean loading) {
        if (loading) {
            showLoadingLiveData.postValue(null);
        } else {
            hideLoadingLiveData.postValue(null);
        }
    }

    /**
     * Callback class
     */
    private class RadioCallback implements RadioRepository.LoadCountriesCallback {

        @Override
        public void onCountriesLoad(List<Country> countries) {
            setCountriesLiveData(countries);
        }

        @Override
        public void onDataLoadFailed() {
            errorMessageLiveData.postValue("Server error. Try again!");
        }

        @Override
        public void onError() {
            errorMessageLiveData.postValue("Something went wrong!");
        }
    }
}
