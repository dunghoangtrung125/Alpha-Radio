package com.trungdunghoang125.radiobrowserokhttp.request;

import com.trungdunghoang125.radiobrowserokhttp.utils.Constants;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by trungdunghoang125 on 11/4/2022.
 */
public class RadioApi {
    private static RadioApi sInstance;
    private static final OkHttpClient sClient = new OkHttpClient();

    public RadioApi() {

    }

    public static RadioApi getInstance() {
        if (sInstance == null) {
            sInstance = new RadioApi();
        }
        return sInstance;
    }

    public Call getCountryCode() {
        HttpUrl url = new HttpUrl.Builder().scheme("https").host(Constants.HOST_NAME)
                .addPathSegment("json")
                .addPathSegment("countrycodes")
                .addQueryParameter("reverse", "false")
                .addQueryParameter("hidebroken", "false")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "a0ffb895eamshb2ff9106aeaf436p1b9818jsn220519fbf2c3")
                .addHeader("X-RapidAPI-Host", "radio-browser.p.rapidapi.com")
                .build();

        return sClient.newCall(request);
    }
}

/*
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://radio-browser.p.rapidapi.com/json/countrycodes?reverse=false&hidebroken=false")
                        .get()
                        .addHeader("X-RapidAPI-Key", "a0ffb895eamshb2ff9106aeaf436p1b9818jsn220519fbf2c3")
                        .addHeader("X-RapidAPI-Host", "radio-browser.p.rapidapi.com")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        Type fooType = new TypeToken<ArrayList<CountryModel>>() {
                        }.getType();
                        List<CountryModel> countryList = gson.fromJson(response.body().string(), fooType);
                        Log.d("tranle1811", "run: " + countryList.get(0).getCountryName());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
 */
