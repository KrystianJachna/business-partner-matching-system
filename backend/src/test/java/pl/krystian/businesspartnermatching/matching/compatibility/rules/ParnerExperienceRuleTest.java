package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PartnerExperienceRuleTest {

    private PartnerExperienceRule rule;

    @BeforeEach
    void setUp() {
        rule = new PartnerExperienceRule();
    }

    @Test
    void shouldBeSatisfiedWhenMinimumExperienceIsNotSpecified() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getMinPartnerExperienceYears())
                .thenReturn(null);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldBeSatisfiedWhenMinimumExperienceIsZero() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getMinPartnerExperienceYears())
                .thenReturn(0);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldBeSatisfiedWhenOfferHasRequiredExperience() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getMinPartnerExperienceYears())
                .thenReturn(5);

        when(offer.getExperienceYears())
                .thenReturn(5);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldBeSatisfiedWhenOfferHasMoreExperienceThanRequired() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getMinPartnerExperienceYears())
                .thenReturn(5);

        when(offer.getExperienceYears())
                .thenReturn(8);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldNotBeSatisfiedWhenOfferHasTooLittleExperience() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getMinPartnerExperienceYears())
                .thenReturn(5);

        when(offer.getExperienceYears())
                .thenReturn(3);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldNotBeSatisfiedWhenOfferExperienceIsMissing() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getMinPartnerExperienceYears())
                .thenReturn(5);

        when(offer.getExperienceYears())
                .thenReturn(null);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldReturnInsufficientExperienceFailureReason() {
        assertThat(rule.failureReason())
                .isEqualTo(
                        CompatibilityFailureReason
                                .INSUFFICIENT_PARTNER_EXPERIENCE
                );
    }
}
