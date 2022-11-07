package com.trungdunghoang125.alpharadio.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.trungdunghoang125.alpharadio.databinding.FragmentHomeBinding;
import com.trungdunghoang125.alpharadio.ui.adapter.RadioFilterGridViewAdapter;
import com.trungdunghoang125.alpharadio.utils.Constants;
import com.trungdunghoang125.alpharadio.viewmodel.HomeViewModel;
import com.trungdunghoang125.radiobrowserokhttp.Utility;
import com.trungdunghoang125.radiobrowserokhttp.model.CountryModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<CountryModel> mCountryList = new ArrayList<>();

    private GridView mRadioFilterGrid;

    private FragmentHomeBinding binding;

    private HomeViewModel viewModel;

    Toast toast;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // binding
        mRadioFilterGrid = binding.gridRadioFilter;
        // set Adapter for Gridview
        RadioFilterGridViewAdapter adapter = new RadioFilterGridViewAdapter(requireContext(), Constants.sFilterList);
        mRadioFilterGrid.setAdapter(adapter);

        Utility.ShowToast.initToast(getContext(), toast, "Toast from radio-browser");

        viewModel.getCountryList().observe(getViewLifecycleOwner(), new Observer<List<CountryModel>>() {
            @Override
            public void onChanged(List<CountryModel> result) {
                Log.d("tranle1205", "onChanged: " + result.get(0).getCountryName());
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}