package com.trungdunghoang125.alpharadio.viewmodel.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/4/2022.
 */
public class HomeViewModel extends ViewModel {
    private final RadioRepository radioRepository;

    private final MutableLiveData<List<RadioStation>> popStationsLiveData = new MutableLiveData<>();

    private final RadioCallback callback = new RadioCallback();

    public HomeViewModel(RadioRepository radioRepository) {
        this.radioRepository = radioRepository;
    }

    public MutableLiveData<List<RadioStation>> getPopStationsLiveData() {
        return popStationsLiveData;
    }

    public void getPopStations() {
        radioRepository.getPopStation(callback);
    }

    private void setStationsLiveData(List<RadioStation> stations) {
        popStationsLiveData.postValue(stations);
    }

    public void updateCacheData() {
        radioRepository.saveStations(popStationsLiveData.getValue());
    }

    public void clearPopStationData() {
        popStationsLiveData.postValue(null);
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

        }

        @Override
        public void onError() {

        }
    }
}