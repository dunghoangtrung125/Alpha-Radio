package com.trungdunghoang125.alpharadio.data.model;

/**
 * Created by trungdunghoang125 on 11/9/2022.
 */
public class RadioStation {
    private String name;

    private String url;

    private String favicon;

    private String tags;

    public RadioStation(String name, String url, String favicon, String tags) {
        this.name = name;
        this.url = url;
        this.favicon = favicon;
        this.tags = tags;
    }

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
}
