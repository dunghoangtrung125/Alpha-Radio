package com.trungdunghoang125.alpharadio.data.repository;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/8/2022.
 */
public interface RadioDataSource {
    interface Remote {
        void getCountries(RadioRepository.LoadCountriesCallback callback);
    }

    interface Local extends Remote {
        void saveCountries(List countries);
    }
}
