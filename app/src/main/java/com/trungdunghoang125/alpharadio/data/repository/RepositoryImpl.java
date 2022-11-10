package com.trungdunghoang125.alpharadio.data.repository;

import com.trungdunghoang125.alpharadio.data.model.Country;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/8/2022.
 */
public class RepositoryImpl implements RadioRepository {

    private final RadioDataSource.Remote remote;

    private static RepositoryImpl instance;

    public RepositoryImpl(RadioDataSource.Remote remote) {
        this.remote = remote;
    }

    public static RepositoryImpl getInstance(RadioDataSource.Remote remote) {
        if (instance == null) {
            instance = new RepositoryImpl(remote);
        }
        return instance;
    }

    @Override
    public void getCountries(LoadCountriesCallback callback) {
        if (callback == null) return;
        getCountriesFromRemote(callback);
    }

    @Override
    public void saveCountries(List<Country> countries) {

    }

    @Override
    public void getStations(LoadStationsCallback callback, String countryCode) {
        if (callback == null) return;
        getStationsFromRemote(callback, countryCode);
    }

    private void getStationsFromRemote(LoadStationsCallback callback, String countryCode) {
        remote.getCountryRadioStation(new LoadStationsCallback() {
            @Override
            public void onStationsLoad(List<RadioStation> stations) {
                callback.onStationsLoad(stations);
            }

            @Override
            public void onDataLoadFailed() {
                callback.onDataLoadFailed();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        }, countryCode);
    }

    @Override
    public void saveStations(List<RadioStation> stations) {

    }

    private void getCountriesFromRemote(LoadCountriesCallback callback) {
        remote.getCountries(new LoadCountriesCallback() {
            @Override
            public void onCountriesLoad(List<Country> countries) {
                callback.onCountriesLoad(countries);
            }

            @Override
            public void onDataLoadFailed() {
                callback.onDataLoadFailed();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }
}