package com.trungdunghoang125.alpharadio.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.DataManager;
import com.trungdunghoang125.alpharadio.data.domain.Country;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;
import com.trungdunghoang125.alpharadio.databinding.FragmentCountryBinding;
import com.trungdunghoang125.alpharadio.ui.adapter.CountryListAdapter;
import com.trungdunghoang125.alpharadio.viewmodel.country.CountryViewModel;
import com.trungdunghoang125.alpharadio.viewmodel.country.CountryViewModelFactory;

public class CountryFragment extends Fragment implements CountryListAdapter.CountryItemClick {

    private FragmentCountryBinding binding;

    private CountryViewModel viewModel;

    private ProgressBar mProgressBarCountry;

    private RecyclerView mRcvCountry;

    private SwipeRefreshLayout swipeRefreshLayout;

    public CountryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("tranle1811", "onCreateView: " + "Country fragment");
        super.onCreate(savedInstanceState);
        binding = FragmentCountryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
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
        // Inflate the layout for this fragment

        return view;
    }

    @Override
    public void onItemClick(Country country) {
        //CountryDetailActivity.start(CountryActivity.this, country.getName());
        CountryDetailFragment.start(getActivity(), country.getName());
    }

    private void swipeToRefreshData() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.getCountries();
            observerCountryViewModel();
            swipeRefreshLayout.setProgressViewEndTarget(false, 0);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void observerCountryViewModel() {
        viewModel.getShowLoadingLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                mProgressBarCountry.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getHideLoadingLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                mProgressBarCountry.setVisibility(View.GONE);
            }
        });

        viewModel.getCountriesLiveData().observe(getViewLifecycleOwner(), countries -> {
            CountryListAdapter adapter = new CountryListAdapter(countries, CountryFragment.this);
            mRcvCountry.setAdapter(adapter);
        });

        viewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void start(FragmentActivity activity) {
        CountryFragment countryFragment = new CountryFragment();
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, countryFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}