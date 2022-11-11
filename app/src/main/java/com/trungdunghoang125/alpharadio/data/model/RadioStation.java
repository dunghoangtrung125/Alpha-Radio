package com.trungdunghoang125.alpharadio.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by trungdunghoang125 on 11/9/2022.
 */
public class RadioStation implements Parcelable {

    private String name;

    private String url;

    private String favicon;

    private String tags;

    private String country;

    private String state;

    private int bitrate;

    public RadioStation(String name, String url, String favicon, String tags, String country, String state, int bitrate) {
        this.name = name;
        this.url = url;
        this.favicon = favicon;
        this.tags = tags;
        this.country = country;
        this.state = state;
        this.bitrate = bitrate;
    }

    protected RadioStation(Parcel in) {
        name = in.readString();
        url = in.readString();
        favicon = in.readString();
        tags = in.readString();
        country = in.readString();
        state = in.readString();
        bitrate = in.readInt();
    }

    public static final Creator<RadioStation> CREATOR = new Creator<RadioStation>() {
        @Override
        public RadioStation createFromParcel(Parcel in) {
            return new RadioStation(in);
        }

        @Override
        public RadioStation[] newArray(int size) {
            return new RadioStation[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getFavicon() {
        return favicon;
    }

    public String getTags() {
        return tags;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public int getBitrate() {
        return bitrate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(favicon);
        dest.writeString(tags);
        dest.writeString(country);
        dest.writeString(state);
        dest.writeInt(bitrate);
    }
}
