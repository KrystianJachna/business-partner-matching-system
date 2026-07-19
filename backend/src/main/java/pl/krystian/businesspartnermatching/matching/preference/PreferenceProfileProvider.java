package pl.krystian.businesspartnermatching.matching.preference;

import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

public interface PreferenceProfileProvider {

    PreferenceProfile forNeed(BusinessNeed need);

    PreferenceProfile forOffer(BusinessOffer offer);
}
