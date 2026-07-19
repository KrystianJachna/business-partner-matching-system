package pl.krystian.businesspartnermatching.company.model.dto;

import jakarta.validation.constraints.*;
import pl.krystian.businesspartnermatching.common.validation.coordinates.ValidCoordinates;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@ValidCoordinates
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

        @NotNull
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        BigDecimal latitude,

        @NotNull
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        BigDecimal longitude,

        @PastOrPresent
        LocalDate establishedAt,

        @Size(max = 5000)
        String capabilities
) {
}
