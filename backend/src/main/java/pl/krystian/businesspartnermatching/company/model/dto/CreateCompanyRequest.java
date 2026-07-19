package pl.krystian.businesspartnermatching.company.model.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

public record CreateCompanyRequest(

        @NotBlank
        @Size(max = 255)
        String name,

        @Size(max = 5000)
        String description,

        @NotNull
        Long industryId,

        @NotEmpty
        Set<Long> specializationIds,

        @NotBlank
        @Size(max = 100)
        String country,

        @NotBlank
        @Size(max = 150)
        String city,

        @PastOrPresent
        LocalDate establishedAt,

        @Size(max = 5000)
        String capabilities
) {
}
