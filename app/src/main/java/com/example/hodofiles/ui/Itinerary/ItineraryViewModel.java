package com.example.hodofiles.ui.Itinerary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.List;

public class ItineraryViewModel extends ViewModel {

    private final MutableLiveData<List<String>> itineraries = new MutableLiveData<>();

    public ItineraryViewModel() {
        // Example data
        List<String> itineraryList = Arrays.asList("Japan January 2025", "Taupo December 2024", "Queenstown August 2024");
        itineraries.setValue(itineraryList);
    }

    public LiveData<List<String>> getItineraries() {
        return itineraries;
    }
}
