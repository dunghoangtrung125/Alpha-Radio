package com.trungdunghoang125.alpharadio.ui.fragment;

import static com.trungdunghoang125.alpharadio.ui.fragment.CountryDetailFragment.START_RADIO_EXTRAS;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;
import com.trungdunghoang125.alpharadio.databinding.FragmentLanguageDetailBinding;
import com.trungdunghoang125.alpharadio.service.RadioPlayerService;
import com.trungdunghoang125.alpharadio.ui.adapter.RadioStationAdapter;
import com.trungdunghoang125.alpharadio.viewmodel.languagedetail.LanguageDetailViewModel;
import com.trungdunghoang125.alpharadio.viewmodel.languagedetail.LanguageDetailViewModelFactory;

public class LanguageDetailFragment extends Fragment implements RadioStationAdapter.StationItemClick {

    public static final String INTENT_EXTRA_LANGUAGE_CODE = "language-name";

    private String mLanguage;

    private FragmentLanguageDetailBinding binding;

    private LanguageDetailViewModel viewModel;

    private RadioStationAdapter adapter;

    private RecyclerView mRcvStationList;

    private ImageButton mBackStackBtn;

    private ProgressBar mProgressLoading;

    private TextView mTitleAppbar;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public LanguageDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLanguageDetailBinding.inflate(inflater, container, false);
        mRcvStationList = binding.rcvStationByLanguageList;
        mBackStackBtn = binding.btnBackLanguageDetail;
        mTitleAppbar = binding.tvLanguageNameAppBar;
        mProgressLoading = binding.progressBarLanguageDetail;
        mSwipeRefreshLayout = binding.swipeToRefreshLanguageDetail;
        // view model instance
        RadioRepository repository = DataManager.getInstance().getRadioRepository();
        LanguageDetailViewModelFactory factory = new LanguageDetailViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(LanguageDetailViewModel.class);
        // get intent data
        mLanguage = getArguments().getString(INTENT_EXTRA_LANGUAGE_CODE);
        configureRecyclerView();
        observerStationDataChange();
        viewModel.getStations(mLanguage);

        setBackStackButton();
        setTextForAppBar();
        swipeToRefreshRadioStation();

        return binding.getRoot();
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

        viewModel.getStationsByLanguageLiveData().observe(getViewLifecycleOwner(), stations -> {
            adapter.setStationList(stations);
        });

        viewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configureRecyclerView() {
        adapter = new RadioStationAdapter(this);
        mRcvStationList.setAdapter(adapter);
    }

    private void swipeToRefreshRadioStation() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            observerStationDataChange();
            viewModel.getStations(mLanguage);
            mSwipeRefreshLayout.setProgressViewEndTarget(false, 0);
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setBackStackButton() {
        mBackStackBtn.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setTextForAppBar() {
        mTitleAppbar.setText(mLanguage);
    }

    public static void start(FragmentActivity activity, String language) {
        LanguageDetailFragment languageDetailFragment = new LanguageDetailFragment();
        Bundle starter = new Bundle();
        starter.putString(INTENT_EXTRA_LANGUAGE_CODE, language);
        languageDetailFragment.setArguments(starter);
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, languageDetailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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