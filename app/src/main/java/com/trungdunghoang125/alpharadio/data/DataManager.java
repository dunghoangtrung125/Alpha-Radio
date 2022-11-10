package com.trungdunghoang125.alpharadio.data;

import com.trungdunghoang125.alpharadio.data.remote.RadioBrowserApi;
import com.trungdunghoang125.alpharadio.data.remote.RetrofitClient;
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

        return RepositoryImpl.getInstance(remoteDataSource);
    }
}
