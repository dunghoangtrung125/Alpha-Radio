package com.trungdunghoang125.alpharadio.data.repository;

import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.trungdunghoang125.alpharadio.App;
import com.trungdunghoang125.alpharadio.data.domain.Country;
import com.trungdunghoang125.alpharadio.data.local.country.CountryDao;
import com.trungdunghoang125.alpharadio.data.local.favorite.FavStationDao;
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

    private final FavStationDao stationDao;

    private static RadioLocalDataSource instance;

    private RadioLocalDataSource(Executor executor, CountryDao countryDao, FavStationDao stationDao) {
        this.executor = executor;
        this.countryDao = countryDao;
        this.stationDao = stationDao;
    }

    public static RadioLocalDataSource getInstance(CountryDao countryDao, FavStationDao stationDao) {
        if (instance == null) {
            instance = new RadioLocalDataSource(new DiskExecutor(), countryDao, stationDao);
        }
        return instance;
    }

    @Override
    public void getCountries(RadioRepository.LoadCountriesCallback callback) {
        Runnable runnable = () -> {
            List<Country> countries = countryDao.getCountries();
            if (!countries.isEmpty()) {
                callback.onCountriesLoad(countries);
            } else callback.onDataLoadFailed();
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
        Runnable runnable = () -> countryDao.saveCountries(countries);
        executor.execute(runnable);
    }

    @Override
    public void saveStations(List<RadioStation> stations) {

    }

    @Override
    public void getFavStations(RadioRepository.LoadStationsCallback callback) {
        Runnable runnable = () -> {
            List<RadioStation> favStations = stationDao.getFavStations();
            callback.onStationsLoad(favStations);
        };
        executor.execute(runnable);
    }

    @Override
    public void addFavStation(RadioStation station) {
        Runnable runnable = () -> stationDao.insert(station);
        executor.execute(runnable);
        Toast.makeText(App.getInstance(), "Add " + station.getName() + "to favorite list", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeFavStation(RadioStation station) {
        Runnable runnable = () -> stationDao.delete(station);
        executor.execute(runnable);
        Toast.makeText(App.getInstance(), "Remove " + station.getName() + "from favorite list", Toast.LENGTH_SHORT).show();
    }
}
