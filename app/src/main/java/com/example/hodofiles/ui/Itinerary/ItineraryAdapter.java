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

    private final List<ItineraryFolder> folders;
    private final OnFolderClickListener folderClickListener;

    public interface OnFolderClickListener {
        void onFolderClick(ItineraryFolder folder);
    }

    public ItineraryAdapter(List<ItineraryFolder> folders, OnFolderClickListener folderClickListener) {
        this.folders = folders;
        this.folderClickListener = folderClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_itinerary_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItineraryFolder folder = folders.get(position);

        // Set folder name
        holder.folderName.setText(folder.getName());

        // Handle the click event and pass the folder directly
        holder.itemView.setOnClickListener(v -> folderClickListener.onFolderClick(folder));
    }

    @Override
    public int getItemCount() {
        // Return the number of folders in the list
        return folders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView folderName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folder_name);
        }
    }
}
