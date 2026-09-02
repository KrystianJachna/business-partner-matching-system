package pl.krystian.businesspartnermatching.company.model.dto;

import pl.krystian.businesspartnermatching.catalog.industry.model.dto.IndustryResponse;
import pl.krystian.businesspartnermatching.company.model.entity.Company;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CompanyResponse(
        Long id,
        String name,
        String description,
        IndustryResponse industry,
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
