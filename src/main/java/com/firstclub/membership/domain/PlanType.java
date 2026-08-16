package com.firstclub.membership.domain;

public enum PlanType {
    MONTHLY(30), QUARTERLY(90), YEARLY(365);
    public final int days;

    PlanType(int days) {
        this.days = days;
    }
}