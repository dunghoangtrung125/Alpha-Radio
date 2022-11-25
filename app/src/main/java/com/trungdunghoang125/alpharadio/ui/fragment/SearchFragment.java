package com.trungdunghoang125.alpharadio.ui.fragment;

import static com.trungdunghoang125.alpharadio.ui.fragment.CountryDetailFragment.START_RADIO_EXTRAS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.trungdunghoang125.alpharadio.data.DataManager;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;
import com.trungdunghoang125.alpharadio.databinding.FragmentSearchBinding;
import com.trungdunghoang125.alpharadio.service.RadioPlayerService;
import com.trungdunghoang125.alpharadio.ui.adapter.RadioStationAdapter;
import com.trungdunghoang125.alpharadio.viewmodel.search.SearchViewModel;
import com.trungdunghoang125.alpharadio.viewmodel.search.SearchViewModelFactory;

public class SearchFragment extends Fragment implements RadioStationAdapter.StationItemClick {

    private FragmentSearchBinding binding;

    private SearchViewModel viewModel;

    private SearchView mSearchView;

    private ProgressBar mStationSearchLoading;

    private RecyclerView mRcvSearchResult;

    private RadioStationAdapter adapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        mSearchView = binding.searchViewAdvance;
        mStationSearchLoading = binding.progressBarSearchLoading;
        mRcvSearchResult = binding.searchResultList;
        // view model instance
        RadioRepository repository = DataManager.getInstance().getRadioRepository();
        SearchViewModelFactory factory = new SearchViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(SearchViewModel.class);

        configureRecyclerView();
        observerDataChange();
        setUpSearchView();

        return binding.getRoot();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), RadioPlayerService.class);
        intent.putExtra(START_RADIO_EXTRAS, position);
        requireContext().startService(intent);
    }

    private void observerDataChange() {
        viewModel.getShowLoadingLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                mStationSearchLoading.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getHideLoadingLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                mStationSearchLoading.setVisibility(View.GONE);
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

    private void setUpSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.searchStation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void configureRecyclerView() {
        adapter = new RadioStationAdapter(SearchFragment.this);
        mRcvSearchResult.setAdapter(adapter);
    }
}