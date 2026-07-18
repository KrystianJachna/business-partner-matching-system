package pl.krystian.businesspartnermatching.offer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krystian.businesspartnermatching.catalog.specialization.Specialization;
import pl.krystian.businesspartnermatching.catalog.specialization.SpecializationResolver;
import pl.krystian.businesspartnermatching.common.money.MoneyRange;
import pl.krystian.businesspartnermatching.common.money.dto.MoneyRangeRequest;
import pl.krystian.businesspartnermatching.common.time.DateRange;
import pl.krystian.businesspartnermatching.common.time.dto.DateRangeRequest;
import pl.krystian.businesspartnermatching.company.Company;
import pl.krystian.businesspartnermatching.company.CompanyRepository;
import pl.krystian.businesspartnermatching.company.exception.CompanyNotFoundException;
import pl.krystian.businesspartnermatching.offer.dto.BusinessOfferResponse;
import pl.krystian.businesspartnermatching.offer.dto.CreateBusinessOfferRequest;
import pl.krystian.businesspartnermatching.offer.exception.BusinessOfferNotFoundException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BusinessOfferService {

    private final BusinessOfferRepository businessOfferRepository;
    private final CompanyRepository companyRepository;
    private final SpecializationResolver specializationResolver;

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
                specializationResolver.resolveActive(
                        request.offeredSpecializationIds()
                );

        MoneyRange priceRange = MoneyRangeRequest.fromNullable(request.priceRange());
        DateRange availabilityPeriod = DateRangeRequest.fromNullable(request.availabilityPeriod());

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
}
