package com.example.plantcare;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.File;
import java.util.ArrayList;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DataManagerTest {
    private Context context;
    private DataManager data;

    @Before public void setUp() {
        Context target = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context = new ContextWrapper(target) {
            @Override public File getDatabasePath(String name) {
                return super.getDatabasePath("test_" + name);
            }
            @Override public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                    SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler handler) {
                return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory, handler);
            }
        };
        SQLiteDatabase.deleteDatabase(context.getDatabasePath("plantcare_db"));
        data = new DataManager(context);
    }

    @After public void tearDown() {
        data.close();
        SQLiteDatabase.deleteDatabase(context.getDatabasePath("plantcare_db"));
    }

    @Test public void seedsOnceAndDoesNotRestoreDeletedSamples() {
        assertEquals(5, data.selectAll().size());
        data.close();
        data = new DataManager(context);
        assertEquals(5, data.selectAll().size());
        for (Plant p : data.selectAll()) data.delete(p.id);
        data.close();
        data = new DataManager(context);
        assertTrue(data.selectAll().isEmpty());
    }

    @Test public void insertSearchWaterDeleteAndReopen() {
        data.insert("Sam's Fern", "Fern", 4, "2026-01-01", "Shade");
        ArrayList<Plant> matches = data.search("Sam's");
        assertEquals(1, matches.size());
        int id = matches.get(0).id;
        data.close();
        data = new DataManager(context);
        assertEquals("Shade", data.findById(id).notes);
        assertEquals(1, data.search("Fern").size());
        data.waterNow(id);
        assertEquals(DateHelper.today(), data.findById(id).lastWatered);
        data.delete(id);
        assertNull(data.findById(id));
        assertTrue(data.search("Sam's").isEmpty());
        assertEquals(5, data.selectAll().size());
    }

    @Test public void sampleDatesCoverAllScheduleStates() {
        int overdue = 0, today = 0, future = 0;
        for (Plant p : data.selectAll()) {
            long days = DateHelper.daysUntilWatering(p);
            if (days < 0) overdue++;
            else if (days == 0) today++;
            else future++;
        }
        assertEquals(1, overdue);
        assertEquals(2, today);
        assertEquals(2, future);
    }
}
