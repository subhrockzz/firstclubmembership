package com.firstclub.membership.service;

import com.firstclub.membership.domain.PlanType;
import com.firstclub.membership.domain.Subscription;
import com.firstclub.membership.domain.TierLevel;
import com.firstclub.membership.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    @Mock
    private SubscriptionRepository repository;
    @Mock
    private UserActivityClient activityClient;
    @InjectMocks
    private MembershipService membershipService;

    @Test
    void testCreateSubscription_Success() {
        when(repository.findByUserId("vip-user")).thenReturn(Optional.empty());
        when(activityClient.verifyUserActivity("vip-user")).thenReturn(true);
        when(repository.save(any(Subscription.class))).thenAnswer(i -> i.getArguments()[0]);

        Subscription sub = membershipService.createSubscription("vip-user", PlanType.YEARLY, TierLevel.PLATINUM);

        assertNotNull(sub);
        assertEquals(TierLevel.PLATINUM, sub.getTierLevel());
        assertEquals(PlanType.YEARLY, sub.getPlanType());
        verify(repository, times(1)).save(any(Subscription.class));
    }

    @Test
    void testCreateSubscription_IneligibleUser_ThrowsException() {
        when(repository.findByUserId("standard-user")).thenReturn(Optional.empty());
        when(activityClient.verifyUserActivity("standard-user")).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                membershipService.createSubscription("standard-user", PlanType.MONTHLY, TierLevel.GOLD)
        );

        assertEquals("Ineligible for tier: GOLD", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void testCreateSubscription_UserAlreadyActive_ThrowsException() {
        Subscription existingSub = new Subscription("vip-user", PlanType.MONTHLY, TierLevel.SILVER, Instant.now(), Instant.now().plusSeconds(1000));
        when(repository.findByUserId("vip-user")).thenReturn(Optional.of(existingSub));

        assertThrows(IllegalStateException.class, () ->
                membershipService.createSubscription("vip-user", PlanType.YEARLY, TierLevel.PLATINUM)
        );
    }
}