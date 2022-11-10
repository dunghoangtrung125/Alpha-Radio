package com.trungdunghoang125.alpharadio.viewmodel.countrydetail;

import android.util.Log;

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
    private final RadioRepository repository;

    private final MutableLiveData<List<RadioStation>> stationsLiveData = new MutableLiveData<>();

    private RadioCallback radioCallback = new RadioCallback();

    public CountryDetailViewModel(RadioRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<RadioStation>> getStationsLiveData() {
        return stationsLiveData;
    }

    public void getStations(String countryCode) {
        repository.getStations(radioCallback, countryCode);
    }

    private void setStationsLiveData(List<RadioStation> stations) {
        Log.d("tranle1811", "setStationsLiveData: " + stations.size());
        stationsLiveData.postValue(stations);
    }

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
