package com.trungdunghoang125.alpharadio.viewmodel.countrydetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/10/2022.
 */
public class CountryDetailViewModel extends ViewModel {

    private final MutableLiveData<List<RadioStation>> stationsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> showLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> hideLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();

    private final RadioRepository repository;

    private RadioCallback radioCallback = new RadioCallback();

    public CountryDetailViewModel(RadioRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<RadioStation>> getStationsLiveData() {
        return stationsLiveData;
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

    public void getStations(String countryCode) {
        setIsLoading(true);
        repository.getStations(radioCallback, countryCode);
    }

    private void setStationsLiveData(List<RadioStation> stations) {
        setIsLoading(false);
        stationsLiveData.postValue(stations);
    }

    private void setIsLoading(boolean loading) {
        if (loading) {
            showLoadingLiveData.postValue(null);
        } else {
            hideLoadingLiveData.postValue(null);
        }
    }

    /**
     * Callback
     */
    private class RadioCallback implements RadioRepository.LoadStationsCallback {

        @Override
        public void onStationsLoad(List<RadioStation> stations) {
            setStationsLiveData(stations);
        }

        @Override
        public void onDataLoadFailed() {
            errorMessageLiveData.postValue("Can not load any item. Try again!");
        }

        @Override
        public void onError() {
            errorMessageLiveData.postValue("Something went wrong!");
        }
    }
}
