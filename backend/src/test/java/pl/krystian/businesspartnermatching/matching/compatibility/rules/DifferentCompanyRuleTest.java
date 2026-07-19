package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.company.model.entity.Company;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DifferentCompanyRuleTest {

    private DifferentCompanyRule rule;

    @BeforeEach
    void setUp() {
        rule = new DifferentCompanyRule();
    }

    @Test
    void shouldBeSatisfiedWhenCompaniesAreDifferent() {
        // given
        Company needCompany = mock(Company.class);
        Company offerCompany = mock(Company.class);

        when(needCompany.getId()).thenReturn(1L);
        when(offerCompany.getId()).thenReturn(2L);

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getCompany()).thenReturn(needCompany);
        when(offer.getCompany()).thenReturn(offerCompany);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldNotBeSatisfiedWhenCompaniesHaveSameId() {
        // given
        Company needCompany = mock(Company.class);
        Company offerCompany = mock(Company.class);

        when(needCompany.getId()).thenReturn(1L);
        when(offerCompany.getId()).thenReturn(1L);

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getCompany()).thenReturn(needCompany);
        when(offer.getCompany()).thenReturn(offerCompany);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldNotBeSatisfiedWhenSameCompanyObjectIsUsed() {
        // given
        Company company = mock(Company.class);

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getCompany()).thenReturn(company);
        when(offer.getCompany()).thenReturn(company);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldReturnSameCompanyFailureReason() {
        assertThat(rule.failureReason())
                .isEqualTo(
                        CompatibilityFailureReason.SAME_COMPANY
                );
    }
}
