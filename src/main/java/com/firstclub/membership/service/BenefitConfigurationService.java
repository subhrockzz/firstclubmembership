package com.firstclub.membership.service;

import com.firstclub.membership.domain.TierBenefitConfig;
import com.firstclub.membership.domain.TierLevel;
import com.firstclub.membership.repository.TierBenefitRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BenefitConfigurationService {

    private final TierBenefitRepository benefitRepository;

    public BenefitConfigurationService(TierBenefitRepository benefitRepository) {
        this.benefitRepository = benefitRepository;
    }

    @Cacheable("benefits")
    @Transactional(readOnly = true)
    public List<TierBenefitConfig> getAllTierBenefits() {
        System.out.println("Executing SQL Query to fetch dynamic benefits from Database...");
        return benefitRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TierBenefitConfig getBenefitConfigByTier(TierLevel tier) {
        return benefitRepository.findById(tier)
                .orElseThrow(() -> new EntityNotFoundException("Configuration missing for tier: " + tier));
    }

    /**
     * @CacheEvict ensures that if an Admin updates a benefit (e.g., changes discount from 5% to 6%),
     * the cached list is wiped, forcing the next GET request to fetch fresh data from the DB.
     */
    @CacheEvict(value = "benefits", allEntries = true)
    public TierBenefitConfig saveBenefitConfig(TierBenefitConfig config) {
        return benefitRepository.save(config);
    }

    @CacheEvict(value = "benefits", allEntries = true)
    public void deleteBenefitConfig(TierLevel tier) {
        if (!benefitRepository.existsById(tier)) {
            throw new EntityNotFoundException("Configuration missing for tier: " + tier);
        }
        benefitRepository.deleteById(tier);
    }
}