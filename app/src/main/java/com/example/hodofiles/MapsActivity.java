package com.example.hodofiles;

import android.os.Bundle;
import android.util.Log;
import android.Manifest;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.content.pm.PackageManager;

import com.example.hodofiles.databinding.ActivityMapsBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import java.util.Arrays;
import java.util.Locale;

public class MapsActivity  extends FragmentActivity implements OnMapReadyCallback {

    private LatLng latLng;
    static GoogleMap mMap;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Sets the content view for the maps fragment
        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapView mapFragment =  findViewById(R.id.mapView);
        if (mapFragment != null) mapFragment.getMapAsync(this);
        mMap.


        // Calls the method that sets up the autocomplete search bar for google maps
        setAutoCompleteFragment();



    }
    public void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Checks if the user gave permission for their location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            latLng = new LatLng(0, 0);
            // Once the location is obtained, display the marker on the map.
            displayCurrentLocation();
            return;
        }

        // Gets the users location if they have allowed access.
        fusedLocationClient.getLastLocation().addOnSuccessListener(this,
                location -> {
                    // Gets the users current location (latitude and longitude)
                    if (location != null) {
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    } else {
                        latLng = new LatLng(0, 0);
                    }
                    // Once the location is obtained, display the marker on the map.
                    displayCurrentLocation();
                }
        );
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        getCurrentLocation();
        setOnClickIcon();

    }


    public void displayCurrentLocation() {
        if (marker != null)
            marker.remove();

        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map)));
        // Moves the camera (what the user can see), to the marker and zooms in so that streets can be seen
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    public void setOnClickIcon() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker)
            {
                {

                }
                return false;
            }
        });
    }


    /**
     * setAutoCompleteFragment - sets up the search bar at the top of the map, which allows the
     * user to search for locations around the world.
     * */
    public void setAutoCompleteFragment() {
        // Checks if places have been initialised yet
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyA5pUxD_2Xi1s-bga4itPVaq-VblEHmxg8", Locale.US);
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        if (autocompleteFragment != null) autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        if (autocompleteFragment != null) {
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                /**
                 * onPlaceSelected - when the user selects a place from the autocomplete fragment (searchbar)
                 * @param place the location that they selected
                 * */
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    latLng = place.getLatLng();
                    displayCurrentLocation();
                }

                /**
                 * onError - if an error occurs when a place is selected
                 * @param status the result of the place selection which caused an error
                 * */
                @Override
                public void onError(@NonNull Status status) {
                    String TAG = "AutoComplete";
                    Log.i(TAG, "Error: " + status);
                }
            });
        }
    }

}