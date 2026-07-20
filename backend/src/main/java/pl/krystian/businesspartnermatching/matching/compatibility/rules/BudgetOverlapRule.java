package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.common.money.MoneyRange;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

@Component
public class BudgetOverlapRule implements CompatibilityRule {

    @Override
    public boolean isSatisfied(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        MoneyRange budget = need.getBudget();
        MoneyRange priceRange = offer.getPriceRange();

        if (budget == null || priceRange == null) {
            return true;
        }

        if (budget.getCurrency() != priceRange.getCurrency()) {
            return false;
        }

        return budget.getMin().compareTo(priceRange.getMax()) <= 0
                && priceRange.getMin().compareTo(budget.getMax()) <= 0;
    }

    @Override
    public CompatibilityFailureReason failureReason() {
        return CompatibilityFailureReason.NO_BUDGET_OVERLAP;
    }
}
