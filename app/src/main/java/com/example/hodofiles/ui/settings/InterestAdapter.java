package com.example.hodofiles.ui.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hodofiles.R;

import java.util.ArrayList;
import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.InterestViewHolder> {

    private final List<Interest> interestList;
    private Context context;
    private final List<String> selectedInterests = new ArrayList<>();  // To store selected interests

    public InterestAdapter(List<Interest> interestList) {
        this.interestList = interestList;
    }

    @NonNull
    @Override
    public InterestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interest, parent, false);
        context = parent.getContext();
        return new InterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InterestViewHolder holder, int position) {
        Interest interest = interestList.get(position);
        holder.imageView.setImageResource(interest.getImageResId());
        holder.textView.setText(interest.getName());

        // Handle card selection
        holder.cardView.setOnClickListener(v -> {
            if (selectedInterests.contains(interest.getName())) {
                // Deselect the card
                selectedInterests.remove(interest.getName());
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.background_purple_light));
            } else {
                // Select the card
                selectedInterests.add(interest.getName());
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.background_purple));
            }
        });

        // Set the initial state of the card
        if (selectedInterests.contains(interest.getName())) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.background_purple));
        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.background_purple_light));
        }
    }

    @Override
    public int getItemCount() {
        return interestList.size();
    }

    public List<String> getSelectedInterests() {
        return selectedInterests;
    }

    // ViewHolder for each interest item
    public static class InterestViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CardView cardView;

        public InterestViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.interest_image);
            textView = itemView.findViewById(R.id.interest_name);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}

