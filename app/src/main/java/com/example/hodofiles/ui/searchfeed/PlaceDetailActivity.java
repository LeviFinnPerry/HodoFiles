package com.example.hodofiles.ui.searchfeed;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hodofiles.R;
import com.example.hodofiles.ui.Itinerary.FolderSelectionFragment;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class PlaceDetailActivity extends AppCompatActivity {

    private String placeName;  // Define placeName and address as fields
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        // Initialize the back button
        Button backButton = findViewById(R.id.btn_back_searchfeed);
        backButton.setOnClickListener(v -> finish());

        // Initialize views
        TextView placeNameTextView = findViewById(R.id.place_detail_name);
        TextView addressTextView = findViewById(R.id.place_address);
        TextView openingHoursTextView = findViewById(R.id.place_opening_hours);
        TextView contactTextView = findViewById(R.id.place_contact);
        ImageView placeImageView = findViewById(R.id.place_image);
        Button addToItineraryButton = findViewById(R.id.btn_add_itinerary);

        // Get the place ID from the intent
        Intent intent = getIntent();
        String placeId = intent.getStringExtra("PLACE_ID");

        if (placeId != null) {
            PlacesClient placesClient = Places.createClient(this);

            List<Place.Field> placeFields = Arrays.asList(
                    Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.PHOTO_METADATAS, Place.Field.OPENING_HOURS,
                    Place.Field.PHONE_NUMBER
            );

            // Fetch place details using the PlacesClient
            FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
            placesClient.fetchPlace(request).addOnSuccessListener(response -> {
                Place place = response.getPlace();

                // Assign place name and address to fields for later use
                placeName = place.getName();
                address = place.getAddress();

                // Update UI with the fetched place details
                placeNameTextView.setText(placeName);
                addressTextView.setText("Address: " + address);

                if (place.getOpeningHours() != null) {
                    openingHoursTextView.setText("Opening Hours: "
                            + place.getOpeningHours().getWeekdayText().get(0));
                }

                contactTextView.setText(place.getPhoneNumber());

                // Fetch the first available photo, if any
                List<PhotoMetadata> photoMetadataList = place.getPhotoMetadatas();
                if (photoMetadataList != null && !photoMetadataList.isEmpty()) {
                    PhotoMetadata photoMetadata = photoMetadataList.get(0);
                    FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                            .setMaxWidth(500)
                            .setMaxHeight(300)
                            .build();

                    placesClient.fetchPhoto(photoRequest).addOnSuccessListener(fetchPhotoResponse -> {
                        Bitmap bitmap = fetchPhotoResponse.getBitmap();
                        placeImageView.setImageBitmap(bitmap);
                    }).addOnFailureListener(e ->
                            Log.e("PlaceDetailActivity", "Failed to fetch photo", e)
                    );
                }
            }).addOnFailureListener(e ->
                    Log.e("PlaceDetailActivity", "Failed to fetch place details", e)
            );
        }

        // Add logic for "Add to Itinerary" button click
        addToItineraryButton.setOnClickListener(v -> {
            if (placeName != null && address != null) {
                // Create and show the FolderSelectionFragment
                FolderSelectionFragment fragment = new FolderSelectionFragment();

                // Pass the place details to the fragment via arguments
                Bundle args = new Bundle();
                args.putString("PLACE_NAME", placeName);
                args.putString("ADDRESS", address);
                fragment.setArguments(args);

                // Show the folder selection dialog fragment
                fragment.show(getSupportFragmentManager(), "FolderSelection");
            } else {
                // Log error if place details are missing
                Log.e("PlaceDetailActivity", "Place name or address is missing.");
            }
        });
    }
}
