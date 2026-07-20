package pl.krystian.businesspartnermatching.common.money;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;

@Component
public class FixedRateMoneyConverter implements MoneyConverter {

    private static final int RESULT_SCALE = 2;

    /*
        * Exchange rates to PLN (Polish Zloty) are hardcoded for simplicity.
        * In a real-world application, these rates would be fetched from a reliable source.
     */
    private static final Map<String, BigDecimal> RATES_TO_PLN = Map.of(
            "PLN", BigDecimal.ONE,
            "EUR", new BigDecimal("4.30"),
            "USD", new BigDecimal("4.00")
    );

    @Override
    public BigDecimal convert(
            BigDecimal amount,
            CurrencyCode sourceCurrency,
            CurrencyCode targetCurrency
    ) {
        validateArguments(
                amount,
                sourceCurrency,
                targetCurrency
        );

        if (sourceCurrency == targetCurrency) {
            return amount.setScale(
                    RESULT_SCALE,
                    RoundingMode.HALF_UP
            );
        }

        BigDecimal sourceRate = rateToPln(sourceCurrency);
        BigDecimal targetRate = rateToPln(targetCurrency);

        BigDecimal amountInPln = amount.multiply(sourceRate);

        return amountInPln.divide(
                targetRate,
                RESULT_SCALE,
                RoundingMode.HALF_UP
        );
    }

    private BigDecimal rateToPln(CurrencyCode currency) {
        BigDecimal rate = RATES_TO_PLN.get(currency.name());

        if (rate == null) {
            throw new IllegalArgumentException(
                    "Exchange rate is not available for currency: "
                            + currency
            );
        }

        return rate;
    }

    private void validateArguments(
            BigDecimal amount,
            CurrencyCode sourceCurrency,
            CurrencyCode targetCurrency
    ) {
        Objects.requireNonNull(
                amount,
                "Amount cannot be null"
        );

        Objects.requireNonNull(
                sourceCurrency,
                "Source currency cannot be null"
        );

        Objects.requireNonNull(
                targetCurrency,
                "Target currency cannot be null"
        );

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Amount cannot be negative"
            );
        }
    }
}
