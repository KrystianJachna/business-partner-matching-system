package pl.krystian.businesspartnermatching.matching.scoring;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityChecker;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityResult;
import pl.krystian.businesspartnermatching.matching.preference.PreferenceProfile;
import pl.krystian.businesspartnermatching.matching.preference.PreferenceProfileProvider;
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
        Objects.requireNonNull(
                need,
                "Business need cannot be null"
        );

        Objects.requireNonNull(
                offer,
                "Business offer cannot be null"
        );

        CompatibilityResult compatibilityResult =
                compatibilityChecker.check(need, offer);

        if (!compatibilityResult.compatible()) {
            throw new IncompatiblePairException(
                    compatibilityResult.failureReasons()
            );
        }

        PreferenceProfile preferenceProfile =
                preferenceProfileProvider.forNeed(need);

        List<CriterionScore> criterionScores = calculators
                .stream()
                .map(calculator -> new CriterionScore(
                        calculator.criterion(),
                        calculator.calculateScore(need, offer)
                ))
                .toList();

        BigDecimal totalScore = criterionScores
                .stream()
                .map(score ->
                        score.value().multiply(
                                preferenceProfile.weightOf(
                                        score.criterion()
                                )
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
}
