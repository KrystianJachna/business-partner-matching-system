package pl.krystian.businesspartnermatching.matching.model.dto;

import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;

public record CompatibilityReasonResponse(
        String code,
        String description
) {

    public static CompatibilityReasonResponse from(
            CompatibilityFailureReason reason
    ) {
        return switch (reason) {
            case SAME_COMPANY -> new CompatibilityReasonResponse(
                    "DIFFERENT_COMPANIES",
                    "The need and offer belong to different companies."
            );
            case INCOMPATIBLE_COOPERATION_TYPE -> new CompatibilityReasonResponse(
                    "COMPATIBLE_COOPERATION_TYPE",
                    "The need and offer use the same cooperation type."
            );
            case NO_COMMON_SPECIALIZATION -> new CompatibilityReasonResponse(
                    "COMMON_SPECIALIZATION",
                    "The need and offer share at least one specialization."
            );
            case NO_BUDGET_OVERLAP -> new CompatibilityReasonResponse(
                    "BUDGET_OVERLAP",
                    "The need budget overlaps with the offer price range."
            );
            case NO_DATE_OVERLAP -> new CompatibilityReasonResponse(
                    "DATE_RANGE_OVERLAP",
                    "The required and available periods overlap."
            );
            case INSUFFICIENT_PARTNER_EXPERIENCE -> new CompatibilityReasonResponse(
                    "SUFFICIENT_EXPERIENCE",
                    "The offer meets the required partner experience."
            );
            case DISTANCE_LIMIT_EXCEEDED -> new CompatibilityReasonResponse(
                    "DISTANCE_WITHIN_LIMIT",
                    "The companies are within the allowed distance."
            );
            case INACTIVE_NEED_OR_OFFER -> new CompatibilityReasonResponse(
                    "ACTIVE_NEED_AND_OFFER",
                    "Both the need and offer are active."
            );
        };
    }
}
