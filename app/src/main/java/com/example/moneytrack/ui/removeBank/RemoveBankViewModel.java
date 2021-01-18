package com.example.moneytrack.ui.removeBank;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RemoveBankViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RemoveBankViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}