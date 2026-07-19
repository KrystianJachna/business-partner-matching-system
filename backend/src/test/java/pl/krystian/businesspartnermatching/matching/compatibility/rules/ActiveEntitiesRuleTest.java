package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ActiveEntitiesRuleTest {

    private ActiveEntitiesRule rule;

    @BeforeEach
    void setUp() {
        rule = new ActiveEntitiesRule();
    }

    @Test
    void shouldBeSatisfiedWhenNeedAndOfferAreActive() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.isActive()).thenReturn(true);
        when(offer.isActive()).thenReturn(true);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldNotBeSatisfiedWhenNeedIsInactive() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.isActive()).thenReturn(false);
        when(offer.isActive()).thenReturn(true);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldNotBeSatisfiedWhenOfferIsInactive() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.isActive()).thenReturn(true);
        when(offer.isActive()).thenReturn(false);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldReturnInactiveNeedOrOfferFailureReason() {
        assertThat(rule.failureReason())
                .isEqualTo(
                        CompatibilityFailureReason
                                .INACTIVE_NEED_OR_OFFER
                );
    }
}
