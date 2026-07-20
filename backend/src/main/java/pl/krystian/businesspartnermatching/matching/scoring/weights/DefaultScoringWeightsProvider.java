package pl.krystian.businesspartnermatching.matching.scoring.weights;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.matching.criterion.MatchingCriterion;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@Component
public class DefaultScoringWeightsProvider
        implements ScoringWeightsProvider {

    private static final ScoringWeights NEED_PROFILE =
            new ScoringWeights(
                    Map.of(
                            MatchingCriterion.SPECIALIZATION,
                            new BigDecimal("0.35"),
                            MatchingCriterion.BUDGET,
                            new BigDecimal("0.25"),
                            MatchingCriterion.DATE,
                            new BigDecimal("0.15"),
                            MatchingCriterion.DISTANCE,
                            new BigDecimal("0.10"),
                            MatchingCriterion.EXPERIENCE,
                            new BigDecimal("0.15")
                    )
            );

    private static final ScoringWeights OFFER_PROFILE =
            new ScoringWeights(
                    Map.of(
                            MatchingCriterion.SPECIALIZATION,
                            new BigDecimal("0.35"),
                            MatchingCriterion.BUDGET,
                            new BigDecimal("0.30"),
                            MatchingCriterion.DATE,
                            new BigDecimal("0.20"),
                            MatchingCriterion.DISTANCE,
                            new BigDecimal("0.10"),
                            MatchingCriterion.EXPERIENCE,
                            new BigDecimal("0.05")
                    )
            );

    @Override
    public ScoringWeights forNeed(BusinessNeed need) {
        Objects.requireNonNull(
                need,
                "Business need cannot be null"
        );

        return NEED_PROFILE;
    }

    @Override
    public ScoringWeights forOffer(BusinessOffer offer) {
        Objects.requireNonNull(
                offer,
                "Business offer cannot be null"
        );

        return OFFER_PROFILE;
    }
}
