package pl.krystian.businesspartnermatching.matching.scoring.calculators;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.common.money.MoneyRange;
import pl.krystian.businesspartnermatching.matching.criterion.MatchingCriterion;
import pl.krystian.businesspartnermatching.matching.scoring.CriterionScoreCalculator;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class BudgetScoreCalculator
        implements CriterionScoreCalculator {

    private static final int SCORE_SCALE = 4;

    @Override
    public MatchingCriterion criterion() {
        return MatchingCriterion.BUDGET;
    }

    @Override
    public BigDecimal calculateScore(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        MoneyRange budget = need.getBudget();
        MoneyRange priceRange = offer.getPriceRange();

        if (budget == null || priceRange == null) {
            return BigDecimal.ONE;
        }

        if (budget.getCurrency() != priceRange.getCurrency()) {
            return BigDecimal.ZERO;
        }

        BigDecimal budgetLength =
                budget.getMax().subtract(budget.getMin());

        if (budgetLength.compareTo(BigDecimal.ZERO) == 0) {
            boolean pointBudgetIsCovered =
                    priceRange.getMin()
                            .compareTo(budget.getMin()) <= 0
                            && priceRange.getMax()
                            .compareTo(budget.getMax()) >= 0;

            return pointBudgetIsCovered
                    ? BigDecimal.ONE
                    : BigDecimal.ZERO;
        }

        BigDecimal overlapStart =
                budget.getMin().max(priceRange.getMin());

        BigDecimal overlapEnd =
                budget.getMax().min(priceRange.getMax());

        if (overlapStart.compareTo(overlapEnd) >= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal overlapLength =
                overlapEnd.subtract(overlapStart);

        return overlapLength.divide(
                budgetLength,
                SCORE_SCALE,
                RoundingMode.HALF_UP
        );
    }
}
