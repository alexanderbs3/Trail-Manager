package com.example.trailmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trailmanager.R;
import com.example.trailmanager.models.Trail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrailAdapter extends RecyclerView.Adapter<TrailAdapter.TrailViewHolder> {

    private Context context;
    private List<Trail> trails;
    private OnTrailClickListener listener;
    private SimpleDateFormat dateFormat;

    public interface OnTrailClickListener {
        void onTrailClick(Trail trail);
        void onEditClick(Trail trail);
        void onDeleteClick(Trail trail);
        void onShareClick(Trail trail);
    }

    public TrailAdapter(Context context, OnTrailClickListener listener) {
        this.context = context;
        this.trails = new ArrayList<>();
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    public void setTrails(List<Trail> trails) {
        this.trails = trails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trail, parent, false);
        return new TrailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailViewHolder holder, int position) {
        Trail trail = trails.get(position);

        holder.tvName.setText(trail.getName());
        holder.tvDate.setText(dateFormat.format(trail.getStartTime()));
        holder.tvDistance.setText(trail.getFormattedDistance());
        holder.tvDuration.setText(trail.getFormattedDuration());
        holder.tvCalories.setText(trail.getFormattedCalories());
        holder.tvAvgSpeed.setText("Média: " + trail.getFormattedAvgSpeed());

        holder.itemView.setOnClickListener(v -> listener.onTrailClick(trail));
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(trail));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(trail));
        holder.btnShare.setOnClickListener(v -> listener.onShareClick(trail));
    }

    @Override
    public int getItemCount() {
        return trails.size();
    }

    static class TrailViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDate;
        TextView tvDistance;
        TextView tvDuration;
        TextView tvCalories;
        TextView tvAvgSpeed;
        ImageButton btnEdit;
        ImageButton btnDelete;
        ImageButton btnShare;

        TrailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTrailName);
            tvDate = itemView.findViewById(R.id.tvTrailDate);
            tvDistance = itemView.findViewById(R.id.tvTrailDistance);
            tvDuration = itemView.findViewById(R.id.tvTrailDuration);
            tvCalories = itemView.findViewById(R.id.tvTrailCalories);
            tvAvgSpeed = itemView.findViewById(R.id.tvTrailAvgSpeed);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnShare = itemView.findViewById(R.id.btnShare);
        }
    }
}