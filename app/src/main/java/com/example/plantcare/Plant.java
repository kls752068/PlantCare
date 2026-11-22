package com.example.plantcare;

// Holds one plant database record.
public class Plant {
    public int id;
    public String name;
    public String type;
    public int days;
    public String lastWatered; // Stored as yyyy-MM-dd
    public String notes;

    public Plant(int id, String name, String type, int days, String lastWatered, String notes) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.days = days;
        this.lastWatered = lastWatered;
        this.notes = notes;
    }
}
