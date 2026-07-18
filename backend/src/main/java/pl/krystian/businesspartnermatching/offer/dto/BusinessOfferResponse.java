package pl.krystian.businesspartnermatching.offer.dto;

import pl.krystian.businesspartnermatching.catalog.specialization.dto.SpecializationResponse;
import pl.krystian.businesspartnermatching.common.cooperation.CooperationType;
import pl.krystian.businesspartnermatching.common.money.CurrencyCode;
import pl.krystian.businesspartnermatching.offer.BusinessOffer;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        BigDecimal priceMin,
        BigDecimal priceMax,
        CurrencyCode priceCurrency,
        LocalDate availableFrom,
        LocalDate availableUntil,
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
                businessOffer.getPriceRange() == null
                        ? null
                        : businessOffer.getPriceRange().getMin(),
                businessOffer.getPriceRange() == null
                        ? null
                        : businessOffer.getPriceRange().getMax(),
                businessOffer.getPriceRange() == null
                        ? null
                        : businessOffer.getPriceRange().getCurrency(),
                businessOffer.getAvailabilityPeriod() == null
                        ? null
                        : businessOffer
                        .getAvailabilityPeriod()
                        .getFrom(),
                businessOffer.getAvailabilityPeriod() == null
                        ? null
                        : businessOffer
                        .getAvailabilityPeriod()
                        .getUntil(),
                businessOffer.getServiceRadiusKm(),
                businessOffer.getMaxPartners(),
                businessOffer.isActive(),
                businessOffer.getCreatedAt(),
                businessOffer.getUpdatedAt()
        );
    }
}
