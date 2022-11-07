package com.trungdunghoang125.radio_browser_retrofit.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trungdunghoang125.radio_browser_retrofit.AppExecutors;
import com.trungdunghoang125.radio_browser_retrofit.model.CountryModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by trungdunghoang125 on 11/7/2022.
 */
public class RadioApiClient {
    private static RadioApiClient instance;
    private static RetrieveCountryList retrieveCountryList;
    private final MutableLiveData<List<CountryModel>> countryCodeList;

    // Constructor
    private RadioApiClient() {
        countryCodeList = new MutableLiveData<>();
    }

    public static RadioApiClient getInstance() {
        if (instance == null) {
            instance = new RadioApiClient();
        }
        return instance;
    }

    public LiveData<List<CountryModel>> getCountryCodeList() {
        return countryCodeList;
    }

    public void getCountryList() {
        if (retrieveCountryList != null) {
            retrieveCountryList = null;
        }

        retrieveCountryList = new RetrieveCountryList();
        final Future handler = AppExecutors.getInstance().getNetWorkIO().submit(retrieveCountryList);

        AppExecutors.getInstance().getNetWorkIO().schedule(new Runnable() {
            @Override
            public void run() {
                handler.cancel(true);
            }
        }, 1000, TimeUnit.MILLISECONDS);
    }

    private class RetrieveCountryList implements Runnable {
        private final boolean isReverse;
        private final boolean isHidebroken;

        // constructor
        public RetrieveCountryList() {
            this.isReverse = false;
            this.isHidebroken = false;
        }

        @Override
        public void run() {
            try {
                Response response = getCountryList().execute();

                if (response.code() ==  200) {
                    List<CountryModel> list = new ArrayList<List<CountryModel>>(response.body());
                }

            } catch (IOException e) {
                e.printStackTrace();
                countryCodeList.postValue(null);
            }
        }

        private Call<List<CountryModel>> getCountryList() {
            return RetrofitService.getRadioApi().getCountryCode(
                    false,
                    false
            );
        }
    }
}
