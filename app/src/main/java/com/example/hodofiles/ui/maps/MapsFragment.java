package com.example.hodofiles.ui.maps;

import static com.example.hodofiles.MainActivity.searchfeedLatlng;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.hodofiles.R;
import com.example.hodofiles.ui.searchfeed.SearchFeedFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SearchView searchView;
    private PlacesClient placesClient;
    private LatLng currentLatLng;  // To store the current selected LatLng
    private Button goToSearchFeedButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        searchView = view.findViewById(R.id.search_view);
        goToSearchFeedButton = view.findViewById(R.id.btn_go_to_search_feed);

        // Initialize the Places API
        Places.initialize(requireContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(requireContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPlace(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Button to navigate to SearchFeedFragment
        goToSearchFeedButton.setOnClickListener(v -> {
            if (currentLatLng != null) {
                // Use Singleton Class to pass data
                LatLong locationData = LatLong.getInstance();
                locationData.setLatitude(currentLatLng.latitude);
                locationData.setLongitude(currentLatLng.longitude);

                // Navigate to SearchFeedFragment
                SearchFeedFragment searchFeedFragment = new SearchFeedFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, searchFeedFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "No location selected", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bundle bundle = getArguments();
        if(searchfeedLatlng != null)
        {

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchfeedLatlng, 10));
        }
        else
        {
            // Set a default location (optional)
            LatLng defaultLocation = new LatLng(-37.788101, 175.276993);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
        }
    }

    private void searchPlace(String query) {
        FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(predictionsRequest).addOnSuccessListener(response -> {
            if (response.getAutocompletePredictions().isEmpty()) {
                Toast.makeText(getContext(), "No results found", Toast.LENGTH_SHORT).show();
                return;
            }

            String placeId = response.getAutocompletePredictions().get(0).getPlaceId();
            List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

            FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(placeId, placeFields).build();

            placesClient.fetchPlace(placeRequest).addOnSuccessListener(placeResponse -> {
                Place place = placeResponse.getPlace();
                if (place.getLatLng() != null) {
                    currentLatLng = place.getLatLng();  // Store the current LatLng

                    // Use Singleton Class to pass data to SearchFeedFragment
                    LatLong locationData = LatLong.getInstance();
                    locationData.setLatitude(currentLatLng.latitude);
                    locationData.setLongitude(currentLatLng.longitude);

                    // Add a marker at the selected location
                    mMap.addMarker(new MarkerOptions().position(currentLatLng).title(place.getName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                }
            }).addOnFailureListener(exception -> {
                Toast.makeText(getContext(), "Place not found: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(exception -> {
            Toast.makeText(getContext(), "Error fetching autocomplete predictions: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
