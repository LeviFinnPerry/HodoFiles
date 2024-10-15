package com.example.hodofiles.ui.searchfeed;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.example.hodofiles.R;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        Button btnGo;
    }

    // Variables to store place names and photos
    private final ArrayList<Place> places;
    private final Map<String, Bitmap> placePhotos;

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final Context mContext;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public SearchFeedAdapter(final Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        mContext = context;

        places = new ArrayList<>();
        placePhotos = new HashMap<>();
    }

    // Add a place and its photo to the adapter
    public void addPlaceWithPhoto(Place place, Bitmap photo) {
        // Check if place is already in the list
        boolean isDuplicate = false;
        for (Place p : places) {
            if (p.getId().equals(place.getId())) {
                isDuplicate = true;
                break;
            }
        }

        if (!isDuplicate) {
            places.add(place);  // Add place to the list
            placePhotos.put(place.getId(), photo);  // Store photo in the map using place ID as the key

            Log.d(TAG, "Place added: " + place.getName() + ", Photo: " + (photo != null ? "Available" : "Not available"));
            notifyDataSetChanged();  // Notify that data has changed to refresh the grid view
        }
    }

    @Override
    public int getCount() {
        return places.size();  // Return the number of places added
    }

    @Override
    public Place getItem(int position) {
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
            vh.btnGo = (Button) convertView.findViewById(R.id.btn_go);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);
        vh.imgLineOne.setHeightRatio(positionHeight);

        // Get the current place
        Place place = getItem(position);
        if (place != null) {
            String placeName = place.getName();
            Bitmap placePhoto = placePhotos.get(place.getId());

            Log.d(TAG, "Displaying place: " + placeName);

            // Set the image for the place (use a placeholder if no image is available)
            if (placePhoto != null) {
                vh.imgLineOne.setImageBitmap(placePhoto);
                vh.placeName.setText(placeName);
                Log.d(TAG, "Photo set for place: " + placeName);
            } else {
                vh.imgLineOne.setImageResource(R.drawable.museum);  // Set a placeholder image
                Log.d(TAG, "No photo available, using placeholder for place: " + placeName);
            }

            // Set the button action
            vh.btnGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Toast.makeText(mContext, "Clicked on " + placeName, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "Error: Place is null at position " + position);
        }

        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0;  // height will be 1.0 - 1.5 the width
    }

    public void clearPlaces() {
        if (places != null) {
            places.clear();
            notifyDataSetChanged();
        }
    }
}



