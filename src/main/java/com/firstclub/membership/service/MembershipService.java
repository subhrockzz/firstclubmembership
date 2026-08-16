package com.firstclub.membership.service;

import com.firstclub.membership.domain.PlanType;
import com.firstclub.membership.domain.Subscription;
import com.firstclub.membership.domain.TierLevel;
import com.firstclub.membership.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class MembershipService {
    private final SubscriptionRepository repository;
    private final UserActivityClient activityClient;

    public MembershipService(SubscriptionRepository repository, UserActivityClient activityClient) {
        this.repository = repository;
        this.activityClient = activityClient;
    }

    public Subscription createSubscription(String userId, PlanType plan, TierLevel tier) {
        repository.findByUserId(userId).ifPresent(sub -> {
            if (sub.isActive()) throw new IllegalStateException("Active subscription already exists.");
        });

        if (tier != TierLevel.SILVER && !activityClient.verifyUserActivity(userId)) {
            throw new IllegalArgumentException("Ineligible for tier: " + tier);
        }

        Instant now = Instant.now();
        return repository.save(new Subscription(userId, plan, tier, now, now.plus(plan.days, ChronoUnit.DAYS)));
    }

    @Transactional(readOnly = true)
    public Subscription getSubscription(String userId) {
        return repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Subscription not found for user: " + userId));
    }

    public Subscription updateTier(String userId, TierLevel newTier) {
        Subscription sub = getSubscription(userId);
        if (!sub.isActive()) {
            throw new IllegalStateException("Cannot upgrade/downgrade an inactive subscription.");
        }

        if (newTier != TierLevel.SILVER && !activityClient.verifyUserActivity(userId)) {
            throw new IllegalArgumentException("Ineligible for tier: " + newTier);
        }

        sub.updateTier(newTier);
        return repository.save(sub);
    }

    public Subscription cancelSubscription(String userId) {
        Subscription sub = getSubscription(userId);
        if (!sub.isActive()) {
            throw new IllegalStateException("Subscription is already inactive.");
        }
        sub.cancel();
        return repository.save(sub);
    }
}