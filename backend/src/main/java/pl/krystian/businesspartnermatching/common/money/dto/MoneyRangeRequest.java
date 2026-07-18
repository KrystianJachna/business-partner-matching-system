package pl.krystian.businesspartnermatching.common.money.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import pl.krystian.businesspartnermatching.common.money.CurrencyCode;

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
}
