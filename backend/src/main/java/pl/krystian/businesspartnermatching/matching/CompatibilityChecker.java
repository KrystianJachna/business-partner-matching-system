package pl.krystian.businesspartnermatching.matching;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

@Component
public class CompatibilityChecker {

    public boolean isCompatible(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        return false;
    }
}
