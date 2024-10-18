package com.example.hodofiles.ui.searchfeed;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.example.hodofiles.R;
import com.example.hodofiles.ui.Itinerary.FolderSelectionFragment;
import com.google.android.libraries.places.api.model.Place;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/***
 * ADAPTER
 */

public class SearchFeedAdapter extends BaseAdapter {

    private static final String TAG = "SearchFeedAdapter";

    static class ViewHolder {
        DynamicHeightImageView imgLineOne;
        TextView placeName;
        Button btnAdd;
    }

    private final ArrayList<PlacesResponse.PlaceResult> places;
    private final Map<String, Bitmap> placePhotos;

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final Context mContext;
    private OnItemClickListener onItemClickListener;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public interface OnItemClickListener {
        void onItemClick(PlacesResponse.PlaceResult place) throws IOException;
    }

    public SearchFeedAdapter(final Context context, OnItemClickListener listener) {
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        mContext = context;
        onItemClickListener = listener;

        places = new ArrayList<>();
        placePhotos = new HashMap<>();
    }

    // Add a place and its photo to the adapter
    public void addPlaceWithPhoto(PlacesResponse.PlaceResult placeResult, Bitmap photo) {
        // Check if place is already in the list
        boolean isDuplicate = false;
        for (PlacesResponse.PlaceResult p : places) {
            if (p.getPlaceId().equals(placeResult.getPlaceId())) {
                isDuplicate = true;
                break;
            }
        }

        if (!isDuplicate) {
            // Only display places with photos
            // Another possible option: display placeholder image saying photo is unavailable
            if (photo != null) {
                places.add(placeResult);  // Add place to the list
                placePhotos.put(placeResult.getPlaceId(), photo);  // Store photo in the map using place ID as the key
            }

            Log.d(TAG, "Place added: " + placeResult.getName() + ", Photo: " + (photo != null ? "Available" : "Not available"));
            notifyDataSetChanged();  // Notify that data has changed to refresh the grid view
        }
    }

    @Override
    public int getCount() {
        return places.size();  // Return the number of places added
    }

    @Override
    public PlacesResponse.PlaceResult getItem(int position) {
        return places.get(position);  // Get place at the given position
    }

    @Override
    public long getItemId(int position) {
        return position;  // Return position as the item ID
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_sample, parent, false);
            vh = new ViewHolder();
            vh.imgLineOne = (DynamicHeightImageView) convertView.findViewById(R.id.img_line1);
            vh.placeName = (TextView) convertView.findViewById(R.id.place_name);
            vh.btnAdd = (Button) convertView.findViewById(R.id.btn_add);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);
        vh.imgLineOne.setHeightRatio(positionHeight);

        // Get the current place
        PlacesResponse.PlaceResult place = getItem(position);
        if (place != null) {
            String placeName = place.getName();
            Bitmap placePhoto = placePhotos.get(place.getPlaceId());

            Log.d(TAG, "Displaying place: " + placeName);

            // Set the image for the place (use a placeholder if no image is available)
            if (placePhoto != null) {
                vh.imgLineOne.setImageBitmap(placePhoto);
                vh.placeName.setText(placeName);
                Log.d(TAG, "Photo set for place: " + placeName);
            } else {
                //vh.imgLineOne.setImageResource(R.drawable.museum);  // Set a placeholder image
                vh.placeName.setText(placeName);
                Log.d(TAG, "No photo available, using placeholder for place: " + placeName);
            }

            // Add to itinerary button
            vh.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (place.getName() != null && place.getAddress() != null) {
                        // Create and show the FolderSelectionFragment
                        FolderSelectionFragment fragment = new FolderSelectionFragment();

                        // Pass the place details to the fragment via arguments
                        Bundle args = new Bundle();
                        args.putString("PLACE_NAME", place.getName());
                        args.putString("ADDRESS", place.getAddress());
                        fragment.setArguments(args);

                        // Show the folder selection dialog fragment
                        //fragment.show(getSupportFragmentManager(), "FolderSelection");
                    } else {
                        // Log error if place details are missing
                        Log.e("PlaceDetailActivity", "Place name or address is missing.");
                    }
                }
            });

            // Set the click listener for each item
            convertView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    try {
                        onItemClickListener.onItemClick(places.get(position));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        return convertView;
    }

        private double getPositionRatio ( final int position){
            double ratio = sPositionHeightRatios.get(position, 0.0);
            if (ratio == 0) {
                ratio = getRandomHeightRatio();
                sPositionHeightRatios.append(position, ratio);
                Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
            }
            return ratio;
        }

        private double getRandomHeightRatio () {
            return (mRandom.nextDouble() / 2.0) + 1.0;  // height will be 1.0 - 1.5 the width
        }

        public void clearPlaces () {
            if (places != null) {
                places.clear();
                notifyDataSetChanged();
            }
        }


}



