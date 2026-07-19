package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.common.money.CurrencyCode;
import pl.krystian.businesspartnermatching.common.money.MoneyRange;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BudgetOverlapRuleTest {

    private BudgetOverlapRule rule;

    @BeforeEach
    void setUp() {
        rule = new BudgetOverlapRule();
    }

    @Test
    void shouldBeSatisfiedWhenBudgetAndPriceRangeOverlap() {
        // given
        MoneyRange budget = moneyRange(
                "10000",
                "20000",
                CurrencyCode.PLN
        );

        MoneyRange priceRange = moneyRange(
                "15000",
                "25000",
                CurrencyCode.PLN
        );

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getBudget()).thenReturn(budget);
        when(offer.getPriceRange()).thenReturn(priceRange);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldBeSatisfiedWhenRangesTouchAtBoundary() {
        // given
        MoneyRange budget = moneyRange(
                "10000",
                "20000",
                CurrencyCode.PLN
        );

        MoneyRange priceRange = moneyRange(
                "20000",
                "30000",
                CurrencyCode.PLN
        );

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getBudget()).thenReturn(budget);
        when(offer.getPriceRange()).thenReturn(priceRange);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldNotBeSatisfiedWhenRangesDoNotOverlap() {
        // given
        MoneyRange budget = moneyRange(
                "10000",
                "20000",
                CurrencyCode.PLN
        );

        MoneyRange priceRange = moneyRange(
                "25000",
                "30000",
                CurrencyCode.PLN
        );

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getBudget()).thenReturn(budget);
        when(offer.getPriceRange()).thenReturn(priceRange);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldNotBeSatisfiedWhenCurrenciesAreDifferent() {
        // given
        MoneyRange budget = moneyRange(
                "10000",
                "20000",
                CurrencyCode.PLN
        );

        MoneyRange priceRange = moneyRange(
                "10000",
                "20000",
                CurrencyCode.EUR
        );

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getBudget()).thenReturn(budget);
        when(offer.getPriceRange()).thenReturn(priceRange);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldBeSatisfiedWhenBudgetIsNotSpecified() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getBudget()).thenReturn(null);
        when(offer.getPriceRange()).thenReturn(
                moneyRange(
                        "10000",
                        "20000",
                        CurrencyCode.PLN
                )
        );

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldBeSatisfiedWhenPriceRangeIsNotSpecified() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getBudget()).thenReturn(
                moneyRange(
                        "10000",
                        "20000",
                        CurrencyCode.PLN
                )
        );

        when(offer.getPriceRange()).thenReturn(null);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldReturnNoBudgetOverlapFailureReason() {
        assertThat(rule.failureReason())
                .isEqualTo(
                        CompatibilityFailureReason.NO_BUDGET_OVERLAP
                );
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
