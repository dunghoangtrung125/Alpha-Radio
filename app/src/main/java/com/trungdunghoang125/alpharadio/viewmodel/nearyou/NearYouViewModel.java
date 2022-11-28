package com.trungdunghoang125.alpharadio.viewmodel.nearyou;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

import java.util.List;

public class NearYouViewModel extends ViewModel {
    private final MutableLiveData<List<RadioStation>> nearYoustationsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> showLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> hideLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();

    private final RadioRepository repository;

    private RadioCallback radioCallback = new RadioCallback();

    public NearYouViewModel(RadioRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<List<RadioStation>> getNearYoustationsLiveData() {
        return nearYoustationsLiveData;
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
        nearYoustationsLiveData.postValue(stations);
    }

    public void getStationsInYourLocation(String countryCode) {
        setIsLoading(true);
        repository.getStations(radioCallback, countryCode);
    }

    private void setIsLoading(boolean loading) {
        if (loading) {
            showLoadingLiveData.postValue(null);
        } else {
            hideLoadingLiveData.postValue(null);
        }
    }

    public void addFavStation(RadioStation station) {
        repository.addFavStation(station);
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
