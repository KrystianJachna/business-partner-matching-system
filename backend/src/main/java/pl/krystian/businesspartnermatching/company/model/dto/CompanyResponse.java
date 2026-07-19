package pl.krystian.businesspartnermatching.company.model.dto;

import pl.krystian.businesspartnermatching.catalog.industry.model.dto.IndustryResponse;
import pl.krystian.businesspartnermatching.catalog.specialization.model.dto.SpecializationResponse;
import pl.krystian.businesspartnermatching.company.model.entity.Company;

import java.math.BigDecimal;
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
        BigDecimal latitude,
        BigDecimal longitude,
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
                company.getLatitude(),
                company.getLongitude(),
                company.getEstablishedAt(),
                company.getCapabilities(),
                company.isActive()
        );
    }
}
