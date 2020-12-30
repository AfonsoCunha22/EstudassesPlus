package com.example.cmprojeto.model;

public class Plan {
    private String id, description;
    private String  subject;
    private long time;
    private Color color;
    private boolean active;

    public Plan() {
    }

    public Plan(String subject, String description, long time, Color color, boolean active) {
        this.subject = subject;
        this.description = description;
        this.time = time;
        this.color = color;
        this.active = active;
    }

    public String getSubject() {
        return subject;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setCurricularUnit(String subject) {
        this.subject = subject;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setActive(boolean active) {this.active = active;}

    public boolean isActive() { return active;}

    public String getPlanInfo(){
        return "Dados de utilizador:\n\n"+
                "CurricularUnit: "+ getSubject() +
                "\nTime: "+ getTime()+
                "\nColor: "+ getColor()+
                "\nDescription: "+ getDescription();
    }
}
