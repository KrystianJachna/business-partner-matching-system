package pl.krystian.businesspartnermatching.matching.scoring;

import pl.krystian.businesspartnermatching.matching.criterion.MatchingCriterion;

import java.math.BigDecimal;
import java.util.Objects;

public record SingleCriterionScore(
        MatchingCriterion criterion,
        BigDecimal value
) {

    private static final BigDecimal MIN_VALUE = BigDecimal.ZERO;
    private static final BigDecimal MAX_VALUE = BigDecimal.ONE;

    public SingleCriterionScore {
        Objects.requireNonNull(
                criterion,
                "Matching criterion cannot be null"
        );

        Objects.requireNonNull(
                value,
                "Criterion score value cannot be null"
        );

        if (value.compareTo(MIN_VALUE) < 0
                || value.compareTo(MAX_VALUE) > 0) {
            throw new IllegalArgumentException(
                    "Criterion score must be between 0 and 1"
            );
        }
    }
}
