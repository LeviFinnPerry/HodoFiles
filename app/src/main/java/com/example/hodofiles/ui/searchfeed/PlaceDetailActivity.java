package com.example.hodofiles.ui.searchfeed;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hodofiles.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class PlaceDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        Button backButton = findViewById(R.id.btn_back_searchfeed);
        backButton.setOnClickListener(v -> {
            finish();
        });

        // Get the data passed from the search feed
        Intent intent = getIntent();
        String placeId = intent.getStringExtra("PLACE_ID");
        String openingHours = intent.getStringExtra("OPENING_HOURS");

        // Find views and populate with data
        TextView placeNameTextView = findViewById(R.id.place_detail_name);
        TextView addressTextView = findViewById(R.id.place_address);
        TextView openingHoursTextView = findViewById(R.id.place_opening_hours);
        TextView contactTextView = findViewById(R.id.place_contact);
        ImageView placeImageView = findViewById(R.id.place_image);

        if (placeId != null) {
            PlacesClient placesClient = Places.createClient(this);

            List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG,
                    Place.Field.PHOTO_METADATAS, Place.Field.ADDRESS, Place.Field.OPENING_HOURS, Place.Field.PHONE_NUMBER);

            FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
            placesClient.fetchPlace(request).addOnSuccessListener(response -> {
                        Place place = response.getPlace();

            List<PhotoMetadata> photoMetadataList = place.getPhotoMetadatas();
            if (photoMetadataList != null && !photoMetadataList.isEmpty()) {
                PhotoMetadata photoMetadata = photoMetadataList.get(0);
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500)
                        .setMaxHeight(300)
                        .build();

                placesClient.fetchPhoto(photoRequest).addOnSuccessListener(fetchPhotoResponse -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();

                    // Set place name and image
                    placeImageView.setImageBitmap(bitmap);
                    placeNameTextView.setText(place.getName());
                    addressTextView.setText("Address: " + place.getAddress());
                    //openingHoursTextView.setText("Opening Hours: " + place.getOpeningHours().getWeekdayText().get(0));
                    //contactTextView.setText(place.getPhoneNumber());
                }).addOnFailureListener(e -> {
                    // Log any errors while fetching the photo
                    Log.e("SearchFeed", "Failed to fetch photo for place: " + place.getName(), e);
                });
                }
            });
        }
    }

}

