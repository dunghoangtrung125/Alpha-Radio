package com.trungdunghoang125.alpharadio.ui.fragment;

import static com.trungdunghoang125.alpharadio.ui.fragment.CountryDetailFragment.START_RADIO_EXTRAS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.trungdunghoang125.alpharadio.data.DataManager;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;
import com.trungdunghoang125.alpharadio.databinding.FragmentFavoriteBinding;
import com.trungdunghoang125.alpharadio.service.RadioPlayerService;
import com.trungdunghoang125.alpharadio.ui.adapter.FavStationAdapter;
import com.trungdunghoang125.alpharadio.viewmodel.favorite.FavoriteViewModel;
import com.trungdunghoang125.alpharadio.viewmodel.favorite.FavoriteViewModelFactory;

import java.util.List;

public class FavoriteFragment extends Fragment implements FavStationAdapter.StationItemClick {

    private FragmentFavoriteBinding binding;

    private FavoriteViewModel viewModel;

    private RecyclerView rcvFavStations;

    private FavStationAdapter adapter;

    private TextView textFavListEmpty;

    public FavoriteFragment() {
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
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        rcvFavStations = binding.rcvFavoriteList;
        textFavListEmpty = binding.textFavListEmpty;
        // get instance of view model
        RadioRepository repository = DataManager.getInstance().getRadioRepository();
        FavoriteViewModelFactory factory = new FavoriteViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(FavoriteViewModel.class);

        configureRecyclerView();
        observerDataChange();
        return binding.getRoot();
    }

    private void observerDataChange() {
        viewModel.getFavStations();
        viewModel.getFavListLiveData().observe(getViewLifecycleOwner(), new Observer<List<RadioStation>>() {
            @Override
            public void onChanged(List<RadioStation> stations) {
                if (stations.size() == 0) {
                    textFavListEmpty.setVisibility(View.VISIBLE);
                } else {
                    textFavListEmpty.setVisibility(View.GONE);
                }
                adapter.setStationList(stations);
                Log.d("tranle1811", "onChanged: " + stations.size());
            }
        });
    }

    private void configureRecyclerView() {
        adapter = new FavStationAdapter(this);
        rcvFavStations.setAdapter(adapter);
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
        viewModel.removeFavStation(station);
        observerDataChange();
    }
}