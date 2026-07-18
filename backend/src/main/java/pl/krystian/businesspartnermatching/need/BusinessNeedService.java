package pl.krystian.businesspartnermatching.need;

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
import pl.krystian.businesspartnermatching.need.dto.BusinessNeedResponse;
import pl.krystian.businesspartnermatching.need.dto.CreateBusinessNeedRequest;
import pl.krystian.businesspartnermatching.need.exception.BusinessNeedNotFoundException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BusinessNeedService {

    private final BusinessNeedRepository businessNeedRepository;
    private final CompanyRepository companyRepository;
    private final SpecializationResolver specializationResolver;

    @Transactional
    public BusinessNeedResponse createBusinessNeed(
            Long companyId,
            CreateBusinessNeedRequest request
    ) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        Set<Specialization> requiredSpecializations =
                specializationResolver.resolveActive(
                        request.requiredSpecializationIds()
                );

        MoneyRange budget = MoneyRangeRequest.fromNullable(request.budget());
        DateRange requiredPeriod = DateRangeRequest.fromNullable(request.requiredPeriod());

        BusinessNeed businessNeed = new BusinessNeed(
                company,
                request.title(),
                request.description(),
                request.cooperationType(),
                requiredSpecializations,
                budget,
                requiredPeriod,
                request.maxDistanceKm(),
                request.minPartnerExperienceYears(),
                request.maxPartners()
        );

        BusinessNeed savedBusinessNeed =
                businessNeedRepository.save(businessNeed);

        return BusinessNeedResponse.from(savedBusinessNeed);
    }

    @Transactional(readOnly = true)
    public BusinessNeedResponse getBusinessNeedById(Long businessNeedId) {
        BusinessNeed businessNeed = businessNeedRepository
                .findById(businessNeedId)
                .orElseThrow(() ->
                        new BusinessNeedNotFoundException(businessNeedId)
                );

        return BusinessNeedResponse.from(businessNeed);
    }

    @Transactional(readOnly = true)
    public List<BusinessNeedResponse> getBusinessNeedsByCompanyId(
            Long companyId
    ) {
        if (!companyRepository.existsById(companyId)) {
            throw new CompanyNotFoundException(companyId);
        }

        return businessNeedRepository
                .findAllByCompanyIdOrderByCreatedAtDesc(companyId)
                .stream()
                .map(BusinessNeedResponse::from)
                .toList();
    }
}
