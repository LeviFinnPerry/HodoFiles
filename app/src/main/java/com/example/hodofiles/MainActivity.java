package com.example.hodofiles;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.example.hodofiles.ui.Itinerary.ItineraryFragment;
import com.example.hodofiles.ui.maps.MapsFragment;
import com.example.hodofiles.ui.searchfeed.SearchFeedFragment;
import com.example.hodofiles.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    //Declare viewable models
    /**
    private MapsViewModel mapsViewModel;
    private SearchFeedViewModel searchFeedViewModel;
    private ItineraryViewModel itineraryViewModel;
    private SettingsViewModel settingsViewModel;
    */

    //Declare fragments
    //private MapsFragment mapsFragment;
    private SearchFeedFragment searchfeedFragment;
    public static LatLng searchfeedLatlng;
    //private ItineraryFragment itineraryFragment;
    //private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set default fragment when the app loads
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapsFragment()).commit();
        }

        //Handle bottom navigation item clicks
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    /**
                     switch (item.getItemId()) {
                     case R.id.navigation_map:
                     selectedFragment = new MapFragment();
                     break;
                     case R.id.navigation_searchfeed:
                     selectedFragment = new SearchfeedFragment();
                     break;
                     case R.id.navigation_itinerary:
                     selectedFragment = new ItineraryFragment();
                     break;
                     case R.id.navigation_settings:
                     selectedFragment = new SettingsFragment();
                     break;
                     }
                     */

                    if (item.getItemId() == R.id.nav_map) {
                        selectedFragment = new MapsFragment();
                    } else if (item.getItemId() == R.id.nav_searchfeed){
                        selectedFragment = new SearchFeedFragment();
                    } else if (item.getItemId() == R.id.nav_itinerary){
                        selectedFragment = new ItineraryFragment();
                    } else if (item.getItemId() == R.id.nav_settings){
                        selectedFragment = new SettingsFragment();
                    }

                    //Replace current fragment with the selected one
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };

}