package com.trungdunghoang125.alpharadio.data.repository;

import com.trungdunghoang125.alpharadio.data.domain.Country;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/8/2022.
 */
public interface RadioDataSource {
    interface Remote {
        void getCountries(RadioRepository.LoadCountriesCallback callback);

        void getCountryRadioStation(RadioRepository.LoadStationsCallback callback, String countryCode);
    }

    interface Local extends Remote {
        void saveCountries(List<Country> countries);
    }
}
