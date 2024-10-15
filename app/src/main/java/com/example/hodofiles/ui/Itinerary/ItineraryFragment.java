package com.example.hodofiles.ui.Itinerary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hodofiles.R;

import java.util.Arrays;
import java.util.List;

public class ItineraryFragment extends Fragment {

    private RecyclerView itineraryList;
    private ItineraryAdapter adapter;
    private ItineraryViewModel itineraryViewModel;
    private RecyclerView itineraryRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_itinerary, container, false);

        // Initialize RecyclerView
        itineraryRecyclerView = root.findViewById(R.id.itinerary_list);
        itineraryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itineraryList = root.findViewById(R.id.itinerary_list);
        itineraryList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample data
        List<String> itineraries = Arrays.asList(
                "Japan January 2025", "Taupo December 2024",
                "Queenstown August 2024", "Auckland February 2024"
        );

        // Set Adapter
        ItineraryAdapter adapter = new ItineraryAdapter(itineraries, itemView -> {
            // Handle item click
        });
        itineraryRecyclerView.setAdapter(adapter);

        return root;
    }
}
