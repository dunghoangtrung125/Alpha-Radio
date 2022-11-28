package com.trungdunghoang125.alpharadio.data.repository;

import android.util.SparseArray;

import com.trungdunghoang125.alpharadio.data.domain.Country;
import com.trungdunghoang125.alpharadio.data.domain.Language;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trungdunghoang125 on 11/12/2022.
 */
public class RadioCacheDataSource implements RadioDataSource.Local {

    private static RadioCacheDataSource sInstance;

    private final SparseArray<Country> cachedCountries = new SparseArray<>();

    private final SparseArray<Language> cachedLanguages = new SparseArray<>();

    // cache to store station list when call service
    public static final List<RadioStation> cacheStations = new ArrayList<>();

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
    public void getLanguages(RadioRepository.LoadLanguagesCallback callback) {
        if (cachedLanguages.size() > 0) {
            List<Language> languages = new ArrayList<>();
            for (int i = 0; i < cachedLanguages.size(); i++) {
                int key = cachedLanguages.keyAt(i);
                languages.add(cachedLanguages.get(key));
            }
            callback.onLanguagesLoad(languages);
        } else {
            callback.onDataLoadFailed();
        }
    }

    @Override
    public void getStationByLanguage(RadioRepository.LoadStationsCallback callback, String language) {

    }

    @Override
    public void getStationSearchResult(RadioRepository.LoadStationsCallback callback, String name) {

    }

    @Override
    public void getPopStation(RadioRepository.LoadStationsCallback callback) {

    }

    @Override
    public void saveCountries(List<Country> countries) {
        cachedCountries.clear();
        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(i);
            cachedCountries.put(i, country);
        }
    }

    @Override
    public void saveLanguages(List<Language> languages) {
        cachedLanguages.clear();
        for (int i = 0; i < languages.size(); i++) {
            Language language = languages.get(i);
            cachedLanguages.put(i, language);
        }
    }

    @Override
    public void saveStations(List<RadioStation> stations) {
        cacheStations.clear();
        if (stations != null) {
            cacheStations.addAll(stations);
        }
    }

    @Override
    public void getFavStations(RadioRepository.LoadStationsCallback callback) {

    }

    @Override
    public void addFavStation(RadioStation station) {

    }

    @Override
    public void removeFavStation(RadioStation station) {

    }
}
