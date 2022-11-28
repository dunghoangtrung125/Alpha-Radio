package com.trungdunghoang125.alpharadio.ui.fragment;

import static android.content.Context.LOCATION_SERVICE;
import static com.trungdunghoang125.alpharadio.ui.fragment.CountryDetailFragment.START_RADIO_EXTRAS;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.trungdunghoang125.alpharadio.data.DataManager;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;
import com.trungdunghoang125.alpharadio.databinding.FragmentNearYouBinding;
import com.trungdunghoang125.alpharadio.service.RadioPlayerService;
import com.trungdunghoang125.alpharadio.ui.adapter.RadioStationAdapter;
import com.trungdunghoang125.alpharadio.viewmodel.nearyou.NearYouViewModel;
import com.trungdunghoang125.alpharadio.viewmodel.nearyou.NearYouViewModelFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class NearYouFragment extends Fragment implements RadioStationAdapter.StationItemClick {

    private FragmentNearYouBinding binding;

    private Location location;
    private List<Address> addresses;
    private Geocoder geocoder;
    private String countryCode;
    private LocationManager locationManager;
    private RecyclerView mRcvStationsByLocation;
    private RadioStationAdapter adapter;
    private NearYouViewModel viewModel;
    private ProgressBar mProgressLoading;

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    // PERMISSION GRANTED
                } else {
                    // PERMISSION NOT GRANTED
                }
            }
    );

    public NearYouFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get location permission
        locationManager = (LocationManager) requireContext().getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
            getCountryCodeByNetWorkProvider();
        } else {
            location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            geocoder = new Geocoder(getContext());
            try {
                if (location != null) {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null) {
                Address address = addresses.get(0);
                countryCode = address.getCountryCode();
                Log.d("tranle1811", "onCreateView: " + address.getCountryCode());
            }
        }

        // Inflate the layout for this fragment
        binding = FragmentNearYouBinding.inflate(inflater, container, false);
        mRcvStationsByLocation = binding.nearMeResultList;
        mProgressLoading = binding.progressNearYouLoading;
        // view model instance
        RadioRepository repository = DataManager.getInstance().getRadioRepository();
        NearYouViewModelFactory factory = new NearYouViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(NearYouViewModel.class);

        configureRecyclerView();
        observerStationDataChange();
        viewModel.getStationsInYourLocation(countryCode);

        return binding.getRoot();
    }

    private void getCountryCodeByNetWorkProvider() {
        TelephonyManager tm = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        countryCode = tm.getNetworkCountryIso().toUpperCase(Locale.ROOT);
        Log.d("tranle1811", "getCountryCodeByNetWorkProvider: ");
    }

    private void configureRecyclerView() {
        adapter = new RadioStationAdapter(NearYouFragment.this);
        mRcvStationsByLocation.setAdapter(adapter);
    }

    private void observerStationDataChange() {
        viewModel.getShowLoadingLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                mProgressLoading.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getHideLoadingLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                mProgressLoading.setVisibility(View.GONE);
            }
        });

        viewModel.getNearYoustationsLiveData().observe(getViewLifecycleOwner(), stations -> {
            adapter.setStationList(stations);
        });

        viewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), RadioPlayerService.class);
        intent.putExtra(START_RADIO_EXTRAS, position);
        requireContext().startService(intent);
    }

    @Override
    public void onCheckBoxImportance(RadioStation station) {
        viewModel.addFavStation(station);
    }
}