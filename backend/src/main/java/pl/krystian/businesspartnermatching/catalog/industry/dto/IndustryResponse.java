package pl.krystian.businesspartnermatching.catalog.industry.dto;

import pl.krystian.businesspartnermatching.catalog.industry.Industry;

public record IndustryResponse(Long id, String code, String name) {

    public static IndustryResponse from(Industry industry) {
        return new IndustryResponse(
                industry.getId(),
                industry.getCode(),
                industry.getName()
        );
    }
}
