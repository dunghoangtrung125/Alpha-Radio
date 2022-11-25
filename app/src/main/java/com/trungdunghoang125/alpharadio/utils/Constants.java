package com.trungdunghoang125.alpharadio.utils;

import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.model.RadioFilterTypeModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by trungdunghoang125 on 11/4/2022.
 */

public final class Constants {

    // data of gridview
    public static final List<RadioFilterTypeModel> sFilterList = Collections.unmodifiableList(
            new ArrayList<RadioFilterTypeModel>() {{
                add(new RadioFilterTypeModel("By country", R.drawable.filter_item_background));
                add(new RadioFilterTypeModel("By language", R.drawable.filter_item_background));
            }}
    );

    public static final String BASE_URL = "https://radio-browser.p.rapidapi.com/json/";
    public static final String X_RAPIDAPI_KEY = "a0ffb895eamshb2ff9106aeaf436p1b9818jsn220519fbf2c3";
    public static final String X_RAPIDAPI_HOST = "radio-browser.p.rapidapi.com";
    // public static final String BASE_COUNTRY_FLAG_URL = "https://www.countryflagicons.com/FLAT/64/";
    public static final String BASE_COUNTRY_FLAG_URL = "https://countryflagsapi.com/png/";
    public static final String RADIO_STATION_EXTRA = "radioStationCode";

    public static final String EXO_PLAYER_PLAYER_STATUS_ACTION = "com.example.exoplayer.PLAYER_STATUS";
    public static final String STATE = "state";

    // const for broadcast receiver
    public static final String NOTIFICATION_CONTROL_ACTION = "com.trungdunghoang125.NOTIFICATION_CONTROL";
    public static final String ACTION_NAME = "action-name";
}
