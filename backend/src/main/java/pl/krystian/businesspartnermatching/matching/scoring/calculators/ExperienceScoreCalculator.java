package pl.krystian.businesspartnermatching.matching.scoring.calculators;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.company.model.entity.Company;
import pl.krystian.businesspartnermatching.matching.criterion.MatchingCriterion;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

@Component
public class ExperienceScoreCalculator
        implements CriterionScoreCalculator {

    private static final int SCORE_SCALE = 4;
    private static final BigDecimal BASE_SCORE =
            new BigDecimal("0.5");

    private final Clock clock;

    public ExperienceScoreCalculator() {
        this(Clock.systemDefaultZone());
    }

    ExperienceScoreCalculator(Clock clock) {
        this.clock = clock;
    }

    @Override
    public MatchingCriterion criterion() {
        return MatchingCriterion.EXPERIENCE;
    }

    @Override
    public BigDecimal calculateScore(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        Integer requiredExperienceYears =
                need.getMinPartnerExperienceYears();

        if (requiredExperienceYears == null
                || requiredExperienceYears == 0) {
            return BigDecimal.ONE;
        }

        Company partnerCompany = offer.getCompany();
        LocalDate establishedAt = partnerCompany.getEstablishedAt();

        if (establishedAt == null) {
            return BigDecimal.ZERO;
        }

        int actualExperienceYears = Period.between(
                establishedAt,
                LocalDate.now(clock)
        ).getYears();

        if (actualExperienceYears < requiredExperienceYears) {
            return BigDecimal.ZERO;
        }

        int additionalExperienceYears =
                actualExperienceYears - requiredExperienceYears;

        BigDecimal additionalScore =
                BigDecimal.valueOf(additionalExperienceYears)
                        .divide(
                                BigDecimal.valueOf(
                                        requiredExperienceYears
                                ),
                                SCORE_SCALE,
                                RoundingMode.HALF_UP
                        )
                        .multiply(BASE_SCORE);

        return BASE_SCORE
                .add(additionalScore)
                .min(BigDecimal.ONE)
                .setScale(
                        SCORE_SCALE,
                        RoundingMode.HALF_UP
                );
    }
}
