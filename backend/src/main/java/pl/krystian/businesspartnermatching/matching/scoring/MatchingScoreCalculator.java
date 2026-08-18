package pl.krystian.businesspartnermatching.matching.scoring;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityChecker;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityResult;
import pl.krystian.businesspartnermatching.matching.scoring.exception.IncompatiblePairException;
import pl.krystian.businesspartnermatching.matching.scoring.model.MatchingScore;
import pl.krystian.businesspartnermatching.matching.scoring.model.SingleCriterionScore;
import pl.krystian.businesspartnermatching.matching.scoring.weights.ScoringWeights;
import pl.krystian.businesspartnermatching.matching.scoring.weights.ScoringWeightsProvider;
import pl.krystian.businesspartnermatching.matching.scoring.calculators.CriterionScoreCalculator;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MatchingScoreCalculator {

    private static final int SCORE_SCALE = 4;

    private final CompatibilityChecker compatibilityChecker;
    private final List<CriterionScoreCalculator> calculators;
    private final ScoringWeightsProvider scoringWeightsProvider;

    public MatchingScore calculateForNeed(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        validateArguments(
                need,
                offer
        );

        validateCompatibility(
                need,
                offer
        );

        ScoringWeights scoringWeights =
                scoringWeightsProvider.forNeed(need);

        return calculateScore(
                need,
                offer,
                scoringWeights
        );
    }

    public MatchingScore calculateForOffer(
            BusinessOffer offer,
            BusinessNeed need
    ) {
        validateArguments(
                need,
                offer
        );

        validateCompatibility(
                need,
                offer
        );

        ScoringWeights scoringWeights =
                scoringWeightsProvider.forOffer(offer);

        return calculateScore(
                need,
                offer,
                scoringWeights
        );
    }

    private MatchingScore calculateScore(
            BusinessNeed need,
            BusinessOffer offer,
            ScoringWeights scoringWeights
    ) {
        List<SingleCriterionScore> singleCriterionScores = calculators
                .stream()
                .map(calculator -> {
                    BigDecimal score = calculator.calculateScore(
                            need,
                            offer
                    );

                    return score == null
                            ? null
                            : new SingleCriterionScore(
                                    calculator.criterion(),
                                    score
                            );
                })
                .filter(Objects::nonNull)
                .toList();

        BigDecimal activeWeightSum = singleCriterionScores
                .stream()
                .map(criterionScore -> scoringWeights.weightOf(
                        criterionScore.criterion()
                ))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalScore = singleCriterionScores
                .stream()
                .map(criterionScore -> calculateWeightedScore(
                        criterionScore,
                        scoringWeights,
                        activeWeightSum
                ))
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                )
                .setScale(
                        SCORE_SCALE,
                        RoundingMode.HALF_UP
                );

        return new MatchingScore(
                totalScore,
                singleCriterionScores
        );
    }

    private BigDecimal calculateWeightedScore(
            SingleCriterionScore singleCriterionScore,
            ScoringWeights scoringWeights,
            BigDecimal activeWeightSum
    ) {
        if (activeWeightSum.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return singleCriterionScore
                .value()
                .multiply(
                        scoringWeights.weightOf(
                                singleCriterionScore.criterion()
                        )
                )
                .divide(activeWeightSum, SCORE_SCALE, RoundingMode.HALF_UP);
    }

    private void validateCompatibility(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        CompatibilityResult compatibilityResult =
                compatibilityChecker.check(
                        need,
                        offer
                );

        if (!compatibilityResult.compatible()) {
            throw new IncompatiblePairException(
                    compatibilityResult.failureReasons()
            );
        }
    }

    private void validateArguments(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        Objects.requireNonNull(
                need,
                "Business need cannot be null"
        );

        Objects.requireNonNull(
                offer,
                "Business offer cannot be null"
        );
    }
}
