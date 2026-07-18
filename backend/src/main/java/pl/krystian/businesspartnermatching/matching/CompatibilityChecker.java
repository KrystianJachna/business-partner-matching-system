package pl.krystian.businesspartnermatching.matching;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.need.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.BusinessOffer;

@Component
public class CompatibilityChecker {

    public boolean isCompatible(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        return false;
    }
}
