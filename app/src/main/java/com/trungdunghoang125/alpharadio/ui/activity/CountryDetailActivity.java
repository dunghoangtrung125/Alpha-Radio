package com.trungdunghoang125.alpharadio.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.trungdunghoang125.alpharadio.data.DataManager;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;
import com.trungdunghoang125.alpharadio.databinding.ActivityCountryDetailBinding;
import com.trungdunghoang125.alpharadio.ui.adapter.RadioStationAdapter;
import com.trungdunghoang125.alpharadio.viewmodel.countrydetail.CountryDetailViewModel;
import com.trungdunghoang125.alpharadio.viewmodel.countrydetail.CountryDetailViewModelFactory;

import java.util.List;

public class CountryDetailActivity extends AppCompatActivity implements RadioStationAdapter.StationItemClick {

    private static final String INTENT_EXTRA_NAME = "countryCode";

    private ActivityCountryDetailBinding binding;

    private CountryDetailViewModel viewModel;

    private RecyclerView mRcvStationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCountryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mRcvStationList = binding.rcvStationList;
        // view model instance
        RadioRepository repository = DataManager.getInstance().getRadioRepository();
        CountryDetailViewModelFactory factory = new CountryDetailViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(CountryDetailViewModel.class);
        // getIntent data
        String countryCode = getIntent().getStringExtra(INTENT_EXTRA_NAME);
        observerStationDataChange();
        viewModel.getStations(countryCode);
    }

    private void observerStationDataChange() {
        viewModel.getStationsLiveData().observe(this, new Observer<List<RadioStation>>() {
            @Override
            public void onChanged(List<RadioStation> stations) {
                for (RadioStation station : stations) {
                    Log.d("tranle1811", "onChanged: " + station.getName() + " " + station.getUrl());
                    RadioStationAdapter adapter = new RadioStationAdapter(CountryDetailActivity.this);
                    adapter.setStationList(stations);
                    mRcvStationList.setAdapter(adapter);
                }
            }
        });
    }

    public static void start(Context context, String countryCode) {
        Intent starter = new Intent(context, CountryDetailActivity.class);
        starter.putExtra(INTENT_EXTRA_NAME, countryCode);
        context.startActivity(starter);
    }

    @Override
    public void onItemClick(RadioStation station) {

    }
}