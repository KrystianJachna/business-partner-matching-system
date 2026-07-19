package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityRule;
import pl.krystian.businesspartnermatching.matching.distance.DistanceCalculator;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

@Component
@RequiredArgsConstructor
public class DistanceRule implements CompatibilityRule {

    private final DistanceCalculator distanceCalculator;

    @Override
    public boolean isSatisfied(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        Integer needMaxDistanceKm = need.getMaxDistanceKm();
        Integer offerServiceRadiusKm = offer.getServiceRadiusKm();

        if (needMaxDistanceKm == null
                && offerServiceRadiusKm == null) {
            return true;
        }

        double distanceKm =
                distanceCalculator.calculateDistanceKm(
                        need.getCompany(),
                        offer.getCompany()
                );

        return isWithinLimit(distanceKm, needMaxDistanceKm)
                && isWithinLimit(distanceKm, offerServiceRadiusKm);
    }

    @Override
    public CompatibilityFailureReason failureReason() {
        return CompatibilityFailureReason.DISTANCE_LIMIT_EXCEEDED;
    }

    private boolean isWithinLimit(
            double distanceKm,
            Integer limitKm
    ) {
        return limitKm == null || distanceKm <= limitKm;
    }
}
