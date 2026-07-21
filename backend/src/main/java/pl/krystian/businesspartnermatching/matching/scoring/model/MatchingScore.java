package pl.krystian.businesspartnermatching.matching.scoring.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public record MatchingScore(
        BigDecimal totalScore,
        List<SingleCriterionScore> singleCriterionScores
) {

    public MatchingScore {
        Objects.requireNonNull(
                totalScore,
                "Total matching score cannot be null"
        );

        Objects.requireNonNull(
                singleCriterionScores,
                "Criterion scores cannot be null"
        );

        if (totalScore.compareTo(BigDecimal.ZERO) < 0
                || totalScore.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException(
                    "Total matching score must be between 0 and 1"
            );
        }

        singleCriterionScores = List.copyOf(singleCriterionScores);
    }
}
