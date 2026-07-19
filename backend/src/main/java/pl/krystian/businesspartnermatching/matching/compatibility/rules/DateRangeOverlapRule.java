package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.common.time.DateRange;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityRule;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

@Component
public class DateRangeOverlapRule implements CompatibilityRule {

    @Override
    public boolean isSatisfied(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        DateRange requiredPeriod = need.getRequiredPeriod();
        DateRange availabilityPeriod = offer.getAvailabilityPeriod();

        if (requiredPeriod == null || availabilityPeriod == null) {
            return true;
        }

        return !requiredPeriod.getFrom()
                .isAfter(availabilityPeriod.getUntil())
                && !availabilityPeriod.getFrom()
                .isAfter(requiredPeriod.getUntil());
    }

    @Override
    public CompatibilityFailureReason failureReason() {
        return CompatibilityFailureReason.NO_DATE_OVERLAP;
    }
}
