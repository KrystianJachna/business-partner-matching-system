package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.company.model.entity.Company;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

@Component
public class PartnerExperienceRule implements CompatibilityRule {

    private final Clock clock;

    public PartnerExperienceRule() {
        this(Clock.systemDefaultZone());
    }

    PartnerExperienceRule(Clock clock) {
        this.clock = clock;
    }

    @Override
    public boolean isSatisfied(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        Integer requiredExperienceYears =
                need.getMinPartnerExperienceYears();

        if (requiredExperienceYears == null) {
            return true;
        }

        Company partnerCompany = offer.getCompany();
        LocalDate establishedAt = partnerCompany.getEstablishedAt();

        if (establishedAt == null) {
            return false;
        }

        LocalDate currentDate = LocalDate.now(clock);

        int experienceYears = Period.between(
                establishedAt,
                currentDate
        ).getYears();

        return experienceYears >= requiredExperienceYears;
    }

    @Override
    public CompatibilityFailureReason failureReason() {
        return CompatibilityFailureReason
                .INSUFFICIENT_PARTNER_EXPERIENCE;
    }
}
