package pl.krystian.businesspartnermatching.catalog.specialization.model.dto;

import java.util.List;
import java.util.Objects;

public record SpecializationGroupResponse(
        Long industryId,
        String industryName,
        List<SpecializationResponse> specializations
) {

    public SpecializationGroupResponse {
        Objects.requireNonNull(
                industryId,
                "Industry id cannot be null"
        );
        Objects.requireNonNull(
                industryName,
                "Industry name cannot be null"
        );
        Objects.requireNonNull(
                specializations,
                "Specializations cannot be null"
        );

        specializations = List.copyOf(specializations);
    }
}
