package com.example.cmprojeto.model;

import java.util.Objects;

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

    public String getId() {
        return id;
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
        return "Dados do Plano:\n\n"+
                "CurricularUnit: "+ getSubject() +
                "\nTime: "+ getTime()+
                "\nColor: "+ getColor()+
                "\nDescription: "+ getDescription() +
                "\nID do plano na BD: "+ getId() +
                "\nIs Active? "+ isActive();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plan plan = (Plan) o;
        return time == plan.time &&
                active == plan.active &&
                Objects.equals(id, plan.id) &&
                Objects.equals(description, plan.description) &&
                Objects.equals(subject, plan.subject) &&
                color == plan.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, subject, time, color, active);
    }
}
