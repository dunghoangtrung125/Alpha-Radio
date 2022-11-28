package com.trungdunghoang125.alpharadio.ui.fragment;

import static com.trungdunghoang125.alpharadio.ui.fragment.CountryDetailFragment.START_RADIO_EXTRAS;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.trungdunghoang125.alpharadio.data.DataManager;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;
import com.trungdunghoang125.alpharadio.databinding.FragmentHomeBinding;
import com.trungdunghoang125.alpharadio.service.RadioPlayerService;
import com.trungdunghoang125.alpharadio.ui.adapter.PopStationAdapter;
import com.trungdunghoang125.alpharadio.ui.adapter.RadioFilterGridViewAdapter;
import com.trungdunghoang125.alpharadio.utils.Constants;
import com.trungdunghoang125.alpharadio.viewmodel.home.HomeViewModel;
import com.trungdunghoang125.alpharadio.viewmodel.home.HomeViewModelFactory;

import java.util.List;

public class HomeFragment extends Fragment implements PopStationAdapter.ItemClick {

    private GridView mRadioFilterGrid;

    private FragmentHomeBinding binding;

    private HomeViewModel viewModel;

    private RecyclerView mRcvPopStations;

    private PopStationAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RadioRepository radioRepository = DataManager.getInstance().getRadioRepository();
        HomeViewModelFactory factory = new HomeViewModelFactory(radioRepository);
        viewModel = new ViewModelProvider(requireActivity(), factory).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // binding
        mRadioFilterGrid = binding.gridRadioFilter;
        mRcvPopStations = binding.rcvPopRadio;
        // set Adapter for Gridview
        RadioFilterGridViewAdapter adapter = new RadioFilterGridViewAdapter(requireContext(), Constants.sFilterList);
        mRadioFilterGrid.setAdapter(adapter);
        // onClickListener for GridView
        mRadioFilterGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        CountryFragment.start(requireActivity());
                        break;
                    case 1:
                        break;
                }
            }
        });

        // set up for Pop Stations Recyclerview
        configureRecyclerView();
        observerDataChanged();
        getPopStations();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.updateCacheData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), RadioPlayerService.class);
        intent.putExtra(START_RADIO_EXTRAS, position);
        requireContext().startService(intent);
    }

    private void getPopStations() {
        viewModel.getPopStations();
    }

    private void observerDataChanged() {
        viewModel.getPopStationsLiveData().observe(getViewLifecycleOwner(), new Observer<List<RadioStation>>() {
            @Override
            public void onChanged(List<RadioStation> stations) {
                adapter.setStationList(stations);
            }
        });
    }

    private void configureRecyclerView() {
        adapter = new PopStationAdapter(this);
        mRcvPopStations.setAdapter(adapter);
    }
}