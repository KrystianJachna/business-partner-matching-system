package pl.krystian.businesspartnermatching.matching.scoring.calculators;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.catalog.specialization.model.entity.Specialization;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingCriterion;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SpecializationScoreCalculator
        implements CriterionScoreCalculator {

    private static final int SCORE_SCALE = 4;

    @Override
    public MatchingCriterion criterion() {
        return MatchingCriterion.SPECIALIZATION;
    }

    @Override
    public BigDecimal calculateScore(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        Set<Specialization> requiredSpecializations =
                need.getRequiredSpecializations();

        if (requiredSpecializations.isEmpty()) {
            return BigDecimal.ONE;
        }

        Set<String> offeredSpecializationCodes =
                offer.getOfferedSpecializations()
                        .stream()
                        .map(Specialization::getCode)
                        .collect(Collectors.toSet());

        long matchedSpecializations = requiredSpecializations
                .stream()
                .map(Specialization::getCode)
                .filter(offeredSpecializationCodes::contains)
                .count();

        return BigDecimal.valueOf(matchedSpecializations)
                .divide(
                        BigDecimal.valueOf(
                                requiredSpecializations.size()
                        ),
                        SCORE_SCALE,
                        RoundingMode.HALF_UP
                );
    }
}
