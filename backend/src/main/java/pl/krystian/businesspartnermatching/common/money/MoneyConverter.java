package pl.krystian.businesspartnermatching.common.money;

import java.math.BigDecimal;

public interface MoneyConverter {

    BigDecimal convert(
            BigDecimal amount,
            CurrencyCode sourceCurrency,
            CurrencyCode targetCurrency
    );
}
