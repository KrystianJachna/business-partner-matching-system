package pl.krystian.businesspartnermatching.matching.compatibility;

public enum CompatibilityFailureReason {

    SAME_COMPANY,
    INCOMPATIBLE_COOPERATION_TYPE,
    NO_COMMON_SPECIALIZATION,
    NO_BUDGET_OVERLAP,
    NO_DATE_OVERLAP,
    INSUFFICIENT_PARTNER_EXPERIENCE,
    INACTIVE_NEED_OR_OFFER,
    DISTANCE_LIMIT_EXCEEDED
}
