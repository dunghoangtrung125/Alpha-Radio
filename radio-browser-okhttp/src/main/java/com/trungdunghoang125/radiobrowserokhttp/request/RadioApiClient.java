package com.trungdunghoang125.radiobrowserokhttp.request;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trungdunghoang125.radiobrowserokhttp.model.CountryModel;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by trungdunghoang125 on 11/4/2022.
 */
public class RadioApiClient {
    private static RadioApiClient sInstance;

    private MutableLiveData<List<CountryModel>> countryList;

    public RadioApiClient() {
        countryList = new MutableLiveData<>();
    }

    public static RadioApiClient getInstance() {
        if (sInstance == null) {
            sInstance = new RadioApiClient();
        }
        return sInstance;
    }

    public LiveData<List<CountryModel>> getCountryCode() {
        RadioApi.getInstance().getCountryCode().enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    Type fooType = new TypeToken<ArrayList<CountryModel>>() {
                    }.getType();
                    List<CountryModel> result = gson.fromJson(response.body().string(), fooType);
                    countryList.postValue(result);
                }
            }
        });

        return countryList;
    }
}
