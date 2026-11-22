package com.example.plantcare;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

// Activity 5: Plants whose watering date is today or overdue.
public class ScheduleActivity extends BaseActivity {
    RecyclerView recycler;
    TextView empty;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        recycler = findViewById(R.id.recyclerDue);
        empty = findViewById(R.id.textDueEmpty);
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override protected void onResume() {
        super.onResume();
        DataManager dm = new DataManager(this);
        ArrayList<Plant> all = dm.selectAll();
        dm.close();
        ArrayList<Plant> due = new ArrayList<>();
        for (Plant plant : all) {
            if (DateHelper.daysUntilWatering(plant) <= 0) due.add(plant);
        }
        empty.setVisibility(due.isEmpty() ? View.VISIBLE : View.GONE);
        recycler.setAdapter(new PlantAdapter(this, due));
    }
    // Return to the previous Activity without creating a new one.
    public void goBack(View view) {
        finish();
    }
}
