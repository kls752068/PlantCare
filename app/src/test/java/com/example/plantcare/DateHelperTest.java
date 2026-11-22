package com.example.plantcare;

import org.junit.Test;
import java.util.TimeZone;
import static org.junit.Assert.assertEquals;

public class DateHelperTest {
    private Plant plant(String last, int days) {
        return new Plant(1, "Rose", "Flower", days, last, "");
    }
    @Test public void calculatesFutureDueAndOverdue() {
        assertEquals(2, DateHelper.daysUntilWatering(plant("2026-11-21", 3), "2026-11-22"));
        assertEquals(0, DateHelper.daysUntilWatering(plant("2026-11-19", 3), "2026-11-22"));
        assertEquals(-2, DateHelper.daysUntilWatering(plant("2026-11-18", 2), "2026-11-22"));
    }
    @Test public void handlesMonthYearAndLeapDay() {
        assertEquals("2027-01-02", DateHelper.shiftDate("2026-12-30", 3));
        assertEquals("2024-02-29", DateHelper.shiftDate("2024-02-28", 1));
        assertEquals("2024-03-01", DateHelper.shiftDate("2024-02-28", 2));
    }
    @Test public void daylightSavingDoesNotShortenCalendarDays() {
        TimeZone original = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
            assertEquals(1, DateHelper.daysUntilWatering(plant("2026-03-07", 2), "2026-03-08"));
            assertEquals(-1, DateHelper.daysUntilWatering(plant("2026-03-06", 2), "2026-03-09"));
        } finally { TimeZone.setDefault(original); }
    }
    @Test public void invalidLegacyDateRemainsDue() {
        assertEquals(0, DateHelper.daysUntilWatering(plant("invalid", 2), "2026-11-22"));
    }
}
