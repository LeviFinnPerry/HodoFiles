package com.example.hodofiles.ui.Itinerary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hodofiles.R;

import java.util.List;

public class StopAdapter extends RecyclerView.Adapter<StopAdapter.ViewHolder> {

    private final List<ItineraryFolder.ItineraryStop> stops;

    public StopAdapter(List<ItineraryFolder.ItineraryStop> stops) {
        this.stops = stops;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_stop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItineraryFolder.ItineraryStop stop = stops.get(position);
        holder.stopName.setText(stop.getName());
        holder.stopAddress.setText(stop.getAddress());
    }

    @Override
    public int getItemCount() {
        return stops.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stopName;
        TextView stopAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stopName = itemView.findViewById(R.id.stop_name);
            stopAddress = itemView.findViewById(R.id.stop_address);
        }
    }
}
