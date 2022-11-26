package com.trungdunghoang125.alpharadio.viewmodel.favorite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/26/2022.
 */
public class FavoriteViewModel extends ViewModel {
    private final RadioRepository radioRepository;

    private final MutableLiveData<List<RadioStation>> favListLiveData = new MutableLiveData<>();

    private final RadioCallback callback = new RadioCallback();

    public FavoriteViewModel(RadioRepository radioRepository) {
        this.radioRepository = radioRepository;
    }

    public LiveData<List<RadioStation>> getFavListLiveData() {
        return favListLiveData;
    }

    private void setFavStationsLiveData(List<RadioStation> stations) {
        favListLiveData.postValue(stations);
    }

    public void getFavStations() {
        radioRepository.getFavStations(callback);
    }

    public void addFavStation(RadioStation station) {
        radioRepository.addFavStation(station);
    }

    public void removeFavStation(RadioStation station) {
        radioRepository.removeFavStation(station);
    }

    /**
     * Callback class
     */
    private class RadioCallback implements RadioRepository.LoadStationsCallback {

        @Override
        public void onStationsLoad(List<RadioStation> stations) {
            setFavStationsLiveData(stations);
        }

        @Override
        public void onDataLoadFailed() {

        }

        @Override
        public void onError() {

        }
    }
}
