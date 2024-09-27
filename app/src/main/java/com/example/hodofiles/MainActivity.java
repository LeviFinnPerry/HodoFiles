package com.example.hodofiles;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

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

        //Initialise View Models
        mapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);
        searchFeedViewModel = new ViewModelProvider(this).get(SearchFeedViewModel.class);
        itineraryViewModel = new ViewModelProvider(this).get(ItineraryViewModel.class);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        //Setup BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the default fragment (MapsFragment)
        if (savedInstanceState == null) {
            //Initialise fragments as it is first run
            mapsFragment = new MapsFragment();
            searchFeedFragment = new SearchFeedFragment();
            itineraryFragment = new ItineraryFragment();
            settingsFragment = new SettingsFragment();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapsFragment()).commit();
        }

    }

}