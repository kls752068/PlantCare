package com.example.plantcare;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Activity 2: Add a plant to SQLite.
public class AddPlantActivity extends BaseActivity {
    EditText name, type, days, notes;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        name = findViewById(R.id.editName);
        type = findViewById(R.id.editType);
        days = findViewById(R.id.editDays);
        notes = findViewById(R.id.editNotes);
        Button save = findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String plantName = name.getText().toString().trim();
                String plantType = type.getText().toString().trim();
                String intervalText = days.getText().toString().trim();
                if (plantName.isEmpty() || plantType.isEmpty() || intervalText.isEmpty()) {
                    Toast.makeText(AddPlantActivity.this, "Enter name, type and days", Toast.LENGTH_SHORT).show();
                    return;
                }
                int interval;
                try { interval = Integer.parseInt(intervalText); }
                catch (NumberFormatException e) {
                    Toast.makeText(AddPlantActivity.this, "Invalid number of days", Toast.LENGTH_SHORT).show(); return;
                }
                if (interval < 1 || interval > 365) {
                    Toast.makeText(AddPlantActivity.this, "Days must be between 1 and 365", Toast.LENGTH_SHORT).show(); return;
                }
                DataManager dm = new DataManager(AddPlantActivity.this);
                dm.insert(plantName, plantType, interval, DateHelper.today(), notes.getText().toString().trim());
                dm.close();
                Toast.makeText(AddPlantActivity.this, "Plant saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    // Return to the previous Activity without creating a new one.
    public void goBack(View view) {
        finish();
    }
}
