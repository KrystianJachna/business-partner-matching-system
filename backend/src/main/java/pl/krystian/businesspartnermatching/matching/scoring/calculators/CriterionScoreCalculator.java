package pl.krystian.businesspartnermatching.matching.scoring.calculators;

import pl.krystian.businesspartnermatching.matching.criterion.MatchingCriterion;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;

public interface CriterionScoreCalculator {

    MatchingCriterion criterion();

    BigDecimal calculateScore(
            BusinessNeed need,
            BusinessOffer offer
    );
}
