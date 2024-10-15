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

    private final List<ItineraryFolder> folders;
    private final OnFolderClickListener onFolderClickListener;

    public interface OnFolderClickListener {
        void onFolderClick(ItineraryFolder folder);
    }

    public FolderAdapter(List<ItineraryFolder> folders, OnFolderClickListener listener) {
        this.folders = folders;
        this.onFolderClickListener = listener;
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
            Log.d("FolderAdapter", "Folder clicked: " + folder.getName());
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
