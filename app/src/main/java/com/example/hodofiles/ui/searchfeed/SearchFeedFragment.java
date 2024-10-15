package com.example.hodofiles.ui.searchfeed;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.etsy.android.grid.StaggeredGridView;

import android.util.Log;
import android.view.Menu;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.example.hodofiles.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFeedFragment extends Fragment /**implements AbsListView.OnItemClickListener*/ {

    private static final String API_KEY = "AIzaSyAmeofIZbv8pgpxghqjKb_WOw_M6KSZ9So";
    private static final String TAG = "SearchFeedFragment";
    private static final String DEFAULT_TYPE = "";
    private static final String DEFAULT_KEYWORD = "";
    private static final int DEFAULT_RADIUS = 500;

    private StaggeredGridView mGridView;
    private SearchFeedAdapter mAdapter;
    private ArrayList<PlacesResponse.PlaceResult> topPlaces;
    private PlacesClient placesClient;
    private String location;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), API_KEY);
        }

        // Get location passed from map
        location = "-33.8670522,151.1957362"; // Sydney, Australia (Lat,Lng)

        placesClient = Places.createClient(getContext());

        View rootView = inflater.inflate(R.layout.fragment_searchfeed, container, false); // Changed to Fragment layout

        // Search view
        SearchView searchView = rootView.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query; query must be a Place type or it won't work
                fetchTopPlaces(DEFAULT_TYPE, location, query);
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
            fetchTopPlaces("museum", location, DEFAULT_KEYWORD);
        });

        Button parkButton = rootView.findViewById(R.id.searchfeed_container).findViewById(R.id.category_park);
        parkButton.setOnClickListener(view -> {
            fetchTopPlaces("park", location, DEFAULT_KEYWORD);
        });

        Button restaurantButton = rootView.findViewById(R.id.searchfeed_container).findViewById(R.id.category_restaurant);
        restaurantButton.setOnClickListener(view -> {
            fetchTopPlaces("restaurant", location, DEFAULT_KEYWORD);
        });

        // Initialize grid view and adapter
        mGridView = (StaggeredGridView) rootView.findViewById(R.id.grid_searchfeed);
        mAdapter = new SearchFeedAdapter(getActivity(), selectedPlace -> {
            openPlaceDetailsActivity(selectedPlace);
        });

        fetchTopPlaces(DEFAULT_TYPE, location, DEFAULT_KEYWORD);

        mGridView.setAdapter(mAdapter);
        //mGridView.setOnItemClickListener(this);

        return rootView;
    }

    /**
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
          PlacesResponse.PlaceResult selectedPlace = topPlaces.get(position);
          openPlaceDetailsActivity(selectedPlace);
    }
    */

    private void fetchTopPlaces(String type, String location, String keyword) {
        topPlaces = new ArrayList<>();

        int radius = DEFAULT_RADIUS;

        // Create Retrofit client
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GooglePlacesAPIService service = retrofit.create(GooglePlacesAPIService.class);

        // Make API call to fetch nearby places based on type
        Call<PlacesResponse> call = service.getNearbyPlaces(location, radius, type, API_KEY, keyword);
        call.enqueue(new Callback<PlacesResponse>() {
            @Override
            public void onResponse(Call<PlacesResponse> call, Response<PlacesResponse> response) {
                if (response.isSuccessful()) {
                    PlacesResponse placesResponse = response.body();
                    if (placesResponse != null) {
                        topPlaces = (ArrayList<PlacesResponse.PlaceResult>) placesResponse.getResults();
                        updateSearchFeed(topPlaces); // Update UI with the results
                    }
                } else {
                    Log.e(TAG, "Request failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PlacesResponse> call, Throwable t) {
                Log.e(TAG, "Error fetching places: " + t.getMessage());
            }
        });
    }

    private void updateSearchFeed(List<PlacesResponse.PlaceResult> placeResults) {
        // Log the number of places received
        Log.d("SearchFeedResult", "Number of places received: " + placeResults.size());

        // Clear the adapter's old data
        mAdapter.clearPlaces();

        // Loop through the nearby places and add them to your adapter
        for (PlacesResponse.PlaceResult placeResult : placeResults) {
            // Log the place details for each place
            String placeName = placeResult.getName();
            Log.d("SearchFeed", "Processing place: " + placeName);

            // Fetch and display the place image
            List<PlacesResponse.PlaceResult.Photo> photoList = placeResult.getPhotos();
            if (photoList != null && !photoList.isEmpty()) {
                String photoReference = photoList.get(0).getPhotoReference();

                // Create a PhotoMetadata object
                PhotoMetadata photoMetadata = PhotoMetadata.builder(photoReference).build();

                // Create a photo request
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500)  // Set max width (you can adjust this)
                        .setMaxHeight(300) // Set max height (you can adjust this)
                        .build();

                placesClient.fetchPhoto(photoRequest).addOnSuccessListener(fetchPhotoResponse -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();

                    // Set place name and image to your adapter
                    mAdapter.addPlaceWithPhoto(placeResult, bitmap);
                    Log.d("SearchFeed", "Added place with photo to adapter: " + placeName);
                });
            } else {
                Log.d("SearchFeed", "No photos available for place: " + placeName);
                // Add place without a photo
                mAdapter.addPlaceWithPhoto(placeResult, null);
            }
        }

        // Notify the adapter to update the grid/list view
        Log.d("SearchFeed", "Notifying adapter that data has changed");
        mAdapter.notifyDataSetChanged();  // Update the grid/list view
    }

    private void openPlaceDetailsActivity(PlacesResponse.PlaceResult place) {
        Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);

        intent.putExtra("PLACE_ID", place.getPlaceId());

        startActivity(intent);
    }
}

