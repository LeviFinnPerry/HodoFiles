package com.example.hodofiles.ui.maps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.hodofiles.R;
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
import com.google.android.libraries.places.api.net.PlacesStatusCodes;

import java.util.Arrays;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SearchView searchView;
    private PlacesClient placesClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        searchView = view.findViewById(R.id.search_view);

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
                return true; // return true to indicate that the query has been handled
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set a default location (optional)
        LatLng defaultLocation = new LatLng(-34, 151);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
    }

    private void searchPlace(String query) {
        // Build the autocomplete request
        FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(predictionsRequest).addOnSuccessListener(response -> {
            if (response.getAutocompletePredictions().isEmpty()) {
                Toast.makeText(getContext(), "No results found", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the place ID of the first prediction result
            String placeId = response.getAutocompletePredictions().get(0).getPlaceId();

            // Specify the fields to return in the Place object
            List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

            // Build the request
            FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(placeId, placeFields).build();

            // Make a request to fetch the place
            placesClient.fetchPlace(placeRequest).addOnSuccessListener(placeResponse -> {
                Place place = placeResponse.getPlace();
                if (place.getLatLng() != null) {
                    LatLng latLng = place.getLatLng();
                    mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            }).addOnFailureListener(exception -> {
                Toast.makeText(getContext(), "Place not found: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(exception -> {
            Toast.makeText(getContext(), "Error fetching autocomplete predictions: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
