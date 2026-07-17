package pl.krystian.businesspartnermatching.catalog.specialization.dto;

import pl.krystian.businesspartnermatching.catalog.specialization.Specialization;

public record SpecializationResponse(Long id, String code, String name) {
    public static SpecializationResponse from(Specialization specialization) {
        return new SpecializationResponse(
                specialization.getId(),
                specialization.getCode(),
                specialization.getName()
        );
    }
}
