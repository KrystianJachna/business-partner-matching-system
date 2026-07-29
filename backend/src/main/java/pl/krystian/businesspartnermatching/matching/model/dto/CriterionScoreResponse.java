package pl.krystian.businesspartnermatching.matching.model.dto;

import pl.krystian.businesspartnermatching.matching.scoring.MatchingCriterion;
import pl.krystian.businesspartnermatching.matching.scoring.model.SingleCriterionScore;

import java.math.BigDecimal;

public record CriterionScoreResponse(
        MatchingCriterion criterion,
        BigDecimal score,
        BigDecimal weight,
        BigDecimal weightedScore
) {

    public static CriterionScoreResponse from(
            SingleCriterionScore criterionScore,
            BigDecimal weight
    ) {
        return new CriterionScoreResponse(
                criterionScore.criterion(),
                criterionScore.value(),
                weight,
                criterionScore.value().multiply(weight)
        );
    }
}
