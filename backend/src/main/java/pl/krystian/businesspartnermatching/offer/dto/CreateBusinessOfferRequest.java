package pl.krystian.businesspartnermatching.offer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import pl.krystian.businesspartnermatching.common.cooperation.CooperationType;
import pl.krystian.businesspartnermatching.common.money.dto.MoneyRangeRequest;
import pl.krystian.businesspartnermatching.common.time.dto.DateRangeRequest;

import java.util.Set;

public record CreateBusinessOfferRequest(

        @NotBlank
        @Size(max = 150)
        String title,

        @Size(max = 2000)
        String description,

        @NotNull
        CooperationType cooperationType,

        @NotEmpty
        Set<Long> offeredSpecializationIds,

        @Valid
        MoneyRangeRequest priceRange,

        @Valid
        DateRangeRequest availabilityPeriod,

        @PositiveOrZero
        Integer serviceRadiusKm,

        @NotNull
        @Positive
        Integer maxPartners
) {
}
