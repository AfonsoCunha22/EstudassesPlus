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
}
