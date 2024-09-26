package com.example.hodofiles.ui.Itinerary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItineraryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ItineraryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}