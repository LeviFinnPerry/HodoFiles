package com.example.hodofiles.ui.Itinerary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.hodofiles.R;

import java.util.List;

public class ItineraryDetailFragment extends Fragment {

    private ItineraryViewModel viewModel; // Step 1: Declare the ViewModel
    private static final String ARG_FOLDER_NAME = "folder_name";
    private static final String ARG_FOLDER_CREATION_DATE = "folder_creation_date";

    private String folderName;
    private long folderCreationDate;

    public ItineraryDetailFragment() {
        // Required empty public constructor
    }

    // Factory method to create a new instance with folder data
    public static ItineraryDetailFragment newInstance(ItineraryFolder folder) {
        ItineraryDetailFragment fragment = new ItineraryDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FOLDER_NAME, folder.getName());
        args.putLong(ARG_FOLDER_CREATION_DATE, folder.getCreationDate());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            folderName = getArguments().getString(ARG_FOLDER_NAME);
            folderCreationDate = getArguments().getLong(ARG_FOLDER_CREATION_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_itinerary_detail, container, false);

        // Set initial folder name and creation date in TextViews
        TextView nameTextView = view.findViewById(R.id.itinerary_detail_title);
        nameTextView.setText(folderName);

        // Handle the back button
        Button backButton = view.findViewById(R.id.btn_back_itinerary);
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Step 2: Initialize the ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ItineraryViewModel.class);

        // Step 3: Observe the selected folder from the ViewModel
        viewModel.getSelectedFolder().observe(getViewLifecycleOwner(), folder -> {
            if (folder != null) {
                updateUI(folder);  // Populate the UI with the selected folder data
            }
        });
    }

    // Step 4: Update the UI with the folder's stops and details
    private void updateUI(ItineraryFolder folder) {
        ((TextView) getView().findViewById(R.id.itinerary_detail_title)).setText(folder.getName());

        List<ItineraryFolder.ItineraryStop> stops = folder.getStops();
        if (stops.size() > 0) {
            ((TextView) getView().findViewById(R.id.stop_1_name)).setText(stops.get(0).getName());
            ((TextView) getView().findViewById(R.id.stop_1_address)).setText(stops.get(0).getAddress());
        } else {
            ((TextView) getView().findViewById(R.id.stop_1_name)).setText("No stops yet.");
            ((TextView) getView().findViewById(R.id.stop_1_address)).setText("");
        }

        if (stops.size() > 1) {
            ((TextView) getView().findViewById(R.id.stop_2_name)).setText(stops.get(1).getName());
            ((TextView) getView().findViewById(R.id.stop_2_address)).setText(stops.get(1).getAddress());
        } else {
            ((TextView) getView().findViewById(R.id.stop_2_name)).setText("");
            ((TextView) getView().findViewById(R.id.stop_2_address)).setText("");
        }
    }
}
