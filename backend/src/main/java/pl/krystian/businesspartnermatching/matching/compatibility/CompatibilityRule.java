package pl.krystian.businesspartnermatching.matching.compatibility;

import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

public interface CompatibilityRule {

    boolean isSatisfied(
            BusinessNeed need,
            BusinessOffer offer
    );

    CompatibilityFailureReason failureReason();
}
