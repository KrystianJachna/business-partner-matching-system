package pl.krystian.businesspartnermatching.offer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krystian.businesspartnermatching.catalog.specialization.Specialization;
import pl.krystian.businesspartnermatching.catalog.specialization.SpecializationRepository;
import pl.krystian.businesspartnermatching.catalog.specialization.exception.SpecializationNotFoundException;
import pl.krystian.businesspartnermatching.common.money.MoneyRange;
import pl.krystian.businesspartnermatching.common.time.DateRange;
import pl.krystian.businesspartnermatching.company.Company;
import pl.krystian.businesspartnermatching.company.CompanyRepository;
import pl.krystian.businesspartnermatching.company.exception.CompanyNotFoundException;
import pl.krystian.businesspartnermatching.offer.dto.BusinessOfferResponse;
import pl.krystian.businesspartnermatching.offer.dto.CreateBusinessOfferRequest;
import pl.krystian.businesspartnermatching.offer.exception.BusinessOfferNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusinessOfferService {

    private final BusinessOfferRepository businessOfferRepository;
    private final CompanyRepository companyRepository;
    private final SpecializationRepository specializationRepository;

    @Transactional
    public BusinessOfferResponse createBusinessOffer(
            Long companyId,
            CreateBusinessOfferRequest request
    ) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(
                        () -> new CompanyNotFoundException(companyId)
                );

        Set<Specialization> offeredSpecializations =
                specializationRepository.findAllByIdInAndActiveTrue(
                        request.offeredSpecializationIds()
                );

        validateAllSpecializationsFound(
                request.offeredSpecializationIds(),
                offeredSpecializations
        );

        MoneyRange priceRange = request.priceRange() == null
                ? null
                : new MoneyRange(
                request.priceRange().min(),
                request.priceRange().max(),
                request.priceRange().currency()
        );

        DateRange availabilityPeriod =
                request.availabilityPeriod() == null
                        ? null
                        : new DateRange(
                        request.availabilityPeriod().from(),
                        request.availabilityPeriod().until()
                );

        BusinessOffer businessOffer = new BusinessOffer(
                company,
                request.title(),
                request.description(),
                request.cooperationType(),
                offeredSpecializations,
                priceRange,
                availabilityPeriod,
                request.serviceRadiusKm(),
                request.maxPartners()
        );

        BusinessOffer savedBusinessOffer =
                businessOfferRepository.save(businessOffer);

        return BusinessOfferResponse.from(savedBusinessOffer);
    }

    @Transactional(readOnly = true)
    public BusinessOfferResponse getBusinessOfferById(
            Long businessOfferId
    ) {
        BusinessOffer businessOffer = businessOfferRepository
                .findById(businessOfferId)
                .orElseThrow(
                        () -> new BusinessOfferNotFoundException(
                                businessOfferId
                        )
                );

        return BusinessOfferResponse.from(businessOffer);
    }

    @Transactional(readOnly = true)
    public List<BusinessOfferResponse> getBusinessOffersByCompanyId(
            Long companyId
    ) {
        if (!companyRepository.existsById(companyId)) {
            throw new CompanyNotFoundException(companyId);
        }

        return businessOfferRepository
                .findAllByCompanyIdOrderByCreatedAtDesc(companyId)
                .stream()
                .map(BusinessOfferResponse::from)
                .toList();
    }

    private void validateAllSpecializationsFound(
            Set<Long> requestedIds,
            Set<Specialization> foundSpecializations
    ) {
        Set<Long> foundIds = foundSpecializations.stream()
                .map(Specialization::getId)
                .collect(Collectors.toSet());

        Set<Long> missingIds = new HashSet<>(requestedIds);
        missingIds.removeAll(foundIds);

        if (!missingIds.isEmpty()) {
            throw new SpecializationNotFoundException(missingIds);
        }
    }
}
