package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.catalog.specialization.model.entity.Specialization;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommonSpecializationRuleTest {

    private CommonSpecializationRule rule;

    @BeforeEach
    void setUp() {
        rule = new CommonSpecializationRule();
    }

    @Test
    void shouldBeSatisfiedWhenThereIsCommonSpecialization() {
        // given
        Specialization specialization =
                mock(Specialization.class);

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getRequiredSpecializations())
                .thenReturn(Set.of(specialization));

        when(offer.getOfferedSpecializations())
                .thenReturn(Set.of(specialization));

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldNotBeSatisfiedWhenThereIsNoCommonSpecialization() {
        // given
        Specialization requiredSpecialization =
                mock(Specialization.class);

        Specialization offeredSpecialization =
                mock(Specialization.class);

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getRequiredSpecializations())
                .thenReturn(Set.of(requiredSpecialization));

        when(offer.getOfferedSpecializations())
                .thenReturn(Set.of(offeredSpecialization));

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldNotBeSatisfiedWhenOfferHasNoSpecializations() {
        // given
        Specialization specialization =
                mock(Specialization.class);

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getRequiredSpecializations())
                .thenReturn(Set.of(specialization));

        when(offer.getOfferedSpecializations())
                .thenReturn(Set.of());

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldReturnNoCommonSpecializationFailureReason() {
        assertThat(rule.failureReason())
                .isEqualTo(
                        CompatibilityFailureReason
                                .NO_COMMON_SPECIALIZATION
                );
    }
}
