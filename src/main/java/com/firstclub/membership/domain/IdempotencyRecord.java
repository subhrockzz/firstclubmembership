package com.firstclub.membership.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "idempotency_keys")
public class IdempotencyRecord {
    @Id
    private String idempotencyKey;
    private String responsePayload;
    private Instant createdAt;

    protected IdempotencyRecord() {
    }

    public IdempotencyRecord(String idempotencyKey, String responsePayload) {
        this.idempotencyKey = idempotencyKey;
        this.responsePayload = responsePayload;
        this.createdAt = Instant.now();
    }

    public String getResponsePayload() {
        return responsePayload;
    }
}