package com.example.hodofiles.ui.Itinerary;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hodofiles.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItineraryFragment extends Fragment {

    private RecyclerView itineraryList;
    private List<ItineraryFolder> folders = new ArrayList<>();  // Initialize list to avoid null
    private ItineraryAdapter adapter;
    private ItineraryViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_itinerary, container, false);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ItineraryViewModel.class);

        // Setup RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.itinerary_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter and set it to RecyclerView
        adapter = new ItineraryAdapter(folders, folder -> {
            // Navigate to ItineraryDetailFragment when a folder is selected
            ItineraryDetailFragment detailFragment = ItineraryDetailFragment.newInstance(folder);
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(adapter);

        // Observe ViewModel for itinerary updates
        viewModel.getItineraries().observe(getViewLifecycleOwner(), updatedFolders -> {
            if (updatedFolders != null) {
                folders.clear();
                folders.addAll(updatedFolders);
                adapter.notifyDataSetChanged();  // Notify adapter of data change
            }
        });

        // Setup FAB to add new itineraries
        FloatingActionButton fab = root.findViewById(R.id.fab_add_itinerary);
        fab.setOnClickListener(v -> showAddItineraryDialog());

        return root;
    }

    // Show a dialog to add a new itinerary folder
    private void showAddItineraryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Itinerary");

        final EditText input = new EditText(requireContext());
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String folderName = input.getText().toString().trim();
            if (!folderName.isEmpty()) {
                ItineraryFolder newFolder = new ItineraryFolder(folderName, new ArrayList<>(), System.currentTimeMillis());

                // Add the new folder to the ViewModel
                List<ItineraryFolder> updatedFolders = viewModel.getItineraries().getValue();
                if (updatedFolders == null) updatedFolders = new ArrayList<>();
                updatedFolders.add(newFolder);

                // Update ViewModel and save to storage
                viewModel.setItineraries(updatedFolders);
                saveFoldersToStorage(updatedFolders);
            } else {
                Toast.makeText(requireContext(), "Itinerary name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Reload folders from storage and update ViewModel
        List<ItineraryFolder> updatedFolders = loadFoldersFromStorage();
        viewModel.setItineraries(updatedFolders);
    }

    // Load folders from SharedPreferences
    private List<ItineraryFolder> loadFoldersFromStorage() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String json = prefs.getString("itinerary_folders", "[]");
        Type type = new TypeToken<List<ItineraryFolder>>() {}.getType();
        List<ItineraryFolder> loadedFolders = new Gson().fromJson(json, type);
        return (loadedFolders != null) ? loadedFolders : new ArrayList<>();  // Ensure list is not null
    }

    // Save folders to SharedPreferences
    private void saveFoldersToStorage(List<ItineraryFolder> folders) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(folders);
        editor.putString("itinerary_folders", json);
        editor.apply();  // Use apply() for async saving
    }
}
