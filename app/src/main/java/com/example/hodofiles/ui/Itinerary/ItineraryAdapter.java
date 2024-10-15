package com.example.hodofiles.ui.Itinerary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hodofiles.R;

import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ViewHolder> {

    private final List<String> itineraries;
    private final View.OnClickListener onClickListener;

    public ItineraryAdapter(List<String> itineraries, View.OnClickListener onClickListener) {
        this.itineraries = itineraries;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_sample, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String itinerary = itineraries.get(position);
        holder.itineraryName.setText(itinerary);
    }

    @Override
    public int getItemCount() {
        return itineraries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itineraryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itineraryName = itemView.findViewById(R.id.place_name);
        }
    }
}