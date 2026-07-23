package pl.krystian.businesspartnermatching.common.money.model.dto;

import pl.krystian.businesspartnermatching.common.money.CurrencyCode;
import pl.krystian.businesspartnermatching.common.money.MoneyRange;

import java.math.BigDecimal;

public record MoneyRangeResponse(
        BigDecimal min,
        BigDecimal max,
        CurrencyCode currency
) {

    public static MoneyRangeResponse from(MoneyRange moneyRange) {
        if (moneyRange == null) {
            return null;
        }

        return new MoneyRangeResponse(
                moneyRange.getMin(),
                moneyRange.getMax(),
                moneyRange.getCurrency()
        );
    }
}
