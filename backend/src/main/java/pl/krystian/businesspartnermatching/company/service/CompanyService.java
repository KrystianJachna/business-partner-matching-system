package pl.krystian.businesspartnermatching.company.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krystian.businesspartnermatching.catalog.industry.model.entity.Industry;
import pl.krystian.businesspartnermatching.catalog.industry.repository.IndustryRepository;
import pl.krystian.businesspartnermatching.catalog.industry.exception.IndustryNotFoundException;
import pl.krystian.businesspartnermatching.company.model.dto.CompanyResponse;
import pl.krystian.businesspartnermatching.company.model.dto.CreateCompanyRequest;
import pl.krystian.businesspartnermatching.company.model.entity.Company;
import pl.krystian.businesspartnermatching.company.exception.CompanyNotFoundException;
import pl.krystian.businesspartnermatching.company.repository.CompanyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final IndustryRepository industryRepository;

    @Transactional
    public CompanyResponse createCompany(CreateCompanyRequest request) {
        Industry industry = industryRepository
                .findByIdAndActiveTrue(request.industryId())
                .orElseThrow(() -> new IndustryNotFoundException(request.industryId()));

        Company company = new Company(
                request.name(),
                request.description(),
                industry,
                request.country(),
                request.city(),
                request.latitude(),
                request.longitude(),
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

    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAllByOrderByNameAsc()
                .stream()
                .map(CompanyResponse::from)
                .toList();
    }

}
