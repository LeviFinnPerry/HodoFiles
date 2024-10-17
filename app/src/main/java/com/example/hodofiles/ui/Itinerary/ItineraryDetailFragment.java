package com.example.hodofiles.ui.Itinerary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hodofiles.R;

public class ItineraryDetailFragment extends Fragment {

    private static final String ARG_FOLDER = "selected_folder";

    private ItineraryViewModel viewModel;
    private RecyclerView stopsRecyclerView;

    public static ItineraryDetailFragment newInstance(ItineraryFolder folder) {
        ItineraryDetailFragment fragment = new ItineraryDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FOLDER, folder);  // Assuming ItineraryFolder implements Serializable
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_itinerary_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ItineraryViewModel.class);

        // Set up folder details
        TextView folderName = view.findViewById(R.id.itinerary_detail_title);
        stopsRecyclerView = view.findViewById(R.id.stops_recycler_view);
        stopsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Get selected folder from arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            ItineraryFolder folder = (ItineraryFolder) arguments.getSerializable(ARG_FOLDER);

            if (folder != null) {
                folderName.setText(folder.getName());

                // Set up adapter for stops within the selected folder
                StopAdapter stopAdapter = new StopAdapter(folder.getStops());
                stopsRecyclerView.setAdapter(stopAdapter);
            }
        }

        // Set up Back button functionality
        Button backButton = view.findViewById(R.id.btn_back_itinerary);
        backButton.setOnClickListener(v -> {
            // Pop the fragment back stack when the back button is pressed
            getParentFragmentManager().popBackStack();
        });
    }
}
