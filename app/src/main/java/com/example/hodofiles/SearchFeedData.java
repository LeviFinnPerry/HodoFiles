package com.example.hodofiles;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchFeedData {
    public static final int SAMPLE_DATA_ITEM_COUNT = 30;

    //Sample data, to be changed into data from using Google Places
    public static ArrayList<String> generatePlaces(Context context) {
        final ArrayList<String> data = new ArrayList<String>(SAMPLE_DATA_ITEM_COUNT);
        String[] places = context.getResources().getStringArray(R.array.sample_searchfeed);
        for (int i = 0; i < places.length; i++) {
            data.add(places[i]);
        }
        return data;
    }
}
