package pl.krystian.businesspartnermatching.need.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krystian.businesspartnermatching.catalog.specialization.model.entity.Specialization;
import pl.krystian.businesspartnermatching.catalog.specialization.service.SpecializationResolver;
import pl.krystian.businesspartnermatching.common.money.MoneyRange;
import pl.krystian.businesspartnermatching.common.money.dto.MoneyRangeRequest;
import pl.krystian.businesspartnermatching.common.time.DateRange;
import pl.krystian.businesspartnermatching.common.time.dto.DateRangeRequest;
import pl.krystian.businesspartnermatching.company.model.entity.Company;
import pl.krystian.businesspartnermatching.company.repository.CompanyRepository;
import pl.krystian.businesspartnermatching.company.exception.CompanyNotFoundException;
import pl.krystian.businesspartnermatching.need.model.dto.BusinessNeedResponse;
import pl.krystian.businesspartnermatching.need.model.dto.CreateBusinessNeedRequest;
import pl.krystian.businesspartnermatching.need.exception.BusinessNeedNotFoundException;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.need.repository.BusinessNeedRepository;

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
