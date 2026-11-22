package com.example.plantcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

// Activity 3: Shows all plants and searches SQLite in the app UI.
public class PlantListActivity extends BaseActivity {
    RecyclerView recyclerView;
    EditText search;
    TextView empty;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);
        recyclerView = findViewById(R.id.recyclerPlants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        search = findViewById(R.id.editSearch);
        empty = findViewById(R.id.textEmpty);
        Button find = findViewById(R.id.buttonSearch);
        Button all = findViewById(R.id.buttonAll);
        find.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { loadPlants(search.getText().toString().trim()); }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { search.setText(""); loadPlants(""); }
        });
    }
    @Override protected void onResume() { super.onResume(); loadPlants(search.getText().toString().trim()); }
    private void loadPlants(String query) {
        DataManager dm = new DataManager(this);
        ArrayList<Plant> plants = query.isEmpty() ? dm.selectAll() : dm.search(query);
        dm.close();
        empty.setVisibility(plants.isEmpty() ? View.VISIBLE : View.GONE);
        empty.setText(query.isEmpty() ? "Your garden is empty. Add your first plant!"
                : "No matching plants. Try another name or type.");
        recyclerView.setAdapter(new PlantAdapter(this, plants));
    }
    // Return to the previous Activity without creating a new one.
    public void goBack(View view) {
        finish();
    }
}
