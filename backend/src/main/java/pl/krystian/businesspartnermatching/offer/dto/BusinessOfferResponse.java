package pl.krystian.businesspartnermatching.offer.dto;

import pl.krystian.businesspartnermatching.catalog.specialization.dto.SpecializationResponse;
import pl.krystian.businesspartnermatching.common.cooperation.CooperationType;
import pl.krystian.businesspartnermatching.common.money.dto.MoneyRangeResponse;
import pl.krystian.businesspartnermatching.common.time.dto.DateRangeResponse;
import pl.krystian.businesspartnermatching.offer.BusinessOffer;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record BusinessOfferResponse(
        Long id,
        Long companyId,
        String title,
        String description,
        CooperationType cooperationType,
        Set<SpecializationResponse> offeredSpecializations,
        MoneyRangeResponse priceRange,
        DateRangeResponse availabilityPeriod,
        Integer serviceRadiusKm,
        Integer maxPartners,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static BusinessOfferResponse from(
            BusinessOffer businessOffer
    ) {
        return new BusinessOfferResponse(
                businessOffer.getId(),
                businessOffer.getCompany().getId(),
                businessOffer.getTitle(),
                businessOffer.getDescription(),
                businessOffer.getCooperationType(),
                businessOffer.getOfferedSpecializations()
                        .stream()
                        .map(SpecializationResponse::from)
                        .collect(Collectors.toSet()),
                MoneyRangeResponse.from(
                        businessOffer.getPriceRange()
                ),
                DateRangeResponse.from(
                        businessOffer.getAvailabilityPeriod()
                ),
                businessOffer.getServiceRadiusKm(),
                businessOffer.getMaxPartners(),
                businessOffer.isActive(),
                businessOffer.getCreatedAt(),
                businessOffer.getUpdatedAt()
        );
    }
}
