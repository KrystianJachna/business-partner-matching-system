package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.common.money.MoneyConverter;
import pl.krystian.businesspartnermatching.common.money.MoneyRange;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class BudgetOverlapRule implements CompatibilityRule {

    private final MoneyConverter moneyConverter;

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

        BigDecimal convertedPriceMin = moneyConverter.convert(
                priceRange.getMin(),
                priceRange.getCurrency(),
                budget.getCurrency()
        );

        BigDecimal convertedPriceMax = moneyConverter.convert(
                priceRange.getMax(),
                priceRange.getCurrency(),
                budget.getCurrency()
        );

        return budget.getMin().compareTo(convertedPriceMax) <= 0
                && convertedPriceMin.compareTo(budget.getMax()) <= 0;
    }

    @Override
    public CompatibilityFailureReason failureReason() {
        return CompatibilityFailureReason.NO_BUDGET_OVERLAP;
    }
}
