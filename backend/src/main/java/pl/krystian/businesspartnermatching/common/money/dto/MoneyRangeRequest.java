package pl.krystian.businesspartnermatching.common.money.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import pl.krystian.businesspartnermatching.common.money.CurrencyCode;
import pl.krystian.businesspartnermatching.common.money.MoneyRange;

import java.math.BigDecimal;

public record MoneyRangeRequest(

        @NotNull
        @DecimalMin("0.00")
        BigDecimal min,

        @NotNull
        @DecimalMin("0.00")
        BigDecimal max,

        @NotNull
        CurrencyCode currency
) {

    private MoneyRange toMoneyRange() {
        return new MoneyRange(
                min,
                max,
                currency
        );
    }

    public static MoneyRange fromNullable(
            MoneyRangeRequest request
    ) {
        return request == null
                ? null
                : request.toMoneyRange();
    }
}
