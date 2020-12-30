package com.example.cmprojeto.model;

import java.util.ArrayList;
import java.util.List;

public class UserSessions {
    private final List<Session> sessions;
    private boolean isPopulated;

    public UserSessions() {
        this.sessions = new ArrayList<>();
        this.isPopulated = false;
    }

    public void populate(List<Session> sessions) {
        this.sessions.addAll(sessions);
        isPopulated = true;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public boolean isPopulated() {
        return isPopulated;
    }
}
