package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

@Component
public class PartnerExperienceRule implements CompatibilityRule {

    @Override
    public boolean isSatisfied(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        Integer requiredExperienceYears =
                need.getMinPartnerExperienceYears();

        if (requiredExperienceYears == null
                || requiredExperienceYears == 0) {
            return true;
        }

        Integer offeredExperienceYears =
                offer.getExperienceYears();

        return offeredExperienceYears != null
                && offeredExperienceYears
                >= requiredExperienceYears;
    }

    @Override
    public CompatibilityFailureReason failureReason() {
        return CompatibilityFailureReason
                .INSUFFICIENT_PARTNER_EXPERIENCE;
    }
}
