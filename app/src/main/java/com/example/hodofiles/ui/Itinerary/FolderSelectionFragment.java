package com.example.hodofiles.ui.Itinerary;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hodofiles.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FolderSelectionFragment extends DialogFragment {

    private List<ItineraryFolder> folders = new ArrayList<>(); // Initialize the list
    private ItineraryViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder_selection, container, false);

        // Initialize the ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ItineraryViewModel.class);

        // Load folders from storage
        folders = loadFoldersFromStorage();

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.folder_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup the adapter with folder selection handling
        FolderAdapter adapter = new FolderAdapter(folders, this::onFolderSelected);
        recyclerView.setAdapter(adapter);

        return view;
    }

    // Handle folder selection
    private void onFolderSelected(ItineraryFolder folder) {
        Log.d("FolderSelection", "Selected folder: " + folder.getName());

        Bundle args = getArguments();
        if (args != null) {
            String placeName = args.getString("PLACE_NAME");
            String address = args.getString("ADDRESS");

            if (placeName == null || address == null) {
                Log.e("FolderSelection", "Missing place name or address.");
                return;
            }

            // Add a new stop to the selected folder
            folder.addStop(new ItineraryFolder.ItineraryStop(placeName, address, System.currentTimeMillis()));

            // Save the updated folders to storage
            saveFoldersToStorage();

            // Update the ViewModel
            viewModel.setSelectedFolder(folder);

            // Close the dialog fragment
            dismiss();
        }
    }

    // Load folders from SharedPreferences
    private List<ItineraryFolder> loadFoldersFromStorage() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String json = prefs.getString("itinerary_folders", "[]");
        Type type = new TypeToken<List<ItineraryFolder>>() {}.getType();
        List<ItineraryFolder> loadedFolders = new Gson().fromJson(json, type);
        return (loadedFolders != null) ? loadedFolders : new ArrayList<>();
    }

    // Save folders to SharedPreferences
    private void saveFoldersToStorage() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(folders);
        editor.putString("itinerary_folders", json);
        editor.apply(); // Use apply() for asynchronous saving
    }
}
