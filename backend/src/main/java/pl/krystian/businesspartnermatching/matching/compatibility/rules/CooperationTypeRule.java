package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

@Component
public class CooperationTypeRule implements CompatibilityRule {

    @Override
    public boolean isSatisfied(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        return need.getCooperationType()
                == offer.getCooperationType();
    }

    @Override
    public CompatibilityFailureReason failureReason() {
        return CompatibilityFailureReason
                .INCOMPATIBLE_COOPERATION_TYPE;
    }
}
