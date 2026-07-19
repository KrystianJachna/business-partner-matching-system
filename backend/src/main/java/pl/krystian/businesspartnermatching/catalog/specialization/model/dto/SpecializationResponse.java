package pl.krystian.businesspartnermatching.catalog.specialization.model.dto;

import pl.krystian.businesspartnermatching.catalog.specialization.model.entity.Specialization;

public record SpecializationResponse(Long id, String code, String name) {

    public static SpecializationResponse from(Specialization specialization) {
        return new SpecializationResponse(
                specialization.getId(),
                specialization.getCode(),
                specialization.getName()
        );
    }
}
