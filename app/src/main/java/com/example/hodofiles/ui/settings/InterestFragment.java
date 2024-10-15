package com.example.hodofiles.ui.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hodofiles.R;

import java.util.ArrayList;
import java.util.List;

public class InterestFragment extends Fragment {
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest, container, false);
         RecyclerView recyclerView = view.findViewById(R.id.recycler_view_interest);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Set the adapter to the RecyclerView
        InterestAdapter adapter = new InterestAdapter(getInterests());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<Interest> getInterests() {
        List<Interest> interests = new ArrayList<>();
        interests.add(new Interest("Beach", R.drawable.beach_coast));
        interests.add(new Interest("Park", R.drawable.hamilton_gardens));
        interests.add(new Interest("Museum", R.drawable.museum));
        interests.add(new Interest("Tourism", R.drawable.sky_tower));
        return interests;
    }


}
