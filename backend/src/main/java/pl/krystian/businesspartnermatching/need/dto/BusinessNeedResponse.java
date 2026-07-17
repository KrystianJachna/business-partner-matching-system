package pl.krystian.businesspartnermatching.need.dto;

import pl.krystian.businesspartnermatching.catalog.specialization.dto.SpecializationResponse;
import pl.krystian.businesspartnermatching.common.money.CurrencyCode;
import pl.krystian.businesspartnermatching.need.BusinessNeed;
import pl.krystian.businesspartnermatching.need.CooperationType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record BusinessNeedResponse(
        Long id,
        Long companyId,
        String title,
        String description,
        CooperationType cooperationType,
        Set<SpecializationResponse> requiredSpecializations,
        BigDecimal budgetMin,
        BigDecimal budgetMax,
        CurrencyCode budgetCurrency,
        LocalDate requiredFrom,
        LocalDate requiredUntil,
        Integer maxDistanceKm,
        Integer minPartnerExperienceYears,
        Integer maxPartners,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static BusinessNeedResponse from(BusinessNeed businessNeed) {
        return new BusinessNeedResponse(
                businessNeed.getId(),
                businessNeed.getCompany().getId(),
                businessNeed.getTitle(),
                businessNeed.getDescription(),
                businessNeed.getCooperationType(),
                businessNeed.getRequiredSpecializations()
                        .stream()
                        .map(SpecializationResponse::from)
                        .collect(Collectors.toSet()),
                businessNeed.getBudget() == null
                        ? null
                        : businessNeed.getBudget().getMin(),
                businessNeed.getBudget() == null
                        ? null
                        : businessNeed.getBudget().getMax(),
                businessNeed.getBudget() == null
                        ? null
                        : businessNeed.getBudget().getCurrency(),
                businessNeed.getRequiredPeriod() == null
                        ? null
                        : businessNeed.getRequiredPeriod().getFrom(),
                businessNeed.getRequiredPeriod() == null
                        ? null
                        : businessNeed.getRequiredPeriod().getUntil(),
                businessNeed.getMaxDistanceKm(),
                businessNeed.getMinPartnerExperienceYears(),
                businessNeed.getMaxPartners(),
                businessNeed.isActive(),
                businessNeed.getCreatedAt(),
                businessNeed.getUpdatedAt()
        );
    }
}