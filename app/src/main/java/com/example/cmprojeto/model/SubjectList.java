package com.example.cmprojeto.model;

import java.util.ArrayList;
import java.util.List;

public class SubjectList {
    private final List<String> subjects;
    private boolean isPopulated;

    public SubjectList() {
        this.subjects = new ArrayList<>();
        this.isPopulated = false;
    }

    public void populate(List<String> subjects) {
        this.subjects.addAll(subjects);
        isPopulated = true;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public boolean isPopulated() {
        return isPopulated;
    }
}
