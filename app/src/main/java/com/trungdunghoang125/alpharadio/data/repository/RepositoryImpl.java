package com.trungdunghoang125.alpharadio.data.repository;

import android.util.Log;

import com.trungdunghoang125.alpharadio.data.domain.Country;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/8/2022.
 */
public class RepositoryImpl implements RadioRepository {

    private final RadioDataSource.Remote remote;
    private final RadioDataSource.Local local;
    private final RadioDataSource.Local cache;

    private static RepositoryImpl instance;

    public RepositoryImpl(RadioRemoteDataSource remote,
                          RadioLocalDataSource local,
                          RadioCacheDataSource cache) {
        this.remote = remote;
        this.local = local;
        this.cache = cache;
    }

    public static RepositoryImpl getInstance(RadioRemoteDataSource remote,
                                             RadioLocalDataSource local,
                                             RadioCacheDataSource cache) {
        if (instance == null) {
            instance = new RepositoryImpl(remote, local, cache);
        }
        return instance;
    }

    @Override
    public void getCountries(LoadCountriesCallback callback) {
        if (callback == null) return;

        cache.getCountries(new LoadCountriesCallback() {
            @Override
            public void onCountriesLoad(List<Country> countries) {
                callback.onCountriesLoad(countries);
                Log.d("tranle1811", "onMoviesLoaded: " + "load from cache");
            }

            @Override
            public void onDataLoadFailed() {
                getCountriesFromLocal(callback);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void saveCountries(List<Country> countries) {
        local.saveCountries(countries);
    }

    @Override
    public void getStations(LoadStationsCallback callback, String countryCode) {
        if (callback == null) return;
        getStationsFromRemote(callback, countryCode);
    }

    @Override
    public void getSearchStationsResult(LoadStationsCallback callback, String name) {
        if (callback == null) return;
        getSearchResultFromRemote(callback, name);
    }

    @Override
    public void getPopStation(LoadStationsCallback callback) {
        if (callback == null) return;
        getPopStationFromRemote(callback);
    }

    private void getPopStationFromRemote(LoadStationsCallback callback) {
        remote.getPopStation(new LoadStationsCallback() {
            @Override
            public void onStationsLoad(List<RadioStation> stations) {
                callback.onStationsLoad(stations);
                refreshStationsCache(stations);
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

    @Override
    public void saveStations(List<RadioStation> stations) {
        refreshStationsCache(stations);
    }

    @Override
    public void getFavStations(LoadStationsCallback callback) {
        if (callback == null) return;
        getFavStationsFromLocal(callback);
    }

    @Override
    public void addFavStation(RadioStation station) {
        local.addFavStation(station);
    }

    @Override
    public void removeFavStation(RadioStation station) {
        local.removeFavStation(station);
    }

    private void getCountriesFromRemote(LoadCountriesCallback callback) {
        remote.getCountries(new LoadCountriesCallback() {
            @Override
            public void onCountriesLoad(List<Country> countries) {
                callback.onCountriesLoad(countries);
                Log.d("tranle1811", "onMoviesLoaded: " + "load from remote");
                refreshCountriesCache(countries);
                saveCountries(countries);
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

    private void getCountriesFromLocal(LoadCountriesCallback callback) {
        local.getCountries(new LoadCountriesCallback() {
            @Override
            public void onCountriesLoad(List<Country> countries) {
                callback.onCountriesLoad(countries);
                Log.d("tranle1811", "onMoviesLoaded: " + "load from local");
                // refresh countries cache
                refreshCountriesCache(countries);
            }

            @Override
            public void onDataLoadFailed() {
                getCountriesFromRemote(callback);
            }

            @Override
            public void onError() {

            }
        });
    }

    private void getFavStationsFromLocal(LoadStationsCallback callback) {
        local.getFavStations(new LoadStationsCallback() {
            @Override
            public void onStationsLoad(List<RadioStation> stations) {
                callback.onStationsLoad(stations);
                refreshStationsCache(stations);
            }

            @Override
            public void onDataLoadFailed() {

            }

            @Override
            public void onError() {

            }
        });
    }

    private void refreshCountriesCache(List<Country> countries) {
        cache.saveCountries(countries);
    }

    private void getStationsFromRemote(LoadStationsCallback callback, String countryCode) {
        remote.getCountryRadioStation(new LoadStationsCallback() {
            @Override
            public void onStationsLoad(List<RadioStation> stations) {
                callback.onStationsLoad(stations);
                refreshStationsCache(stations);
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

    private void getSearchResultFromRemote(LoadStationsCallback callback, String name) {
        remote.getStationSearchResult(new LoadStationsCallback() {
            @Override
            public void onStationsLoad(List<RadioStation> stations) {
                callback.onStationsLoad(stations);
                refreshStationsCache(stations);
            }

            @Override
            public void onDataLoadFailed() {
                callback.onDataLoadFailed();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        }, name);
    }

    private void refreshStationsCache(List<RadioStation> stations) {
        cache.saveStations(stations);
    }
}