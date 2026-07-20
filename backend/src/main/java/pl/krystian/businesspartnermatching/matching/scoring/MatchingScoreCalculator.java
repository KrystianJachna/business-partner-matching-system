package pl.krystian.businesspartnermatching.matching.scoring;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityChecker;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityResult;
import pl.krystian.businesspartnermatching.matching.preference.profile.PreferenceProfile;
import pl.krystian.businesspartnermatching.matching.preference.profile.PreferenceProfileProvider;
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
    private final PreferenceProfileProvider preferenceProfileProvider;

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

        PreferenceProfile preferenceProfile =
                preferenceProfileProvider.forNeed(need);

        return calculateScore(
                need,
                offer,
                preferenceProfile
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

        PreferenceProfile preferenceProfile =
                preferenceProfileProvider.forOffer(offer);

        return calculateScore(
                need,
                offer,
                preferenceProfile
        );
    }

    private MatchingScore calculateScore(
            BusinessNeed need,
            BusinessOffer offer,
            PreferenceProfile preferenceProfile
    ) {
        List<CriterionScore> criterionScores = calculators
                .stream()
                .map(calculator -> new CriterionScore(
                        calculator.criterion(),
                        calculator.calculateScore(
                                need,
                                offer
                        )
                ))
                .toList();

        BigDecimal totalScore = criterionScores
                .stream()
                .map(criterionScore ->
                        calculateWeightedScore(
                                criterionScore,
                                preferenceProfile
                        )
                )
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
                criterionScores
        );
    }

    private BigDecimal calculateWeightedScore(
            CriterionScore criterionScore,
            PreferenceProfile preferenceProfile
    ) {
        return criterionScore
                .value()
                .multiply(
                        preferenceProfile.weightOf(
                                criterionScore.criterion()
                        )
                );
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
