package com.example.hodofiles.ui.Itinerary;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hodofiles.R;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private List<ItineraryFolder> folders;
    private final OnFolderClickListener onFolderClickListener;

    public interface OnFolderClickListener {
        void onFolderClick(ItineraryFolder folder);
    }

    public FolderAdapter(List<ItineraryFolder> folders, OnFolderClickListener listener) {
        this.folders = folders;
        this.onFolderClickListener = listener;
    }

    // Method to add a new folder to the list
    public void addFolder(ItineraryFolder folder) {
        folders.add(folder);
        notifyItemInserted(folders.size() - 1);  // Notify that a new item has been inserted
    }

    // Method to update the entire list of folders
    public void updateFolders(List<ItineraryFolder> updatedFolders) {
        this.folders = updatedFolders;
        notifyDataSetChanged();  // Notify that the entire dataset has changed
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
        holder.folderName.setText(folder.getName());

        // Set click listener to handle folder selection
        holder.itemView.setOnClickListener(v -> {
            onFolderClickListener.onFolderClick(folder);  // Invoke the click listener
        });
    }

    @Override
    public int getItemCount() {
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
