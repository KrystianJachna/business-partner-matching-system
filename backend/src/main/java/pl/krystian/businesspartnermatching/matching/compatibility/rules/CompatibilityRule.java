package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

public interface CompatibilityRule {

    boolean isSatisfied(
            BusinessNeed need,
            BusinessOffer offer
    );

    CompatibilityFailureReason failureReason();
}
