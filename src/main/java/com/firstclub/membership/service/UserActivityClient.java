package com.firstclub.membership.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class UserActivityClient {

    @Retryable(retryFor = {RuntimeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public boolean verifyUserActivity(String userId) {
        System.out.println("Attempting to verify user activity for: " + userId);
        if (Math.random() > 0.7) {
            throw new RuntimeException("Network Timeout connecting to Activity Service!");
        }
        return userId.contains("vip");
    }
}