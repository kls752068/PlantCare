package com.example.plantcare;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

// Owns local plant storage and creates demonstration records once.
public class DataManager {
    private final SQLiteDatabase dbase;
    private static final String DB_NAME = "plantcare_db";
    private static final int DB_VERSION = 2;
    private static final String TABLE = "plants";

    public DataManager(Context context) {
        MyDbHelper helper = new MyDbHelper(context);
        dbase = helper.getWritableDatabase();
    }

    private class MyDbHelper extends SQLiteOpenHelper {
        public MyDbHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }
        @Override public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, type TEXT NOT NULL, " +
                    "days INTEGER NOT NULL, last_watered TEXT NOT NULL, notes TEXT NOT NULL)");
            seedSamples(db);
        }
        @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Preserve existing records. Seed only an empty version-1 installation.
            if (oldVersion < 2) {
                try (Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM plants", null)) {
                    if (cursor.moveToFirst() && cursor.getInt(0) == 0) seedSamples(db);
                }
            }
        }
    }

    private static void seedSamples(SQLiteDatabase db) {
        String today = DateHelper.today();
        Object[][] samples = {
                {"Rose", "Flower", 3, DateHelper.shiftDate(today, -3),
                        "Sample plant: check the soil before watering."},
                {"Basil", "Herb", 2, DateHelper.shiftDate(today, -4),
                        "Sample plant: keep near a bright window."},
                {"Aloe Vera", "Succulent", 14, DateHelper.shiftDate(today, -2),
                        "Sample plant: allow the soil to dry between waterings."},
                {"Peace Lily", "Indoor", 7, DateHelper.shiftDate(today, -7),
                        "Sample plant: use indirect light."},
                {"Mint", "Herb", 3, today,
                        "Sample plant: grow in a separate pot."}
        };
        for (Object[] sample : samples) {
            db.execSQL("INSERT INTO plants (name,type,days,last_watered,notes) VALUES (?,?,?,?,?)",
                    sample);
        }
    }

    // execSQL with placeholders prevents a plant name containing an apostrophe breaking SQL.
    public void insert(String name, String type, int days, String lastWatered, String notes) {
        dbase.execSQL("INSERT INTO plants (name,type,days,last_watered,notes) VALUES (?,?,?,?,?)",
                new Object[]{name, type, days, lastWatered, notes});
    }
    public void delete(int id) {
        dbase.execSQL("DELETE FROM plants WHERE _id=?", new Object[]{id});
    }
    public void waterNow(int id) {
        dbase.execSQL("UPDATE plants SET last_watered=? WHERE _id=?",
                new Object[]{DateHelper.today(), id});
    }
    public ArrayList<Plant> selectAll() {
        Cursor c = dbase.rawQuery("SELECT * FROM plants ORDER BY name", null);
        return readPlants(c);
    }
    public ArrayList<Plant> search(String text) {
        Cursor c = dbase.rawQuery("SELECT * FROM plants WHERE name LIKE ? OR type LIKE ? ORDER BY name",
                new String[]{"%" + text + "%", "%" + text + "%"});
        return readPlants(c);
    }
    public Plant findById(int id) {
        Cursor c = dbase.rawQuery("SELECT * FROM plants WHERE _id=?", new String[]{String.valueOf(id)});
        ArrayList<Plant> result = readPlants(c);
        if (result.size() == 0) return null;
        return result.get(0);
    }
    private ArrayList<Plant> readPlants(Cursor c) {
        ArrayList<Plant> plants = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                plants.add(new Plant(c.getInt(0), c.getString(1), c.getString(2),
                        c.getInt(3), c.getString(4), c.getString(5)));
            }
        } finally { c.close(); }
        return plants;
    }
    public void close() { dbase.close(); }
}
