package pl.krystian.businesspartnermatching.company;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krystian.businesspartnermatching.catalog.industry.Industry;
import pl.krystian.businesspartnermatching.catalog.industry.IndustryRepository;
import pl.krystian.businesspartnermatching.catalog.industry.exception.IndustryNotFoundException;
import pl.krystian.businesspartnermatching.catalog.specialization.Specialization;
import pl.krystian.businesspartnermatching.catalog.specialization.SpecializationRepository;
import pl.krystian.businesspartnermatching.catalog.specialization.exception.SpecializationIndustryMismatchException;
import pl.krystian.businesspartnermatching.catalog.specialization.exception.SpecializationNotFoundException;
import pl.krystian.businesspartnermatching.company.dto.CompanyResponse;
import pl.krystian.businesspartnermatching.company.dto.CreateCompanyRequest;
import pl.krystian.businesspartnermatching.company.exception.CompanyNotFoundException;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final IndustryRepository industryRepository;
    private final SpecializationRepository specializationRepository;

    @Transactional
    public CompanyResponse createCompany(CreateCompanyRequest request) {
        Industry industry = industryRepository
                .findByIdAndActiveTrue(request.industryId())
                .orElseThrow(() -> new IndustryNotFoundException(request.industryId()));

        Set<Specialization> specializations = specializationRepository
                .findAllByIdInAndActiveTrue(request.specializationIds());

        validateAllSpecializationsFound(request.specializationIds(), specializations);

        validateSpecializationsBelongToIndustry(
                industry.getId(),
                specializations
        );

        Company company = new Company(
                request.name(),
                request.description(),
                industry,
                specializations,
                request.country(),
                request.city(),
                request.establishedAt(),
                request.capabilities()
        );

        Company savedCompany = companyRepository.save(company);

        return CompanyResponse.from(savedCompany);
    }

    @Transactional(readOnly = true)
    public CompanyResponse getCompanyById(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        return CompanyResponse.from(company);
    }

    private void validateAllSpecializationsFound(
            Set<Long> requestedIds,
            Set<Specialization> foundSpecializations
    ) {
        Set<Long> foundIds = foundSpecializations.stream()
                .map(Specialization::getId)
                .collect(java.util.stream.Collectors.toSet());

        Set<Long> missingIds = new HashSet<>(requestedIds);
        missingIds.removeAll(foundIds);

        if (!missingIds.isEmpty()) {
            throw new SpecializationNotFoundException(missingIds);
        }
    }

    private void validateSpecializationsBelongToIndustry(
            Long industryId,
            Set<Specialization> specializations
    ) {
        specializations.stream()
                .filter(specialization ->
                        !specialization.getIndustry().getId().equals(industryId)
                )
                .findFirst()
                .ifPresent(specialization -> {
                    throw new SpecializationIndustryMismatchException(
                            specialization.getId(),
                            industryId
                    );
                });
    }
}
