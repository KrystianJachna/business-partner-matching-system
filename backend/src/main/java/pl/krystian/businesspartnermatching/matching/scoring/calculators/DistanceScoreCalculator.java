package pl.krystian.businesspartnermatching.matching.scoring.calculators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingCriterion;
import pl.krystian.businesspartnermatching.matching.distance.DistanceCalculator;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class DistanceScoreCalculator
        implements CriterionScoreCalculator {

    private static final int SCORE_SCALE = 4;

    private final DistanceCalculator distanceCalculator;

    @Override
    public MatchingCriterion criterion() {
        return MatchingCriterion.DISTANCE;
    }

    @Override
    public BigDecimal calculateScore(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        Integer effectiveLimit = effectiveLimit(
                need.getMaxDistanceKm(),
                offer.getServiceRadiusKm()
        );

        if (effectiveLimit == null) {
            return BigDecimal.ONE;
        }

        double distanceKm =
                distanceCalculator.calculateDistanceKm(
                        need.getCompany(),
                        offer.getCompany()
                );

        if (distanceKm >= effectiveLimit) {
            return BigDecimal.ZERO;
        }

        BigDecimal distanceRatio =
                BigDecimal.valueOf(distanceKm)
                        .divide(
                                BigDecimal.valueOf(effectiveLimit),
                                SCORE_SCALE,
                                RoundingMode.HALF_UP
                        );

        return BigDecimal.ONE.subtract(distanceRatio);
    }

    private Integer effectiveLimit(
            Integer needLimit,
            Integer offerLimit
    ) {
        if (needLimit == null) {
            return offerLimit;
        }

        if (offerLimit == null) {
            return needLimit;
        }

        return Math.min(needLimit, offerLimit);
    }
}
