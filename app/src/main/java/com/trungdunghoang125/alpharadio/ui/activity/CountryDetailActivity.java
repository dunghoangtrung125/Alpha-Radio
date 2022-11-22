package com.trungdunghoang125.alpharadio.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

import java.util.List;

public class CountryDetailActivity extends AppCompatActivity implements RadioStationAdapter.StationItemClick {

    private static final String INTENT_EXTRA_NAME = "countryCode";

    private ActivityCountryDetailBinding binding;

    private CountryDetailViewModel viewModel;

    private ProgressBar mCountryDetailProgressBar;

    private RecyclerView mRcvStationList;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RadioStationAdapter adapter;

    private SearchView mCountryStationListSearch;

    private String mCountryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCountryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mRcvStationList = binding.rcvStationList;
        swipeRefreshLayout = binding.swipeToRefreshStation;
        mCountryDetailProgressBar = binding.progressBarCountryDetail;
        mCountryStationListSearch = binding.searchViewStation;
        // view model instance
        RadioRepository repository = DataManager.getInstance().getRadioRepository();
        CountryDetailViewModelFactory factory = new CountryDetailViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(CountryDetailViewModel.class);
        // getIntent data
        mCountryCode = getIntent().getStringExtra(INTENT_EXTRA_NAME);
        // set data for recyclerview
        configureRecyclerView();
        observerStationDataChange();
        viewModel.getStations(mCountryCode);
        swipeToRefreshRadioStation();
        // search view filter list
        filterListBySearchView();
    }

    @Override
    public void onItemClick(int position) {
        RadioPlayerActivity.start(CountryDetailActivity.this, position);
    }

    private void configureRecyclerView() {
        adapter = new RadioStationAdapter(CountryDetailActivity.this);
        mRcvStationList.setAdapter(adapter);
    }

    private void filterListBySearchView() {
        mCountryStationListSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.getFilter().filter(newText);
                observerRadioFilterList();
                return false;
            }
        });
    }

    private void observerRadioFilterList() {
        viewModel.getRadioFilterLiveData().observe(this, new Observer<List<RadioStation>>() {
            @Override
            public void onChanged(List<RadioStation> stations) {
                adapter.setStationList(stations);
            }
        });
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
            adapter.setStationList(stations);
        });

        viewModel.getErrorMessageLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void start(Context context, String countryCode) {
        Intent starter = new Intent(context, CountryDetailActivity.class);
        starter.putExtra(INTENT_EXTRA_NAME, countryCode);
        context.startActivity(starter);
    }
}