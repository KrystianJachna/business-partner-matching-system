package pl.krystian.businesspartnermatching.matching.model.dto;

import pl.krystian.businesspartnermatching.matching.algorithm.model.Match;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityChecker;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingScoreCalculator;
import pl.krystian.businesspartnermatching.matching.scoring.model.MatchingScore;
import pl.krystian.businesspartnermatching.matching.scoring.weights.ScoringWeights;
import pl.krystian.businesspartnermatching.matching.scoring.weights.ScoringWeightsProvider;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

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
            ScoringWeightsProvider scoringWeightsProvider,
            CompatibilityChecker compatibilityChecker
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
                compatibilityReasons(compatibilityChecker, need, offer)
        );
    }

    private static List<CompatibilityReasonResponse> compatibilityReasons(
            CompatibilityChecker compatibilityChecker,
            BusinessNeed need,
            BusinessOffer offer
    ) {
        var failureReasons = compatibilityChecker.check(need, offer)
                .failureReasons();

        return Stream.of(
                        CompatibilityFailureReason.NO_COMMON_SPECIALIZATION,
                        CompatibilityFailureReason.NO_BUDGET_OVERLAP,
                        CompatibilityFailureReason.NO_DATE_OVERLAP,
                        CompatibilityFailureReason.INSUFFICIENT_PARTNER_EXPERIENCE,
                        CompatibilityFailureReason.DISTANCE_LIMIT_EXCEEDED,
                        CompatibilityFailureReason.INCOMPATIBLE_COOPERATION_TYPE,
                        CompatibilityFailureReason.SAME_COMPANY
                )
                .filter(reason -> !failureReasons.contains(reason))
                .map(CompatibilityReasonResponse::from)
                .toList();
    }
}
