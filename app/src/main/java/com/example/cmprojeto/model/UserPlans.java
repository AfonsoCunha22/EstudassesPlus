package com.example.cmprojeto.model;

import java.util.ArrayList;
import java.util.List;

public class UserPlans {
    private final List<Plan> plans;
    private boolean isPopulated;

    public UserPlans() {
        this.plans = new ArrayList<>();
        this.isPopulated = false;
    }

    public void populate(List<Plan> plans) {
        this.plans.addAll(plans);
        isPopulated = true;
    }

    public List<Plan> getPlans() {
        return plans;
    }

    public boolean isPopulated() {
        return isPopulated;
    }
}
