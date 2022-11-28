package com.trungdunghoang125.alpharadio.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.trungdunghoang125.alpharadio.databinding.FragmentLanguageBinding;

public class LanguageFragment extends Fragment {
    private FragmentLanguageBinding binding;

    public LanguageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLanguageBinding.inflate(inflater, container, false);






        return binding.getRoot();
    }
}