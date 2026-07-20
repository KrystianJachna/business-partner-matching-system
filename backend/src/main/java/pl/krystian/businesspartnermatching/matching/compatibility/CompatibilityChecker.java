package pl.krystian.businesspartnermatching.matching.compatibility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.matching.compatibility.rules.CompatibilityRule;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CompatibilityChecker {

    private final List<CompatibilityRule> rules;

    public CompatibilityResult check(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        Objects.requireNonNull(
                need,
                "Business need cannot be null"
        );

        Objects.requireNonNull(
                offer,
                "Business offer cannot be null"
        );

        List<CompatibilityFailureReason> failureReasons =
                rules.stream()
                        .filter(rule ->
                                !rule.isSatisfied(need, offer)
                        )
                        .map(CompatibilityRule::failureReason)
                        .toList();

        return new CompatibilityResult(failureReasons);
    }

    public boolean isCompatible(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        return check(need, offer).compatible();
    }
}
