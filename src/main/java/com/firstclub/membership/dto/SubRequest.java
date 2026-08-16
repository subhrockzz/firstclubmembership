package com.firstclub.membership.dto;

import com.firstclub.membership.domain.PlanType;
import com.firstclub.membership.domain.TierLevel;
import jakarta.validation.constraints.NotNull;

public record SubRequest(@NotNull PlanType plan, @NotNull TierLevel tier) {
}