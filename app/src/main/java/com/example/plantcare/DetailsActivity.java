package com.example.plantcare;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// Activity 4: Receives the ID using Intent extras, waters or deletes a plant.
public class DetailsActivity extends BaseActivity {
    private int plantId;
    private TextView details;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        details = findViewById(R.id.textDetails);
        plantId = getIntent().getIntExtra("plant_id", -1);
        if (plantId == -1) { finish(); return; }
        Button water = findViewById(R.id.buttonWater);
        Button delete = findViewById(R.id.buttonDelete);
        water.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                DataManager dm = new DataManager(DetailsActivity.this);
                dm.waterNow(plantId);
                dm.close();
                Toast.makeText(DetailsActivity.this, "Watering recorded", Toast.LENGTH_SHORT).show();
                showPlant();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                DeleteDialog dialog = new DeleteDialog();
                dialog.show(getSupportFragmentManager(), "delete_confirmation");
            }
        });
        showPlant();
    }
    private void showPlant() {
        DataManager dm = new DataManager(this);
        Plant p = dm.findById(plantId);
        dm.close();
        if (p == null) { finish(); return; }
        details.setText(
                p.name + "\n" +
                "Type: " + p.type + "\n" +
                "Water every: " + p.days + " days\n" +
                "Last watered: " + p.lastWatered + "\n\n" +
                DateHelper.status(p) + "\n\n" +
                "CARE NOTES\n" + (p.notes.isEmpty() ? "No notes added" : p.notes)
        );
    }
    // Called by the DialogFragment when the user confirms deletion.
    public void deletePlant() {
        DataManager dm = new DataManager(this);
        dm.delete(plantId);
        dm.close();
        Toast.makeText(this, "Plant deleted", Toast.LENGTH_SHORT).show();
        finish();
    }
    // Return to the previous Activity without creating a new one.
    public void goBack(View view) {
        finish();
    }
}
