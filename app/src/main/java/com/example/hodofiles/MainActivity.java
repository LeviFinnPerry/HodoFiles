package com.example.hodofiles;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.hodofiles.ui.Itinerary.ItineraryFragment;
import com.example.hodofiles.ui.Itinerary.ItineraryViewModel;
import com.example.hodofiles.ui.maps.MapsFragment;
import com.example.hodofiles.ui.maps.MapsViewModel;
import com.example.hodofiles.ui.searchfeed.SearchFeedFragment;
import com.example.hodofiles.ui.searchfeed.SearchFeedViewModel;
import com.example.hodofiles.ui.settings.SettingsFragment;
import com.example.hodofiles.ui.settings.SettingsViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Removes/Hides the action bar from the screen
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) actionbar.hide();

        // Checks if location has been granted
        checkLocationPermissionGranted();

        Intent swap = new Intent(this, MapsActivity.class);
        startActivity(swap);
    }

    public void checkLocationPermissionGranted() {
        // Checks if the location permissions have been granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // If access to the users location hasn't been specified, a request is then generated.
            ActivityResultLauncher<String[]> locationPermissionRequest =
                    registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                        result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                        result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                    });

            // Generate the location permission request to the user
            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }



}