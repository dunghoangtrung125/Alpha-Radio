package com.trungdunghoang125.alpharadio.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
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
        // view model instance
        RadioRepository radioRepository = DataManager.getInstance().getRadioRepository();
        CountryViewModelFactory factory = new CountryViewModelFactory(radioRepository);
        viewModel = new ViewModelProvider(this, factory).get(CountryViewModel.class);
        // get Countries data
        viewModel.getCountries();
        observerCountryData();
        swipeToRefreshData();
    }

    @Override
    public void onItemClick(Country country) {
        CountryDetailActivity.start(CountryActivity.this, country.getName());
    }

    private void swipeToRefreshData() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.getCountries();
            observerCountryData();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, CountryActivity.class);
        context.startActivity(starter);
    }

    private void observerCountryData() {
        viewModel.getCountriesLiveData().observe(this, countries -> {
            for (Country country : countries) {
                Log.d("hoangdung1205", "onChanged: " + country.getName() + " " + country.getStationCount());
            }
            CountryListAdapter adapter = new CountryListAdapter(countries, CountryActivity.this);
            mRcvCountry.setAdapter(adapter);
        });
    }
}