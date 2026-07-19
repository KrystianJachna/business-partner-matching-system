package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.company.model.entity.Company;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PartnerExperienceRuleTest {

    private PartnerExperienceRule rule;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(
                Instant.parse("2026-07-19T12:00:00Z"),
                ZoneOffset.UTC
        );

        rule = new PartnerExperienceRule(fixedClock);
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
    void shouldBeSatisfiedWhenPartnerHasRequiredExperience() {
        // given
        Company partnerCompany = mock(Company.class);
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getMinPartnerExperienceYears())
                .thenReturn(5);

        when(offer.getCompany())
                .thenReturn(partnerCompany);

        when(partnerCompany.getEstablishedAt())
                .thenReturn(LocalDate.of(2020, 7, 19));

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldNotBeSatisfiedWhenPartnerHasTooLittleExperience() {
        // given
        Company partnerCompany = mock(Company.class);
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getMinPartnerExperienceYears())
                .thenReturn(5);

        when(offer.getCompany())
                .thenReturn(partnerCompany);

        when(partnerCompany.getEstablishedAt())
                .thenReturn(LocalDate.of(2023, 7, 19));

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldNotBeSatisfiedWhenEstablishmentDateIsMissing() {
        // given
        Company partnerCompany = mock(Company.class);
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getMinPartnerExperienceYears())
                .thenReturn(5);

        when(offer.getCompany())
                .thenReturn(partnerCompany);

        when(partnerCompany.getEstablishedAt())
                .thenReturn(null);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldNotCountIncompleteYearOfExperience() {
        // given
        Company partnerCompany = mock(Company.class);
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getMinPartnerExperienceYears())
                .thenReturn(5);

        when(offer.getCompany())
                .thenReturn(partnerCompany);

        when(partnerCompany.getEstablishedAt())
                .thenReturn(LocalDate.of(2021, 7, 20));

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
