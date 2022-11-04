package com.trungdunghoang125.alpharadio.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trungdunghoang125.alpharadio.databinding.FragmentHomeBinding;
import com.trungdunghoang125.alpharadio.model.CountryModel;
import com.trungdunghoang125.alpharadio.ui.adapter.RadioFilterGridViewAdapter;
import com.trungdunghoang125.alpharadio.utils.Constants;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private List<CountryModel> mCountryList = new ArrayList<>();
    private GridView mRadioFilterGrid;
    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // binding
        mRadioFilterGrid = binding.gridRadioFilter;
        // set Adapter for Gridview
        RadioFilterGridViewAdapter adapter = new RadioFilterGridViewAdapter(requireContext(), Constants.mFilterList);
        mRadioFilterGrid.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}