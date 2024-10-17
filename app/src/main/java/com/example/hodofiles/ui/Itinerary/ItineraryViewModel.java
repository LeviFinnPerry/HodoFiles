package com.example.hodofiles.ui.Itinerary;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItineraryViewModel extends AndroidViewModel {

    private final MutableLiveData<List<ItineraryFolder>> itineraries = new MutableLiveData<>();
    private final MutableLiveData<ItineraryFolder> selectedFolder = new MutableLiveData<>();

    public ItineraryViewModel(@NonNull Application application) {
        super(application);
        // Load folders from SharedPreferences when the ViewModel is created
        List<ItineraryFolder> savedFolders = loadFoldersFromStorage();
        itineraries.setValue(savedFolders);
    }

    public LiveData<List<ItineraryFolder>> getItineraries() {
        return itineraries;
    }

    public LiveData<ItineraryFolder> getSelectedFolder() {
        return selectedFolder;
    }

    public void setItineraries(List<ItineraryFolder> newFolders) {
        itineraries.setValue(newFolders);
        saveFoldersToStorage(newFolders);  // Save updates to SharedPreferences
    }

    public void setSelectedFolder(ItineraryFolder folder) {
        selectedFolder.setValue(folder);
    }

    // Add a stop to a folder
    public void addStopToFolder(ItineraryFolder folder, ItineraryFolder.ItineraryStop stop) {
        folder.addStop(stop);  // Add the stop to the selected folder
        saveFoldersToStorage(itineraries.getValue());  // Save the updated list to storage
        itineraries.setValue(itineraries.getValue());  // Trigger UI update
    }

    // Load folders from SharedPreferences
    private List<ItineraryFolder> loadFoldersFromStorage() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        String json = prefs.getString("itinerary_folders", "[]");
        Type type = new TypeToken<List<ItineraryFolder>>() {}.getType();
        List<ItineraryFolder> loadedFolders = new Gson().fromJson(json, type);
        return (loadedFolders != null) ? loadedFolders : new ArrayList<>();
    }

    // Save folders to SharedPreferences
    private void saveFoldersToStorage(List<ItineraryFolder> folders) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(folders);
        editor.putString("itinerary_folders", json);
        editor.apply();
    }
}