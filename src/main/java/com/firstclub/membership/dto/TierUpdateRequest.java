package com.firstclub.membership.dto;

import com.firstclub.membership.domain.TierLevel;
import jakarta.validation.constraints.NotNull;

public record TierUpdateRequest(@NotNull TierLevel tier) {
}