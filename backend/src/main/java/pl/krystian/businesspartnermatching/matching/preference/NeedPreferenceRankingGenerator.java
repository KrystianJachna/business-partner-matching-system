package pl.krystian.businesspartnermatching.matching.preference;

import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.util.List;

public interface NeedPreferenceRankingGenerator {

    List<ScoredNeed> generateRanking(
            BusinessOffer offer,
            List<BusinessNeed> needs
    );
}
