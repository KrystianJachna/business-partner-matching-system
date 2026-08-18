package pl.krystian.businesspartnermatching.matching.scoring.calculators;

import pl.krystian.businesspartnermatching.matching.scoring.MatchingCriterion;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;

public interface CriterionScoreCalculator {

    MatchingCriterion criterion();

    /**
     * Returns a score in the range [0, 1]. A null result means that the
     * criterion is not applicable because one of its optional values is
     * missing.
     */
    BigDecimal calculateScore(
            BusinessNeed need,
            BusinessOffer offer
    );
}
