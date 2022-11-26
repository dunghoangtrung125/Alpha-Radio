package com.trungdunghoang125.alpharadio.viewmodel.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/25/2022.
 */
public class SearchViewModel extends ViewModel {

    private final MutableLiveData<List<RadioStation>> stationsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> showLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> hideLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();

    private final RadioRepository radioRepository;

    private final RadioCallback radioCallback = new RadioCallback();

    public SearchViewModel(RadioRepository radioRepository) {
        this.radioRepository = radioRepository;
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

    private void setStationsLiveData(List<RadioStation> stations) {
        setIsLoading(false);
        stationsLiveData.postValue(stations);
    }

    public void searchStation(String name) {
        setIsLoading(true);
        radioRepository.getSearchStationsResult(radioCallback, name);
    }

    private void setIsLoading(boolean loading) {
        if (loading) {
            showLoadingLiveData.postValue(null);
        } else {
            hideLoadingLiveData.postValue(null);
        }
    }

    public void addFavStation(RadioStation station) {
        radioRepository.addFavStation(station);
    }

    public void removeFavStation(RadioStation station) {
        radioRepository.removeFavStation(station);
    }

    /**
     * Call back inner class
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
