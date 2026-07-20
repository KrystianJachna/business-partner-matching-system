package pl.krystian.businesspartnermatching.matching.preference.ranking;

import pl.krystian.businesspartnermatching.matching.preference.model.Preference;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.util.List;

public interface OfferPreferenceRankingGenerator {

    List<Preference<BusinessOffer>> generateRanking(
            BusinessNeed need,
            List<BusinessOffer> offers
    );
}
