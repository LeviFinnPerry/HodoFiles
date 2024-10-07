package com.example.hodofiles.ui.maps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hodofiles.R;
import com.example.hodofiles.databinding.FragmentMapsBinding;

public class MapsFragment extends Fragment {

    //Initialise View Model
    private MapsViewModel mapsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}