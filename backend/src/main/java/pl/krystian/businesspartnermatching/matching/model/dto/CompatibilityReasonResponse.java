package pl.krystian.businesspartnermatching.matching.model.dto;

import pl.krystian.businesspartnermatching.matching.scoring.MatchingCriterion;
import pl.krystian.businesspartnermatching.matching.scoring.model.SingleCriterionScore;

import java.math.BigDecimal;
import java.util.Optional;

public record CompatibilityReasonResponse(
        String code,
        String description
) {

    private static final BigDecimal HIGHLIGHT_THRESHOLD =
            new BigDecimal("0.75");

    public static Optional<CompatibilityReasonResponse> from(
            SingleCriterionScore criterionScore
    ) {
        if (criterionScore.value().compareTo(HIGHLIGHT_THRESHOLD) < 0) {
            return Optional.empty();
        }

        return Optional.of(switch (criterionScore.criterion()) {
            case SPECIALIZATION -> specializationReason(criterionScore);
            case BUDGET -> new CompatibilityReasonResponse(
                    "STRONG_BUDGET_MATCH",
                    "The offer price is close to the expected budget."
            );
            case DATE -> new CompatibilityReasonResponse(
                    "GOOD_DATE_MATCH",
                    "The offer availability closely matches the required period."
            );
            case DISTANCE -> new CompatibilityReasonResponse(
                    "CLOSE_DISTANCE",
                    "The companies are located within a short distance of each other."
            );
            case EXPERIENCE -> new CompatibilityReasonResponse(
                    "EXPERIENCED_PARTNER",
                    "The offer provides strong relevant partner experience."
            );
        });
    }

    private static CompatibilityReasonResponse specializationReason(
            SingleCriterionScore criterionScore
    ) {
        if (criterionScore.value().compareTo(new BigDecimal("0.90")) >= 0) {
            return new CompatibilityReasonResponse(
                    "EXCELLENT_SPECIALIZATION_MATCH",
                    "The offer covers almost all required specializations."
            );
        }

        return new CompatibilityReasonResponse(
                "STRONG_SPECIALIZATION_MATCH",
                "The need and offer share several relevant specializations."
        );
    }
}
