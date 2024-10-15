package com.example.hodofiles.ui.Itinerary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hodofiles.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
        holder.folderName.setText(folder.getName());

        String formattedDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                .format(folder.getCreationDate());
        holder.creationDate.setText("Created on: " + formattedDate);

        // Handle the click event and pass the folder directly
        holder.itemView.setOnClickListener(v -> folderClickListener.onFolderClick(folder));
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView folderName;
        TextView creationDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folder_name);
            creationDate = itemView.findViewById(R.id.folder_creation_date);
        }
    }
}