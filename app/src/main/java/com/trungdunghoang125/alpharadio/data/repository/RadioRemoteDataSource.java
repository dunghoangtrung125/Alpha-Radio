package com.trungdunghoang125.alpharadio.data.repository;


import android.util.Log;

import com.trungdunghoang125.alpharadio.data.domain.Country;
import com.trungdunghoang125.alpharadio.data.mapper.CountryMapper;
import com.trungdunghoang125.alpharadio.data.model.CountryRemote;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.remote.RadioBrowserApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by trungdunghoang125 on 11/8/2022.
 */
public class RadioRemoteDataSource implements RadioDataSource.Remote {

    private static RadioRemoteDataSource instance;

    private final RadioBrowserApi api;

    public RadioRemoteDataSource(RadioBrowserApi api) {
        this.api = api;
    }

    public static RadioRemoteDataSource getInstance(RadioBrowserApi api) {
        if (instance == null) {
            instance = new RadioRemoteDataSource(api);
        }
        return instance;
    }

    @Override
    public void getCountries(RadioRepository.LoadCountriesCallback callback) {
        api.getCountries(false, false).enqueue(new Callback<List<CountryRemote>>() {
            @Override
            public void onResponse(Call<List<CountryRemote>> call, Response<List<CountryRemote>> response) {
                List<CountryRemote> countries = response.body();
                if (countries != null && !countries.isEmpty()) {
                    List<Country> countriesDomain = new ArrayList<>();
                    for (int i = 0; i < countries.size(); i++) {
                        countriesDomain.add(CountryMapper.toDomain(i, countries.get(i)));
                    }

                    callback.onCountriesLoad(countriesDomain);
                } else {
                    callback.onDataLoadFailed();
                }
            }

            @Override
            public void onFailure(Call<List<CountryRemote>> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void getCountryRadioStation(RadioRepository.LoadStationsCallback callback, String countryCode) {
        Log.d("tranle1811", "getCountryRadioStation: " + countryCode);
        api.getCountryRadioStation(countryCode, false, 0, 100000, false)
                .enqueue(new Callback<List<RadioStation>>() {
                    @Override
                    public void onResponse(Call<List<RadioStation>> call, Response<List<RadioStation>> response) {
                        List<RadioStation> stationList = response.body();
                        if (stationList != null && !stationList.isEmpty()) {
                            callback.onStationsLoad(stationList);
                        } else {
                            callback.onDataLoadFailed();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<RadioStation>> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
}
