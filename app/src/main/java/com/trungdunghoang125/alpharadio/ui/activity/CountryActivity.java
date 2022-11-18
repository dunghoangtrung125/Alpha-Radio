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
import com.trungdunghoang125.alpharadio.data.domain.Country;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;
import com.trungdunghoang125.alpharadio.databinding.ActivityCountryBinding;
import com.trungdunghoang125.alpharadio.ui.adapter.CountryListAdapter;
import com.trungdunghoang125.alpharadio.viewmodel.country.CountryViewModel;
import com.trungdunghoang125.alpharadio.viewmodel.country.CountryViewModelFactory;

public class CountryActivity extends AppCompatActivity implements CountryListAdapter.CountryItemClick {

    private ActivityCountryBinding binding;

    private CountryViewModel viewModel;

    private ProgressBar mProgressBarCountry;

    private RecyclerView mRcvCountry;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCountryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // binding
        mRcvCountry = binding.rcvCountryList;
        swipeRefreshLayout = binding.swipeToRefreshCountry;
        mProgressBarCountry = binding.progressBarCountry;
        // view model instance
        RadioRepository radioRepository = DataManager.getInstance().getRadioRepository();
        CountryViewModelFactory factory = new CountryViewModelFactory(radioRepository);
        viewModel = new ViewModelProvider(this, factory).get(CountryViewModel.class);
        // get Countries data
        viewModel.getCountries();
        observerCountryViewModel();
        swipeToRefreshData();
    }

    @Override
    public void onItemClick(Country country) {
        CountryDetailActivity.start(CountryActivity.this, country.getName());
    }

    private void swipeToRefreshData() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.getCountries();
            observerCountryViewModel();
            swipeRefreshLayout.setProgressViewEndTarget(false, 0);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, CountryActivity.class);
        context.startActivity(starter);
    }

    private void observerCountryViewModel() {
        viewModel.getShowLoadingLiveData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                mProgressBarCountry.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getHideLoadingLiveData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                mProgressBarCountry.setVisibility(View.GONE);
            }
        });

        viewModel.getCountriesLiveData().observe(this, countries -> {
            CountryListAdapter adapter = new CountryListAdapter(countries, CountryActivity.this);
            mRcvCountry.setAdapter(adapter);
        });
    }
}