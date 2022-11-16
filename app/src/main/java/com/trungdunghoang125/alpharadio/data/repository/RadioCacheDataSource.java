package com.trungdunghoang125.alpharadio.data.repository;

import android.util.SparseArray;

import com.trungdunghoang125.alpharadio.data.domain.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trungdunghoang125 on 11/12/2022.
 */
public class RadioCacheDataSource implements RadioDataSource.Local {

    private static RadioCacheDataSource sInstance;

    private final SparseArray<Country> cachedCountries = new SparseArray<>();

    public static RadioCacheDataSource getsInstance() {
        if (sInstance == null) {
            sInstance = new RadioCacheDataSource();
        }
        return sInstance;
    }

    @Override
    public void getCountries(RadioRepository.LoadCountriesCallback callback) {
        if (cachedCountries.size() > 0) {
            List<Country> countries = new ArrayList<>();
            for (int i = 0; i < cachedCountries.size(); i++) {
                int key = cachedCountries.keyAt(i);
                countries.add(cachedCountries.get(key));
            }
            callback.onCountriesLoad(countries);
        } else {
            callback.onDataLoadFailed();
        }
    }

    @Override
    public void getCountryRadioStation(RadioRepository.LoadStationsCallback callback, String countryCode) {

    }

    @Override
    public void saveCountries(List<Country> countries) {
        cachedCountries.clear();
        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(i);
            cachedCountries.put(i, country);
        }
    }
}
