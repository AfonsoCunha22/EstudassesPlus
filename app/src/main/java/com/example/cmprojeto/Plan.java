package com.example.cmprojeto;

public class Plan {
    private String id, curricularUnit, description;
    private long time;
    private Color color;
    private boolean active;

    public Plan() {
    }

    public Plan(String curricularUnit, String description, long time, Color color, boolean active) {
        this.curricularUnit = curricularUnit;
        this.description = description;
        this.time = time;
        this.color = color;
        this.active = active;
    }

    public String getCurricularUnit() {
        return curricularUnit;
    }

    public String getDescription() {
        return description;
    }

    public long getTime() {
        return time;
    }

    public Color getColor() {
        return color;
    }
}
