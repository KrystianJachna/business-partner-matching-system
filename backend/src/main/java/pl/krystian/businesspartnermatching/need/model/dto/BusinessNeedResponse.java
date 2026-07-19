package pl.krystian.businesspartnermatching.need.model.dto;

import pl.krystian.businesspartnermatching.catalog.specialization.model.dto.SpecializationResponse;
import pl.krystian.businesspartnermatching.common.cooperation.CooperationType;
import pl.krystian.businesspartnermatching.common.money.dto.MoneyRangeResponse;
import pl.krystian.businesspartnermatching.common.time.dto.DateRangeResponse;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;

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
        MoneyRangeResponse budget,
        DateRangeResponse requiredPeriod,
        Integer maxDistanceKm,
        Integer minPartnerExperienceYears,
        Integer maxPartners,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static BusinessNeedResponse from(
            BusinessNeed businessNeed
    ) {
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
                MoneyRangeResponse.from(
                        businessNeed.getBudget()
                ),
                DateRangeResponse.from(
                        businessNeed.getRequiredPeriod()
                ),
                businessNeed.getMaxDistanceKm(),
                businessNeed.getMinPartnerExperienceYears(),
                businessNeed.getMaxPartners(),
                businessNeed.isActive(),
                businessNeed.getCreatedAt(),
                businessNeed.getUpdatedAt()
        );
    }
}
