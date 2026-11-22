package com.example.plantcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// Activity 1: Home screen
public class MainActivity extends BaseActivity {
    TextView summary;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        summary = findViewById(R.id.textSummary);
        Button add = findViewById(R.id.buttonAdd);
        Button list = findViewById(R.id.buttonList);
        Button schedule = findViewById(R.id.buttonSchedule);
        add.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { startActivity(new Intent(MainActivity.this, AddPlantActivity.class)); }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { startActivity(new Intent(MainActivity.this, PlantListActivity.class)); }
        });
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { startActivity(new Intent(MainActivity.this, ScheduleActivity.class)); }
        });
    }
    @Override protected void onResume() {
        super.onResume();
        DataManager dm = new DataManager(this);
        int count = dm.selectAll().size();
        dm.close();
        summary.setText(count + (count == 1 ? " plant" : " plants"));
    }
}
