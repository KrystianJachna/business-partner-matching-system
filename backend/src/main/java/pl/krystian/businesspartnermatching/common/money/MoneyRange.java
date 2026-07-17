package pl.krystian.businesspartnermatching.common.money;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoneyRange {

    private BigDecimal min;
    private BigDecimal max;

    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;

    public MoneyRange(
            BigDecimal min,
            BigDecimal max,
            CurrencyCode currency
    ) {
        validate(min, max, currency);

        this.min = min;
        this.max = max;
        this.currency = currency;
    }

    private void validate(
            BigDecimal min,
            BigDecimal max,
            CurrencyCode currency
    ) {
        if (min == null || max == null || currency == null) {
            throw new IllegalArgumentException(
                    "Minimum amount, maximum amount and currency must be provided together"
            );
        }

        if (min.signum() < 0 || max.signum() < 0) {
            throw new IllegalArgumentException(
                    "Money range values cannot be negative"
            );
        }

        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException(
                    "Minimum amount cannot be greater than maximum amount"
            );
        }
    }
}