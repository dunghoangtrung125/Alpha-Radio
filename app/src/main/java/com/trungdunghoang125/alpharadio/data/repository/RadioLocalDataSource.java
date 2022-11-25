package com.trungdunghoang125.alpharadio.data.repository;

import com.trungdunghoang125.alpharadio.data.domain.Country;
import com.trungdunghoang125.alpharadio.data.local.country.CountryDao;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.utils.DiskExecutor;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by trungdunghoang125 on 11/12/2022.
 */
public class RadioLocalDataSource implements RadioDataSource.Local {

    private final Executor executor;

    private final CountryDao countryDao;

    private static RadioLocalDataSource instance;

    private RadioLocalDataSource(Executor executor, CountryDao countryDao) {
        this.executor = executor;
        this.countryDao = countryDao;
    }

    public static RadioLocalDataSource getInstance(CountryDao countryDao) {
        if (instance == null) {
            instance = new RadioLocalDataSource(new DiskExecutor(), countryDao);
        }
        return instance;
    }

    @Override
    public void getCountries(RadioRepository.LoadCountriesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<Country> countries = countryDao.getCountries();
                if (!countries.isEmpty()) {
                    callback.onCountriesLoad(countries);
                } else callback.onDataLoadFailed();
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void getCountryRadioStation(RadioRepository.LoadStationsCallback callback, String countryCode) {

    }

    @Override
    public void getStationSearchResult(RadioRepository.LoadStationsCallback callback, String name) {

    }

    @Override
    public void saveCountries(List<Country> countries) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                countryDao.saveCountries(countries);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void saveStations(List<RadioStation> stations) {

    }
}
