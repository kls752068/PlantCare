package com.example.plantcare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

// Binds stored plants to reusable card rows.
public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {
    private final Context context;
    private final ArrayList<Plant> plants;
    public PlantAdapter(Context context, ArrayList<Plant> plants) {
        this.context = context;
        this.plants = plants;
    }
    public static class PlantViewHolder extends RecyclerView.ViewHolder {
        TextView name, type, status;
        public PlantViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.rowName);
            type = view.findViewById(R.id.rowType);
            status = view.findViewById(R.id.rowStatus);
        }
    }
    @Override public PlantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_plant, parent, false);
        return new PlantViewHolder(view);
    }
    @Override public void onBindViewHolder(PlantViewHolder holder, int position) {
        final Plant plant = plants.get(position);
        holder.name.setText(plant.name);
        holder.type.setText(plant.type + "  •  Every " + plant.days + " days");
        holder.status.setText(DateHelper.status(plant));
        holder.status.setTextColor(context.getResources().getColor(
                DateHelper.daysUntilWatering(plant) <= 0 ? R.color.red : R.color.leaf));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("plant_id", plant.id);
                context.startActivity(intent);
            }
        });
    }
    @Override public int getItemCount() { return plants.size(); }
}
