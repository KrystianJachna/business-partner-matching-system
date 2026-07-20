package pl.krystian.businesspartnermatching.matching.preference;

import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.util.List;

public interface OfferPreferenceRankingGenerator {

    List<ScoredOffer> generateRanking(
            BusinessNeed need,
            List<BusinessOffer> offers
    );
}
