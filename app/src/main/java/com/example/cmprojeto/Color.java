package com.example.cmprojeto;

public enum Color {
    RED, BLUE, GREEN, YELLOW, BROWN, WHITE;

    @Override
    public String toString() {
        switch (this){
            case RED: return "#FF0000";
            case BLUE: return "#0000FF";
            case BROWN: return "#A52A2A";
            case GREEN: return "#00FF00";
            case YELLOW: return "#FFFF00";
            default: return "#FFFFFF";
        }
    }

    public static Color toColor(String color) {
        switch (color) {
            case "#FF0000":
                return RED;
            case "#0000FF":
                return BLUE;
            case "#A52A2A":
                return BROWN;
            case "#00FF00":
                return GREEN;
            case "#FFFF00":
                return YELLOW;
            default:
                return RED;
        }
    }
}
