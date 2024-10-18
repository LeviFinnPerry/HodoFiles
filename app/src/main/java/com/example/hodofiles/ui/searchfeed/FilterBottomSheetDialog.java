package com.example.hodofiles.ui.searchfeed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hodofiles.R;
import com.example.hodofiles.ui.settings.InterestAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterBottomSheetDialog extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private FilterAdapter filterAdapter;  // Reuse your existing adapter

    public FilterBottomSheetDialog() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_bottom_sheet, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns
        recyclerView.setAdapter(new FilterAdapter(getContext(), new FilterAdapter.OnFilterSelectedListener() {
            @Override
            public void onFilterSelected(String selectedTag) {
                // Pass the tag to the SearchFeedFragment
                Bundle result = new Bundle();
                result.putString("selected_tag", selectedTag);
                getParentFragmentManager().setFragmentResult("requestKey", result);
                dismiss();
            }
        }));

        return view;
    }
}

