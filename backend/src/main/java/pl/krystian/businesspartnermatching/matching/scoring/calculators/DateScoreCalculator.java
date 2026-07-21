package pl.krystian.businesspartnermatching.matching.scoring.calculators;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.common.time.DateRange;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingCriterion;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class DateScoreCalculator
        implements CriterionScoreCalculator {

    private static final int SCORE_SCALE = 4;

    @Override
    public MatchingCriterion criterion() {
        return MatchingCriterion.DATE;
    }

    @Override
    public BigDecimal calculateScore(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        DateRange requiredPeriod = need.getRequiredPeriod();
        DateRange availabilityPeriod = offer.getAvailabilityPeriod();

        if (requiredPeriod == null || availabilityPeriod == null) {
            return BigDecimal.ONE;
        }

        LocalDate overlapStart = requiredPeriod.getFrom()
                .isAfter(availabilityPeriod.getFrom())
                ? requiredPeriod.getFrom()
                : availabilityPeriod.getFrom();

        LocalDate overlapEnd = requiredPeriod.getUntil()
                .isBefore(availabilityPeriod.getUntil())
                ? requiredPeriod.getUntil()
                : availabilityPeriod.getUntil();

        if (overlapStart.isAfter(overlapEnd)) {
            return BigDecimal.ZERO;
        }

        long overlapDays = ChronoUnit.DAYS.between(
                overlapStart,
                overlapEnd
        ) + 1;

        long requiredDays = ChronoUnit.DAYS.between(
                requiredPeriod.getFrom(),
                requiredPeriod.getUntil()
        ) + 1;

        return BigDecimal.valueOf(overlapDays)
                .divide(
                        BigDecimal.valueOf(requiredDays),
                        SCORE_SCALE,
                        RoundingMode.HALF_UP
                );
    }
}
