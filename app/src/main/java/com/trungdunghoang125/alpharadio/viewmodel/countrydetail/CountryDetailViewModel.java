package com.trungdunghoang125.alpharadio.viewmodel.countrydetail;

import android.widget.Filter;
import android.widget.Filterable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by trungdunghoang125 on 11/10/2022.
 */
public class CountryDetailViewModel extends ViewModel implements Filterable {

    private final MutableLiveData<List<RadioStation>> stationsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> showLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> hideLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<RadioStation>> radioFilterLiveData = new MutableLiveData<>();
    private final RadioRepository repository;
    private List<RadioStation> resultList = new ArrayList<>();
    private RadioCallback radioCallback = new RadioCallback();

    public CountryDetailViewModel(RadioRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<RadioStation>> getStationsLiveData() {
        return stationsLiveData;
    }

    private void setStationsLiveData(List<RadioStation> stations) {
        setIsLoading(false);
        stationsLiveData.postValue(stations);
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

    public MutableLiveData<List<RadioStation>> getRadioFilterLiveData() {
        return radioFilterLiveData;
    }

    public void getStations(String countryCode) {
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

    public void removeFavStation(RadioStation station) {
        repository.removeFavStation(station);
    }

    /**
     * Filter data for search view
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<RadioStation> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(Objects.requireNonNull(stationsLiveData.getValue()));
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (RadioStation station : Objects.requireNonNull(stationsLiveData.getValue())) {
                        if (station.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(station);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                resultList.clear();
                resultList.addAll((List) results.values);
                radioFilterLiveData.postValue(resultList);
                // update stations data on cache
                repository.saveStations(resultList);
            }
        };
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
