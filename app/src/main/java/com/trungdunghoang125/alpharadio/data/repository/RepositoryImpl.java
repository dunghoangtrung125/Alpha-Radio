package com.trungdunghoang125.alpharadio.data.repository;

import com.trungdunghoang125.alpharadio.data.model.Country;

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