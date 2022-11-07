package com.trungdunghoang125.alpharadio.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.trungdunghoang125.radiobrowserokhttp.model.CountryModel;
import com.trungdunghoang125.radiobrowserokhttp.request.RadioApiClient;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/4/2022.
 */
public class HomeViewModel extends ViewModel {
    private LiveData<List<CountryModel>> countryList;

    private final RadioApiClient apiClientInstance = RadioApiClient.getInstance();

    public HomeViewModel() {
        countryList = apiClientInstance.getCountryCode();
    }

    public LiveData<List<CountryModel>> getCountryList() {
        return countryList;
    }
}
