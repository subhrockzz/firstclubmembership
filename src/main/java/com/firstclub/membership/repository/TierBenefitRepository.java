package com.firstclub.membership.repository;

import com.firstclub.membership.domain.TierBenefitConfig;
import com.firstclub.membership.domain.TierLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TierBenefitRepository extends JpaRepository<TierBenefitConfig, TierLevel> {
}