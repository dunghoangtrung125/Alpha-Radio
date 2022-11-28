package com.trungdunghoang125.alpharadio.data;

import com.trungdunghoang125.alpharadio.App;
import com.trungdunghoang125.alpharadio.data.local.country.CountryDao;
import com.trungdunghoang125.alpharadio.data.local.country.CountryDatabase;
import com.trungdunghoang125.alpharadio.data.local.favorite.FavStationDao;
import com.trungdunghoang125.alpharadio.data.local.favorite.FavStationDatabase;
import com.trungdunghoang125.alpharadio.data.local.language.LanguageDao;
import com.trungdunghoang125.alpharadio.data.local.language.LanguageDatabase;
import com.trungdunghoang125.alpharadio.data.remote.RadioBrowserApi;
import com.trungdunghoang125.alpharadio.data.remote.RetrofitClient;
import com.trungdunghoang125.alpharadio.data.repository.RadioCacheDataSource;
import com.trungdunghoang125.alpharadio.data.repository.RadioLocalDataSource;
import com.trungdunghoang125.alpharadio.data.repository.RadioRemoteDataSource;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;
import com.trungdunghoang125.alpharadio.data.repository.RepositoryImpl;

/**
 * Created by trungdunghoang125 on 11/9/2022.
 */
public class DataManager {

    private static DataManager instance;

    private DataManager() {

    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public RadioRepository getRadioRepository() {

        RadioBrowserApi radioBrowserApi = RetrofitClient.getInstance().getApi();
        RadioRemoteDataSource remoteDataSource = RadioRemoteDataSource.getInstance(radioBrowserApi);

        CountryDao countryDao = CountryDatabase.getInstance(App.getInstance()).countryDao();
        LanguageDao languageDao = LanguageDatabase.getInstance(App.getInstance()).languageDao();
        FavStationDao stationDao = FavStationDatabase.getInstance(App.getInstance()).stationDao();
        RadioLocalDataSource localDataSource = RadioLocalDataSource.getInstance(countryDao, languageDao, stationDao);
        RadioCacheDataSource cacheDataSource = RadioCacheDataSource.getsInstance();

        return RepositoryImpl.getInstance(remoteDataSource, localDataSource, cacheDataSource);
    }
}
