package com.firstclub.membership.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity 
@Table(name = "tier_benefits_config")
public class TierBenefitConfig {
    
    @Id 
    @Enumerated(EnumType.STRING) 
    private TierLevel tierLevel;
    
    private boolean freeDelivery; 
    private BigDecimal extraDiscountPercentage;
    private boolean earlyAccessToSales; 
    private boolean prioritySupport;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tier_additional_perks", joinColumns = @JoinColumn(name = "tier_level"))
    @Column(name = "perk_description") 
    private List<String> additionalPerks;

    protected TierBenefitConfig() {}
    
    public TierBenefitConfig(TierLevel tierLevel, boolean freeDelivery, BigDecimal extraDiscountPercentage, boolean earlyAccessToSales, boolean prioritySupport, List<String> additionalPerks) {
        this.tierLevel = tierLevel; 
        this.freeDelivery = freeDelivery; 
        this.extraDiscountPercentage = extraDiscountPercentage;
        this.earlyAccessToSales = earlyAccessToSales; 
        this.prioritySupport = prioritySupport; 
        this.additionalPerks = additionalPerks;
    }
    
    // Getters
    public TierLevel getTierLevel() { return tierLevel; }
    public boolean isFreeDelivery() { return freeDelivery; }
    public BigDecimal getExtraDiscountPercentage() { return extraDiscountPercentage; }
    public boolean isEarlyAccessToSales() { return earlyAccessToSales; }
    public boolean isPrioritySupport() { return prioritySupport; }
    public List<String> getAdditionalPerks() { return additionalPerks; }
}