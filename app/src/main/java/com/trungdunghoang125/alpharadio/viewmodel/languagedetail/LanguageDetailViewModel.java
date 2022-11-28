package com.trungdunghoang125.alpharadio.viewmodel.languagedetail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trungdunghoang125 on 11/28/2022.
 */
public class LanguageDetailViewModel extends ViewModel {
    private final MutableLiveData<List<RadioStation>> stationsByLanguageLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> showLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> hideLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<RadioStation>> radioFilterLiveData = new MutableLiveData<>();
    private final RadioRepository repository;
    private List<RadioStation> resultList = new ArrayList<>();
    private final RadioCallback callback = new RadioCallback();

    public LanguageDetailViewModel(RadioRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<List<RadioStation>> getStationsByLanguageLiveData() {
        return stationsByLanguageLiveData;
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

    public void getStations(String language) {
        setIsLoading(true);
        repository.getStationsByLanguage(callback, language);
    }

    public void addFavStation(RadioStation station) {
        repository.addFavStation(station);
    }

    private void setStationsLiveData(List<RadioStation> stations) {
        setIsLoading(false);
        stationsByLanguageLiveData.postValue(stations);
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
            errorMessageLiveData.postValue("Server error. Try again!");
        }

        @Override
        public void onError() {
            errorMessageLiveData.postValue("Something went wrong!");
        }
    }
}
