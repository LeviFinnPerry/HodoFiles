package com.example.hodofiles.ui.searchfeed;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.etsy.android.grid.StaggeredGridView;
import com.example.hodofiles.ui.searchfeed.PlaceDetailActivity;

import android.util.Log;
import android.view.Menu;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.example.hodofiles.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SearchFeedFragment extends Fragment implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "SearchFeedFragment";
    public static final String SAVED_DATA_KEY = "SAVED_DATA";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String DEFAULT_QUERY = "beach";

    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private SearchFeedAdapter mAdapter;
    private ArrayList<String> mData;
    private ArrayList<Place> topPlaces = new ArrayList<>();
    private PlacesClient placesClient;
    private String query;
    private boolean isLoading = false; // To prevent multiple triggers
    private boolean hasNextPage = true;


    // onCreateView is used to inflate the layout of the Fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), "AIzaSyAmeofIZbv8pgpxghqjKb_WOw_M6KSZ9So");
        }

        placesClient = Places.createClient(getContext());

        View rootView = inflater.inflate(R.layout.fragment_searchfeed, container, false); // Changed to Fragment layout

        // Search view
        SearchView searchView = rootView.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Handle search query
                fetchTopPlaces(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Handle live query changes if needed
                return false;
            }
        });

        // Filter buttons
        Button museumButton = rootView.findViewById(R.id.searchfeed_container).findViewById(R.id.category_museum);
        museumButton.setOnClickListener(view -> {
            //Fetch or filter places related to museum
            query ="museum";
            fetchTopPlaces(query);
        });

        Button parkButton = rootView.findViewById(R.id.searchfeed_container).findViewById(R.id.category_park);
        museumButton.setOnClickListener(view -> {
            //Fetch or filter places related to museum
            query = "park";
            fetchTopPlaces(query);
        });

        Button restaurantButton = rootView.findViewById(R.id.searchfeed_container).findViewById(R.id.category_restaurant);
        museumButton.setOnClickListener(view -> {
            //Fetch or filter places related to museum
            query = "museum";
            fetchTopPlaces(query);
        });

        // Initialize grid view and adapter
        mGridView = (StaggeredGridView) rootView.findViewById(R.id.grid_searchfeed);
        mAdapter = new SearchFeedAdapter(getActivity());

        fetchTopPlaces(DEFAULT_QUERY);

        mGridView.setAdapter(mAdapter);

        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SAVED_DATA_KEY, mData);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d("ScrollStateChanged", "scrollState: " + scrollState);

        if (scrollState == SCROLL_STATE_IDLE) {
            Log.d("ScrollStateChanged", "Scroll state is idle");
            if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                Log.d("ScrollStateChanged", "At bottom, fetching more places");
                fetchMorePlaces();
            }
        } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            Log.d("ScrollStateChanged", "User is scrolling");
        } else if (scrollState == SCROLL_STATE_FLING) {
            Log.d("ScrollStateChanged", "List is fling");
        }
    }



    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d("Scroll", "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);

        // Load more items if needed
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d(TAG, "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                //fetchMorePlaces();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(getContext(), "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
            Place selectedPlace = topPlaces.get(position);  // Assuming `topPlaces` holds your place data
            openPlaceDetailsActivity(selectedPlace);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getContext(), "Item Long Clicked: " + position, Toast.LENGTH_SHORT).show();
        return true;
    }

    // Optional: Override onCreateOptionsMenu and onOptionsItemSelected if you need menu handling
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sgv_dynamic, menu);
    }

    /**
     @Override public boolean onOptionsItemSelected(MenuItem item) {
     switch (item.getItemId()) {
     case R.id.col1:
     mGridView.setColumnCount(1);
     break;
     case R.id.col2:
     mGridView.setColumnCount(2);
     break;
     case R.id.col3:
     mGridView.setColumnCount(3);
     break;
     }
     return true;
     }
     */

    private void openAutocomplete() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(requireContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("SearchFeedFragment", "Place: " + place.getName() + ", " + place.getId());

                // Fetch popular places using the selected place's location
                fetchTopPlaces(DEFAULT_QUERY);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("SearchFeedFragment", "Error: " + status.getStatusMessage());
            }
        }
    }

    private void fetchTopPlaces(String query) {
        // Set up the keyword or query for the places you want to fetch
        String setQuery = query;

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setQuery(setQuery)
                .setLocationBias(null) // No boundaries for location
                .build();

        PlacesClient placesClient = Places.createClient(getContext());

        placesClient.findAutocompletePredictions(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FindAutocompletePredictionsResponse response = task.getResult();
                if (response != null && !response.getAutocompletePredictions().isEmpty()) {
                    List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                    topPlaces.clear();
                    topPlaces = new ArrayList<>();

                    for (AutocompletePrediction prediction : predictions) {
                        String placeId = prediction.getPlaceId();
                        // Fetch place details for each prediction
                        fetchPlaceDetails(placeId, topPlaces, () -> {
                            updateSearchFeed(topPlaces);
                        });
                    }

                    updateSearchFeed(topPlaces);
                } else {
                    Log.d("PlacesAPI", "No predictions found");
                }
            } else {
                Log.e("PlacesAPI", "Error fetching predictions", task.getException());
            }
        });
    }



    private void fetchPlaceDetails(String placeId, ArrayList<Place> newPlaces, Runnable callback) {
        // Check if placeId already exists
        for (Place existingPlace : topPlaces) {
            if (existingPlace.getId().equals(placeId)) {
                return;
            }
        }

        // Specify which fields to retrieve for the place
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG,
                Place.Field.PHOTO_METADATAS, Place.Field.ADDRESS, Place.Field.OPENING_HOURS, Place.Field.PHONE_NUMBER);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener(response -> {
            Place place = response.getPlace();
            newPlaces.add(place);
            Log.d("PlacesAPI", "Fetched place details for: " + place.getName());
            // Call the callback when done fetching a place
            callback.run();
        }).addOnFailureListener(e -> {
            Log.e("PlacesAPI", "Error fetching place details", e);
        });
    }


    private void updateSearchFeed(List<Place> nearbyPlaces) {
        // Log the number of places received
        Log.d("SearchFeed", "Number of places received: " + nearbyPlaces.size());

        // Clear the adapter's old data
        mAdapter.clearPlaces();

        // Loop through the nearby places and add them to your adapter
        for (Place place : nearbyPlaces) {
            // Log the place details for each place
            String placeName = place.getName();
            Log.d("SearchFeed", "Processing place: " + placeName);

            LatLng placeLatLng = place.getLatLng();
            Log.d("SearchFeed", "Place LatLng: " + placeLatLng);

            // Fetch and display the place image
            List<PhotoMetadata> photoMetadataList = place.getPhotoMetadatas();
            if (photoMetadataList != null && !photoMetadataList.isEmpty()) {
                PhotoMetadata photoMetadata = photoMetadataList.get(0);
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata).build();

                placesClient.fetchPhoto(photoRequest).addOnSuccessListener(fetchPhotoResponse -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();

                    // Log when a photo is fetched successfully
                    Log.d("SearchFeed", "Fetched photo for place: " + placeName);

                    // Set place name and image to your adapter
                    mAdapter.addPlaceWithPhoto(place, bitmap);
                    Log.d("SearchFeed", "Added place to adapter: " + placeName);
                }).addOnFailureListener(e -> {
                    // Log any errors while fetching the photo
                    Log.e("SearchFeed", "Failed to fetch photo for place: " + placeName, e);
                });
            } else {
                Log.d("SearchFeed", "No photos available for place: " + placeName);
            }
        }

        // Log before notifying the adapter
        Log.d("SearchFeed", "Notifying adapter that data has changed");
        mAdapter.notifyDataSetChanged(); // Update the grid/list view
        //mHasRequestedMore = false;
    }

    private int pageCounter = 1; // Track how many times you've loaded more data

    private void fetchMorePlaces() {
        if (isLoading || !hasNextPage) {
            return; // Prevent multiple triggers
        }

        isLoading = true; // Set loading flag to prevent multiple triggers

        // Modify the query to fetch new data (append page number or other variation)
        String modifiedQuery = query + pageCounter;

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(modifiedQuery)
                .setLocationBias(null)  // Adjust if needed, or add bias for location
                .build();

        placesClient.findAutocompletePredictions(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FindAutocompletePredictionsResponse response = task.getResult();
                List<AutocompletePrediction> predictions = response.getAutocompletePredictions();

                if (predictions != null && !predictions.isEmpty()) {
                    ArrayList<Place> newPlaces = new ArrayList<>();
                    for (AutocompletePrediction prediction : predictions) {
                        String placeId = prediction.getPlaceId();
                        fetchPlaceDetails(placeId, newPlaces, () -> {
                            // Callback when new place details are fetched
                            updateSearchFeed(newPlaces);
                        });
                    }

                    // Append new places to the existing list
                    topPlaces.addAll(newPlaces);

                    pageCounter++; // Increment page count
                    mHasRequestedMore = false; // Reset scroll flag
                } else {
                    hasNextPage = false; // Stop if no more predictions
                }
            } else {
                Log.e("PlacesAPI", "Error fetching more places");
            }

            isLoading = false; // Reset loading flag
        });
    }

    private void openPlaceDetailsActivity(Place place) {
        Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);

        // Set other place details
        intent.putExtra("PLACE_NAME", place.getName());
        intent.putExtra("PLACE_ADDRESS", place.getAddress());
        intent.putExtra("PLACE_CONTACT", place.getPhoneNumber()); // You can fetch contact from place object
        intent.putExtra("PLACE_ID", place.getId());

        startActivity(intent);
    }


}

