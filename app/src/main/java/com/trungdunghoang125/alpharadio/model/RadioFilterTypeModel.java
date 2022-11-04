package com.trungdunghoang125.alpharadio.model;

/**
 * Created by trungdunghoang125 on 11/3/2022.
 */

/**
 * This class for store data of category card view on home screen
 */

public class RadioFilterTypeModel {
    private String mRadioFilterTitle;

    private int mRadioFilterBackground;

    public RadioFilterTypeModel(String mRadioFilterTitle, int mRadioFilterBackground) {
        this.mRadioFilterTitle = mRadioFilterTitle;
        this.mRadioFilterBackground = mRadioFilterBackground;
    }

    public String getRadioFilterTitle() {
        return mRadioFilterTitle;
    }

    public int getRadioFilterBackground() {
        return mRadioFilterBackground;
    }
}
