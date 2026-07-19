package pl.krystian.businesspartnermatching.matching.scoring.calculators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.common.money.CurrencyCode;
import pl.krystian.businesspartnermatching.common.money.MoneyRange;
import pl.krystian.businesspartnermatching.matching.criterion.MatchingCriterion;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BudgetScoreCalculatorTest {

    private BudgetScoreCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new BudgetScoreCalculator();
    }

    @Test
    void shouldReturnBudgetCriterion() {
        assertThat(calculator.criterion())
                .isEqualTo(MatchingCriterion.BUDGET);
    }

    @Test
    void shouldReturnOneWhenPriceRangeFullyCoversBudget() {
        // given
        BusinessNeed need = needWithBudget(
                moneyRange("10000", "20000", CurrencyCode.PLN)
        );

        BusinessOffer offer = offerWithPriceRange(
                moneyRange("8000", "22000", CurrencyCode.PLN)
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo("1.0000");
    }

    @Test
    void shouldReturnPartialScoreWhenRangesPartiallyOverlap() {
        // given
        BusinessNeed need = needWithBudget(
                moneyRange("10000", "20000", CurrencyCode.PLN)
        );

        BusinessOffer offer = offerWithPriceRange(
                moneyRange("15000", "25000", CurrencyCode.PLN)
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo("0.5000");
    }

    @Test
    void shouldReturnZeroWhenRangesDoNotOverlap() {
        // given
        BusinessNeed need = needWithBudget(
                moneyRange("10000", "20000", CurrencyCode.PLN)
        );

        BusinessOffer offer = offerWithPriceRange(
                moneyRange("25000", "30000", CurrencyCode.PLN)
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldReturnZeroWhenRangesOnlyTouchAtBoundary() {
        // given
        BusinessNeed need = needWithBudget(
                moneyRange("10000", "20000", CurrencyCode.PLN)
        );

        BusinessOffer offer = offerWithPriceRange(
                moneyRange("20000", "30000", CurrencyCode.PLN)
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldReturnZeroWhenCurrenciesAreDifferent() {
        // given
        BusinessNeed need = needWithBudget(
                moneyRange("10000", "20000", CurrencyCode.PLN)
        );

        BusinessOffer offer = offerWithPriceRange(
                moneyRange("10000", "20000", CurrencyCode.EUR)
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldReturnOneWhenBudgetIsNotSpecified() {
        // given
        BusinessNeed need = needWithBudget(null);

        BusinessOffer offer = offerWithPriceRange(
                moneyRange("10000", "20000", CurrencyCode.PLN)
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void shouldReturnOneWhenPriceRangeIsNotSpecified() {
        // given
        BusinessNeed need = needWithBudget(
                moneyRange("10000", "20000", CurrencyCode.PLN)
        );

        BusinessOffer offer = offerWithPriceRange(null);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void shouldReturnOneWhenPointBudgetIsCoveredByPriceRange() {
        // given
        BusinessNeed need = needWithBudget(
                moneyRange("15000", "15000", CurrencyCode.PLN)
        );

        BusinessOffer offer = offerWithPriceRange(
                moneyRange("10000", "20000", CurrencyCode.PLN)
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void shouldReturnZeroWhenPointBudgetIsNotCoveredByPriceRange() {
        // given
        BusinessNeed need = needWithBudget(
                moneyRange("15000", "15000", CurrencyCode.PLN)
        );

        BusinessOffer offer = offerWithPriceRange(
                moneyRange("16000", "20000", CurrencyCode.PLN)
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ZERO);
    }

    private BusinessNeed needWithBudget(MoneyRange budget) {
        BusinessNeed need = mock(BusinessNeed.class);
        when(need.getBudget()).thenReturn(budget);
        return need;
    }

    private BusinessOffer offerWithPriceRange(MoneyRange priceRange) {
        BusinessOffer offer = mock(BusinessOffer.class);
        when(offer.getPriceRange()).thenReturn(priceRange);
        return offer;
    }

    private MoneyRange moneyRange(
            String min,
            String max,
            CurrencyCode currency
    ) {
        return new MoneyRange(
                new BigDecimal(min),
                new BigDecimal(max),
                currency
        );
    }
}
