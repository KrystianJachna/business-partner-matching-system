package pl.krystian.businesspartnermatching.company.dto;

import pl.krystian.businesspartnermatching.catalog.industry.dto.IndustryResponse;
import pl.krystian.businesspartnermatching.catalog.specialization.dto.SpecializationResponse;
import pl.krystian.businesspartnermatching.company.Company;

import java.time.LocalDate;
import java.util.Set;

public record CompanyResponse(
        Long id,
        String name,
        String description,
        IndustryResponse industry,
        Set<SpecializationResponse> specializations,
        String country,
        String city,
        LocalDate establishedAt,
        String capabilities,
        boolean active
) {

    public static CompanyResponse from(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getDescription(),
                IndustryResponse.from(company.getIndustry()),
                company.getSpecializations()
                        .stream()
                        .map(SpecializationResponse::from)
                        .collect(java.util.stream.Collectors.toSet()),
                company.getCountry(),
                company.getCity(),
                company.getEstablishedAt(),
                company.getCapabilities(),
                company.isActive()
        );
    }
}
