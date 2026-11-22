package com.example.plantcare;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateHelper {
    private static SimpleDateFormat format() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        f.setLenient(false);
        return f;
    }
    public static String today() { return format().format(new Date()); }
    // Use UTC for date-only arithmetic so daylight-saving changes cannot lose a day.
    private static SimpleDateFormat dayFormat() {
        SimpleDateFormat f = format();
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        return f;
    }
    public static String shiftDate(String date, int days) {
        try {
            SimpleDateFormat f = dayFormat();
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            c.setTime(f.parse(date));
            c.add(Calendar.DAY_OF_MONTH, days);
            return f.format(c.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid calendar date: " + date, e);
        }
    }
    public static long daysUntilWatering(Plant plant) {
        return daysUntilWatering(plant, today());
    }
    static long daysUntilWatering(Plant plant, String currentDate) {
        try {
            SimpleDateFormat f = dayFormat();
            Date due = f.parse(shiftDate(plant.lastWatered, plant.days));
            Date now = f.parse(currentDate);
            return TimeUnit.MILLISECONDS.toDays(due.getTime() - now.getTime());
        } catch (ParseException | IllegalArgumentException e) { return 0; }
    }
    public static String status(Plant plant) {
        long remaining = daysUntilWatering(plant);
        if (remaining < 0) return "Overdue by " + (-remaining) + " day(s)";
        if (remaining == 0) return "Water today";
        return "Water in " + remaining + " day(s)";
    }
}
