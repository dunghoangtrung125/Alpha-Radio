package com.trungdunghoang125.alpharadio.data.repository;


import com.trungdunghoang125.alpharadio.data.model.Country;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/8/2022.
 */
public interface RadioRepository {

    interface LoadCountriesCallback {
        void onCountriesLoad(List<Country> countries);

        void onDataLoadFailed();

        void onError();
    }

    void getCountries(LoadCountriesCallback callback);

    void saveCountries(List<Country> countries);
}
