package com.trungdunghoang125.alpharadio.data.mapper;

import com.trungdunghoang125.alpharadio.data.domain.Language;
import com.trungdunghoang125.alpharadio.data.model.LanguageRemote;

/**
 * Created by trungdunghoang125 on 11/28/2022.
 */
public class LanguageMapper {
    public static Language toDomain(int id, LanguageRemote languageRemote) {
        return new Language(id, languageRemote.getName(), languageRemote.getStationCount());
    }
}
