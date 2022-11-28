package com.trungdunghoang125.alpharadio.viewmodel.language;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trungdunghoang125.alpharadio.data.domain.Language;
import com.trungdunghoang125.alpharadio.data.repository.RadioRepository;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/28/2022.
 */
public class LanguageViewModel extends ViewModel {

    private final MutableLiveData<List<Language>> languagesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> showLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> hideLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();

    private final RadioRepository radioRepository;

    private final RadioCallback callback = new RadioCallback();

    public LanguageViewModel(RadioRepository radioRepository) {
        this.radioRepository = radioRepository;
    }

    public MutableLiveData<List<Language>> getLanguagesLiveData() {
        return languagesLiveData;
    }

    public MutableLiveData<Void> getShowLoadingLiveData() {
        return showLoadingLiveData;
    }

    public MutableLiveData<Void> getHideLoadingLiveData() {
        return hideLoadingLiveData;
    }

    public MutableLiveData<String> getErrorMessageLiveData() {
        return errorMessageLiveData;
    }

    private void setLanguagesLiveData(List<Language> languages) {
        setIsLoading(false);
        languagesLiveData.postValue(languages);
    }

    public void getLanguages() {
        setIsLoading(true);
        radioRepository.getLanguages(callback);
    }

    private void setIsLoading(boolean loading) {
        if (loading) {
            showLoadingLiveData.postValue(null);
        } else {
            hideLoadingLiveData.postValue(null);
        }
    }

    /**
     * Callback class
     */
    private class RadioCallback implements RadioRepository.LoadLanguagesCallback {

        @Override
        public void onLanguagesLoad(List<Language> languages) {
            setLanguagesLiveData(languages);
        }

        @Override
        public void onDataLoadFailed() {
            errorMessageLiveData.postValue("Server error. Try again!");
        }

        @Override
        public void onError() {
            errorMessageLiveData.postValue("Something went wrong!");
        }
    }
}
