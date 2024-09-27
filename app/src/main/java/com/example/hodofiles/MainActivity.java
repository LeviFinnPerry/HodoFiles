package com.example.hodofiles;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
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
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    //Declare viewable models
    private MapsViewModel mapsViewModel;
    private SearchFeedViewModel searchFeedViewModel;
    private ItineraryViewModel itineraryViewModel;
    private SettingsViewModel settingsViewModel;

    //Declare fragments
    private MapsFragment mapsFragment;
    private SearchFeedFragment searchFeedFragment;
    private ItineraryFragment itineraryFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup Main View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }

}