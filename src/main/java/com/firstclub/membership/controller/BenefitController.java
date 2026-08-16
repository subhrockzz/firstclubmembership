package com.firstclub.membership.controller;

import com.firstclub.membership.domain.TierBenefitConfig;
import com.firstclub.membership.domain.TierLevel;
import com.firstclub.membership.service.BenefitConfigurationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/benefits")
public class BenefitController {
    private final BenefitConfigurationService benefitService;

    public BenefitController(BenefitConfigurationService benefitService) {
        this.benefitService = benefitService;
    }

    @GetMapping
    public ResponseEntity<List<TierBenefitConfig>> getBenefits() {
        return ResponseEntity.ok(benefitService.getAllTierBenefits());
    }

    @GetMapping("/{tier}")
    public ResponseEntity<TierBenefitConfig> getBenefitByTier(@PathVariable TierLevel tier) {
        return ResponseEntity.ok(benefitService.getBenefitConfigByTier(tier));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TierBenefitConfig> createOrUpdateBenefit(@RequestBody TierBenefitConfig config) {
        return new ResponseEntity<>(benefitService.saveBenefitConfig(config), HttpStatus.CREATED);
    }

    @DeleteMapping("/{tier}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBenefit(@PathVariable TierLevel tier) {
        benefitService.deleteBenefitConfig(tier);
        return ResponseEntity.noContent().build();
    }
}