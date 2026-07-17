package pl.krystian.businesspartnermatching.need.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import pl.krystian.businesspartnermatching.need.CooperationType;

import java.util.Set;

public record CreateBusinessNeedRequest(

        @NotBlank
        @Size(max = 150)
        String title,

        @Size(max = 2000)
        String description,

        @NotNull
        CooperationType cooperationType,

        @NotEmpty
        Set<Long> requiredSpecializationIds,

        @Valid
        MoneyRangeRequest budget,

        @Valid
        DateRangeRequest requiredPeriod,

        @PositiveOrZero
        Integer maxDistanceKm,

        @PositiveOrZero
        Integer minPartnerExperienceYears,

        @NotNull
        @Positive
        Integer maxPartners
) {
}