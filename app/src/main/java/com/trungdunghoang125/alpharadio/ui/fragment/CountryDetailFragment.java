package com.trungdunghoang125.alpharadio.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.DataManager;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;
import com.trungdunghoang125.alpharadio.databinding.FragmentCountryDetailBinding;
import com.trungdunghoang125.alpharadio.service.RadioPlayerService;
import com.trungdunghoang125.alpharadio.ui.adapter.RadioStationAdapter;
import com.trungdunghoang125.alpharadio.viewmodel.countrydetail.CountryDetailViewModel;
import com.trungdunghoang125.alpharadio.viewmodel.countrydetail.CountryDetailViewModelFactory;

import java.util.List;

public class CountryDetailFragment extends Fragment implements RadioStationAdapter.StationItemClick {

    private static final String INTENT_EXTRA_NAME = "countryCode";

    public static final String START_RADIO_EXTRAS = "startFromCountryDetail";

    private FragmentCountryDetailBinding binding;

    private CountryDetailViewModel viewModel;

    private ProgressBar mCountryDetailProgressBar;

    private RecyclerView mRcvStationList;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RadioStationAdapter adapter;

    private SearchView mCountryStationListSearch;

    private String mCountryCode;

    public CountryDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCountryDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mRcvStationList = binding.rcvStationList;
        swipeRefreshLayout = binding.swipeToRefreshStation;
        mCountryDetailProgressBar = binding.progressBarCountryDetail;
        mCountryStationListSearch = binding.searchViewStation;
        // view model instance
        RadioRepository repository = DataManager.getInstance().getRadioRepository();
        CountryDetailViewModelFactory factory = new CountryDetailViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(CountryDetailViewModel.class);
        // getIntent data
        mCountryCode = getArguments().getString(INTENT_EXTRA_NAME);
        // set data for recyclerview
        configureRecyclerView();
        observerStationDataChange();
        viewModel.getStations(mCountryCode);
        swipeToRefreshRadioStation();
        // search view filter list
        filterListBySearchView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {
        // Send position data click to Radio Player Fragment
        Intent intent = new Intent(getContext(), RadioPlayerService.class);
        intent.putExtra(START_RADIO_EXTRAS, position);
        requireContext().startService(intent);
    }

    @Override
    public void onCheckBoxImportance(RadioStation station) {
        viewModel.addFavStation(station);
    }

    private void configureRecyclerView() {
        adapter = new RadioStationAdapter(CountryDetailFragment.this);
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
        viewModel.getShowLoadingLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                mCountryDetailProgressBar.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getHideLoadingLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                mCountryDetailProgressBar.setVisibility(View.GONE);
            }
        });

        viewModel.getStationsLiveData().observe(getViewLifecycleOwner(), stations -> {
            adapter.setStationList(stations);
        });

        viewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void start(FragmentActivity activity, String countryCode) {
        CountryDetailFragment countryDetailFragment = new CountryDetailFragment();
        Bundle starter = new Bundle();
        starter.putString(INTENT_EXTRA_NAME, countryCode);
        countryDetailFragment.setArguments(starter);
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, countryDetailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}