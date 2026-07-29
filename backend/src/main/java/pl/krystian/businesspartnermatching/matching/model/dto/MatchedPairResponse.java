package pl.krystian.businesspartnermatching.matching.model.dto;

import pl.krystian.businesspartnermatching.matching.algorithm.model.Match;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingScoreCalculator;
import pl.krystian.businesspartnermatching.matching.scoring.model.MatchingScore;
import pl.krystian.businesspartnermatching.matching.scoring.weights.ScoringWeights;
import pl.krystian.businesspartnermatching.matching.scoring.weights.ScoringWeightsProvider;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Comparator;

public record MatchedPairResponse(
        Long needId,
        String needTitle,
        Long needCompanyId,
        String needCompanyName,
        Long offerId,
        String offerTitle,
        Long offerCompanyId,
        String offerCompanyName,
        BigDecimal totalScore,
        List<CriterionScoreResponse> criterionScores,
        List<CompatibilityReasonResponse> compatibilityReasons
) {

    public static MatchedPairResponse from(
            Match<BusinessNeed, BusinessOffer> match,
            MatchingScoreCalculator matchingScoreCalculator,
            ScoringWeightsProvider scoringWeightsProvider
    ) {
        BusinessNeed need =
                match.leftParticipant();

        BusinessOffer offer =
                match.rightParticipant();

        MatchingScore matchingScore = matchingScoreCalculator.calculateForNeed(
                need,
                offer
        );
        ScoringWeights weights = scoringWeightsProvider.forNeed(need);

        return new MatchedPairResponse(
                need.getId(),
                need.getTitle(),
                need.getCompany().getId(),
                need.getCompany().getName(),
                offer.getId(),
                offer.getTitle(),
                offer.getCompany().getId(),
                offer.getCompany().getName(),
                matchingScore.totalScore(),
                matchingScore.singleCriterionScores()
                        .stream()
                        .map(score -> CriterionScoreResponse.from(
                                score,
                                weights.weightOf(score.criterion())
                        ))
                        .toList(),
                compatibilityReasons(matchingScore)
        );
    }

    private static List<CompatibilityReasonResponse> compatibilityReasons(
            MatchingScore matchingScore
    ) {
        List<CompatibilityReasonResponse> reasons = matchingScore
                .singleCriterionScores()
                .stream()
                .map(CompatibilityReasonResponse::from)
                .flatMap(java.util.Optional::stream)
                .toList();

        if (!reasons.isEmpty()) {
            return reasons;
        }

        return matchingScore.singleCriterionScores()
                .stream()
                .max(Comparator.comparing(
                        score -> score.value()
                ))
                .map(score -> List.of(
                        new CompatibilityReasonResponse(
                                "BEST_AVAILABLE_CRITERION",
                                "The strongest aspect of this match is "
                                        + score.criterion().name().toLowerCase()
                                        + "."
                        )
                ))
                .orElseGet(List::of);
    }
}
