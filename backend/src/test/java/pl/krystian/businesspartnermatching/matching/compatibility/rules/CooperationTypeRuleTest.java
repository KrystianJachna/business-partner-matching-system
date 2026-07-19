package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.common.cooperation.CooperationType;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CooperationTypeRuleTest {

    private CooperationTypeRule rule;

    @BeforeEach
    void setUp() {
        rule = new CooperationTypeRule();
    }

    @Test
    void shouldBeSatisfiedWhenCooperationTypesAreEqual() {
        // given
        CooperationType type = CooperationType.values()[0];

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getCooperationType()).thenReturn(type);
        when(offer.getCooperationType()).thenReturn(type);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldNotBeSatisfiedWhenCooperationTypesAreDifferent() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getCooperationType())
                .thenReturn(CooperationType.values()[0]);

        when(offer.getCooperationType())
                .thenReturn(CooperationType.values()[1]);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldReturnIncompatibleCooperationTypeFailureReason() {
        assertThat(rule.failureReason())
                .isEqualTo(
                        CompatibilityFailureReason
                                .INCOMPATIBLE_COOPERATION_TYPE
                );
    }
}
