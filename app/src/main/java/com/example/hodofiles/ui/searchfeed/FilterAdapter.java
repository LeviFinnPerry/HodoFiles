package com.example.hodofiles.ui.searchfeed;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hodofiles.R;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    private String[] filterNames;
    private String[] filterTags;
    private Context context;
    private OnFilterSelectedListener listener;

    public interface OnFilterSelectedListener {
        void onFilterSelected(String selectedTag);
    }

    public FilterAdapter(Context context, OnFilterSelectedListener listener) {
        this.context = context;
        this.listener = listener;
        this.filterNames = context.getResources().getStringArray(R.array.place_type_names);;
        this.filterTags = context.getResources().getStringArray(R.array.place_types);
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_button, parent, false);
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        String filter = filterNames[position];
        holder.filterButton.setText(filter);

        // Handle button click
        holder.filterButton.setOnClickListener(v ->
                //Toast.makeText(context, "Selected: " + filter, Toast.LENGTH_SHORT).show());
                //String tag = filterTags[holder.getAdapterPosition()];
                //if (listener != null) {
                    listener.onFilterSelected(filterTags[position]));
                //}

    }

    @Override
    public int getItemCount() {
        return filterNames.length;
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder {
        Button filterButton;

        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            filterButton = itemView.findViewById(R.id.btnFilter);
        }
    }
}

