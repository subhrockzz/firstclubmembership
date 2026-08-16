package com.firstclub.membership.controller;

import com.firstclub.membership.domain.IdempotencyRecord;
import com.firstclub.membership.domain.Subscription;
import com.firstclub.membership.dto.SubRequest;
import com.firstclub.membership.dto.TierUpdateRequest;
import com.firstclub.membership.repository.IdempotencyRepository;
import com.firstclub.membership.service.MembershipService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users/{userId}/subscriptions")
public class MembershipController {
    private final MembershipService service;
    private final IdempotencyRepository idempotencyRepository;
    private final ObjectMapper objectMapper;

    public MembershipController(MembershipService service, IdempotencyRepository idempotencyRepository, ObjectMapper objectMapper) {
        this.service = service;
        this.idempotencyRepository = idempotencyRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    @PreAuthorize("#userId == authentication.name or hasRole('ADMIN')")
    public ResponseEntity<?> subscribe(
            @PathVariable String userId,
            @Valid @RequestBody SubRequest req,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {

        if (idempotencyKey != null) {
            Optional<IdempotencyRecord> existing = idempotencyRepository.findById(idempotencyKey);
            if (existing.isPresent()) {
                try {
                    return ResponseEntity.ok(objectMapper.readValue(existing.get().getResponsePayload(), Subscription.class));
                } catch (Exception e) {
                }
            }
        }
        Subscription sub = service.createSubscription(userId, req.plan(), req.tier());
        if (idempotencyKey != null) {
            try {
                idempotencyRepository.save(new IdempotencyRecord(idempotencyKey, objectMapper.writeValueAsString(sub)));
            } catch (Exception e) {
            }
        }
        return new ResponseEntity<>(sub, HttpStatus.CREATED);
    }

    @GetMapping("/current")
    @PreAuthorize("#userId == authentication.name or hasRole('ADMIN')")
    public ResponseEntity<Subscription> getSubscription(@PathVariable String userId) {
        return ResponseEntity.ok(service.getSubscription(userId));
    }

    @PatchMapping("/current/tier")
    @PreAuthorize("#userId == authentication.name or hasRole('ADMIN')")
    public ResponseEntity<Subscription> updateTier(
            @PathVariable String userId,
            @Valid @RequestBody TierUpdateRequest req) {
        return ResponseEntity.ok(service.updateTier(userId, req.tier()));
    }

    @DeleteMapping("/current")
    @PreAuthorize("#userId == authentication.name or hasRole('ADMIN')")
    public ResponseEntity<Subscription> cancelSubscription(@PathVariable String userId) {
        return ResponseEntity.ok(service.cancelSubscription(userId));
    }
}