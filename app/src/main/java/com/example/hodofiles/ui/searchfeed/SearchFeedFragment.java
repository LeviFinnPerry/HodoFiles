package com.example.hodofiles.ui.searchfeed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hodofiles.R;
import com.example.hodofiles.databinding.FragmentSearchfeedBinding;
import com.example.hodofiles.ui.maps.MapsViewModel;

public class SearchFeedFragment extends Fragment {

    //Initialise View Model
    private SearchFeedViewModel searchFeedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}