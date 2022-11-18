package com.trungdunghoang125.alpharadio.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.trungdunghoang125.alpharadio.data.DataManager;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;
import com.trungdunghoang125.alpharadio.databinding.ActivityCountryDetailBinding;
import com.trungdunghoang125.alpharadio.ui.adapter.RadioStationAdapter;
import com.trungdunghoang125.alpharadio.viewmodel.countrydetail.CountryDetailViewModel;
import com.trungdunghoang125.alpharadio.viewmodel.countrydetail.CountryDetailViewModelFactory;

public class CountryDetailActivity extends AppCompatActivity implements RadioStationAdapter.StationItemClick {

    private static final String INTENT_EXTRA_NAME = "countryCode";

    private ActivityCountryDetailBinding binding;

    private CountryDetailViewModel viewModel;

    private ProgressBar mCountryDetailProgressBar;

    private RecyclerView mRcvStationList;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String mCountryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCountryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mRcvStationList = binding.rcvStationList;
        swipeRefreshLayout = binding.swipeToRefreshStation;
        mCountryDetailProgressBar = binding.progressBarCountryDetail;
        // view model instance
        RadioRepository repository = DataManager.getInstance().getRadioRepository();
        CountryDetailViewModelFactory factory = new CountryDetailViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(CountryDetailViewModel.class);
        // getIntent data
        mCountryCode = getIntent().getStringExtra(INTENT_EXTRA_NAME);
        observerStationDataChange();
        viewModel.getStations(mCountryCode);
        swipeToRefreshRadioStation();
    }

    @Override
    public void onItemClick(RadioStation station) {
        RadioPlayerActivity.start(CountryDetailActivity.this, station);
    }

    private void swipeToRefreshRadioStation() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            observerStationDataChange();
            viewModel.getStations(mCountryCode);
            swipeRefreshLayout.setProgressViewEndTarget(false, 0);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void observerStationDataChange() {
        viewModel.getShowLoadingLiveData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                mCountryDetailProgressBar.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getHideLoadingLiveData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                mCountryDetailProgressBar.setVisibility(View.GONE);
            }
        });

        viewModel.getStationsLiveData().observe(this, stations -> {
            RadioStationAdapter adapter = new RadioStationAdapter(CountryDetailActivity.this);
            adapter.setStationList(stations);
            mRcvStationList.setAdapter(adapter);
        });
    }

    public static void start(Context context, String countryCode) {
        Intent starter = new Intent(context, CountryDetailActivity.class);
        starter.putExtra(INTENT_EXTRA_NAME, countryCode);
        context.startActivity(starter);
    }
}