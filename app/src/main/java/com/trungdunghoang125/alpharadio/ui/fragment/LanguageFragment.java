package com.trungdunghoang125.alpharadio.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.trungdunghoang125.alpharadio.data.domain.Language;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;
import com.trungdunghoang125.alpharadio.databinding.FragmentLanguageBinding;
import com.trungdunghoang125.alpharadio.ui.adapter.LanguageListAdapter;
import com.trungdunghoang125.alpharadio.viewmodel.language.LanguageViewModel;
import com.trungdunghoang125.alpharadio.viewmodel.language.LanguageViewModelFactory;

public class LanguageFragment extends Fragment implements LanguageListAdapter.ItemClick {

    private FragmentLanguageBinding binding;

    private ImageButton mButtonBackStack;
    private RecyclerView mRcvLanguagesList;
    private ProgressBar mLanguagesLoading;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LanguageListAdapter adapter;
    private LanguageViewModel viewModel;

    public LanguageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLanguageBinding.inflate(inflater, container, false);
        mButtonBackStack = binding.btnBackLanguage;
        mRcvLanguagesList = binding.rcvLanguageList;
        mLanguagesLoading = binding.progressBarLanguage;
        mSwipeRefreshLayout = binding.swipeToRefreshLanguage;
        // view model instance
        RadioRepository radioRepository = DataManager.getInstance().getRadioRepository();
        LanguageViewModelFactory factory = new LanguageViewModelFactory(radioRepository);
        viewModel = new ViewModelProvider(this, factory).get(LanguageViewModel.class);

        viewModel.getLanguages();
        configureBackStackButton();
        observerData();
        swipeToRefreshData();

        return binding.getRoot();
    }

    private void observerData() {
        viewModel.getShowLoadingLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                mLanguagesLoading.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getHideLoadingLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                mLanguagesLoading.setVisibility(View.GONE);
            }
        });

        viewModel.getLanguagesLiveData().observe(getViewLifecycleOwner(), languages -> {
            LanguageListAdapter adapter = new LanguageListAdapter(languages, LanguageFragment.this);
            mRcvLanguagesList.setAdapter(adapter);
        });

        viewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void swipeToRefreshData() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.getLanguages();
            observerData();
            mSwipeRefreshLayout.setProgressViewEndTarget(false, 0);
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }

    private void configureBackStackButton() {
        mButtonBackStack.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    public static void start(FragmentActivity activity) {
        LanguageFragment languageFragment = new LanguageFragment();
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, languageFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(Language language) {
        LanguageDetailFragment.start(getActivity(), language.getName());
    }
}