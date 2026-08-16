package com.firstclub.membership.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    private String id = UUID.randomUUID().toString();
    @Column(nullable = false, unique = true)
    private String userId;
    @Enumerated(EnumType.STRING)
    private PlanType planType;
    @Enumerated(EnumType.STRING)
    private TierLevel tierLevel;
    @Enumerated(EnumType.STRING)
    private SubStatus status;
    private Instant startDate;
    private Instant endDate;
    @Version
    private Long version;

    protected Subscription() {
    }

    public Subscription(String userId, PlanType planType, TierLevel tierLevel, Instant startDate, Instant endDate) {
        this.userId = userId;
        this.planType = planType;
        this.tierLevel = tierLevel;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = SubStatus.ACTIVE;
    }

    public boolean isActive() {
        return this.status == SubStatus.ACTIVE && Instant.now().isBefore(endDate);
    }

    public void cancel() {
        this.status = SubStatus.CANCELLED;
    }

    public void updateTier(TierLevel newTier) {
        this.tierLevel = newTier;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public TierLevel getTierLevel() {
        return tierLevel;
    }

    public SubStatus getStatus() {
        return status;
    }
}