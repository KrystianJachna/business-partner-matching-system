package pl.krystian.businesspartnermatching.matching.scoring.weights;

import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

public interface ScoringWeightsProvider {

    ScoringWeights forNeed(BusinessNeed need);

    ScoringWeights forOffer(BusinessOffer offer);
}
