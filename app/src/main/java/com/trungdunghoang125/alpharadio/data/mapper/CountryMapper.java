package com.trungdunghoang125.alpharadio.data.mapper;

import com.trungdunghoang125.alpharadio.data.domain.Country;
import com.trungdunghoang125.alpharadio.data.model.CountryRemote;

/**
 * Created by trungdunghoang125 on 11/12/2022.
 */
public class CountryMapper {
    public static Country toDomain(int id, CountryRemote countryRemote) {
        return new Country(id, countryRemote.getName(), countryRemote.getStationCount());
    }
}
