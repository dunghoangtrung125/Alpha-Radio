package com.trungdunghoang125.alpharadio.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.trungdunghoang125.alpharadio.data.DataManager;
import com.trungdunghoang125.alpharadio.data.model.CountryRemote;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;
import com.trungdunghoang125.alpharadio.databinding.FragmentHomeBinding;
import com.trungdunghoang125.alpharadio.ui.activity.LanguageActivity;
import com.trungdunghoang125.alpharadio.ui.adapter.RadioFilterGridViewAdapter;
import com.trungdunghoang125.alpharadio.utils.Constants;
import com.trungdunghoang125.alpharadio.viewmodel.home.HomeViewModel;
import com.trungdunghoang125.alpharadio.viewmodel.home.HomeViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<CountryRemote> mCountryList = new ArrayList<>();

    private GridView mRadioFilterGrid;

    private FragmentHomeBinding binding;

    private HomeViewModel viewModel;

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
        // set Adapter for Gridview
        RadioFilterGridViewAdapter adapter = new RadioFilterGridViewAdapter(requireContext(), Constants.sFilterList);
        mRadioFilterGrid.setAdapter(adapter);
        // onClickListener for GridView
        mRadioFilterGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //CountryActivity.start(getActivity());
                        CountryFragment.start(requireActivity());
                        break;
                    case 1:
                        LanguageActivity.start(getActivity());
                        break;
                }
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