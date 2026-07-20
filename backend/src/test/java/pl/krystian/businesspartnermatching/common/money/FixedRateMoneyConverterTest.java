package pl.krystian.businesspartnermatching.common.money;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FixedRateMoneyConverterTest {

    private FixedRateMoneyConverter moneyConverter;

    @BeforeEach
    void setUp() {
        moneyConverter = new FixedRateMoneyConverter();
    }

    @Test
    void shouldReturnAmountWhenCurrenciesAreTheSame() {
        // given
        BigDecimal amount = new BigDecimal("100.00");

        // when
        BigDecimal convertedAmount = moneyConverter.convert(
                amount,
                CurrencyCode.PLN,
                CurrencyCode.PLN
        );

        // then
        assertThat(convertedAmount)
                .isEqualByComparingTo("100.00");
    }

    @Test
    void shouldConvertEuroToPolishZloty() {
        // given
        BigDecimal amount = new BigDecimal("100.00");

        // when
        BigDecimal convertedAmount = moneyConverter.convert(
                amount,
                CurrencyCode.EUR,
                CurrencyCode.PLN
        );

        // then
        assertThat(convertedAmount)
                .isEqualByComparingTo("430.00");
    }

    @Test
    void shouldConvertPolishZlotyToEuro() {
        // given
        BigDecimal amount = new BigDecimal("430.00");

        // when
        BigDecimal convertedAmount = moneyConverter.convert(
                amount,
                CurrencyCode.PLN,
                CurrencyCode.EUR
        );

        // then
        assertThat(convertedAmount)
                .isEqualByComparingTo("100.00");
    }

    @Test
    void shouldConvertBetweenTwoNonBaseCurrencies() {
        // given
        BigDecimal amount = new BigDecimal("100.00");

        // when
        BigDecimal convertedAmount = moneyConverter.convert(
                amount,
                CurrencyCode.EUR,
                CurrencyCode.USD
        );

        // then
        assertThat(convertedAmount)
                .isEqualByComparingTo("107.50");
    }

    @Test
    void shouldRoundConvertedAmountToTwoDecimalPlaces() {
        // given
        BigDecimal amount = new BigDecimal("10.00");

        // when
        BigDecimal convertedAmount = moneyConverter.convert(
                amount,
                CurrencyCode.PLN,
                CurrencyCode.EUR
        );

        // then
        assertThat(convertedAmount)
                .isEqualByComparingTo("2.33");

        assertThat(convertedAmount.scale())
                .isEqualTo(2);
    }

    @Test
    void shouldConvertZeroAmount() {
        // given
        BigDecimal amount = BigDecimal.ZERO;

        // when
        BigDecimal convertedAmount = moneyConverter.convert(
                amount,
                CurrencyCode.EUR,
                CurrencyCode.PLN
        );

        // then
        assertThat(convertedAmount)
                .isEqualByComparingTo("0.00");
    }

    @Test
    void shouldRejectNegativeAmount() {
        // given
        BigDecimal amount = new BigDecimal("-100.00");

        // when / then
        assertThatThrownBy(() ->
                moneyConverter.convert(
                        amount,
                        CurrencyCode.EUR,
                        CurrencyCode.PLN
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount cannot be negative");
    }

    @Test
    void shouldRejectNullAmount() {
        // when / then
        assertThatThrownBy(() ->
                moneyConverter.convert(
                        null,
                        CurrencyCode.EUR,
                        CurrencyCode.PLN
                )
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Amount cannot be null");
    }

    @Test
    void shouldRejectNullSourceCurrency() {
        // when / then
        assertThatThrownBy(() ->
                moneyConverter.convert(
                        BigDecimal.ONE,
                        null,
                        CurrencyCode.PLN
                )
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Source currency cannot be null");
    }

    @Test
    void shouldRejectNullTargetCurrency() {
        // when / then
        assertThatThrownBy(() ->
                moneyConverter.convert(
                        BigDecimal.ONE,
                        CurrencyCode.EUR,
                        null
                )
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Target currency cannot be null");
    }
}
