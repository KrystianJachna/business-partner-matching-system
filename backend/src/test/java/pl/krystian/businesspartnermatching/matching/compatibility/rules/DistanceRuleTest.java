package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.company.model.entity.Company;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.matching.distance.DistanceCalculator;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class DistanceRuleTest {

    private DistanceCalculator distanceCalculator;
    private DistanceRule rule;

    @BeforeEach
    void setUp() {
        distanceCalculator = mock(DistanceCalculator.class);
        rule = new DistanceRule(distanceCalculator);
    }

    @Test
    void shouldBeSatisfiedWhenDistanceIsWithinBothLimits() {
        // given
        Company needCompany = mock(Company.class);
        Company offerCompany = mock(Company.class);

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getCompany()).thenReturn(needCompany);
        when(offer.getCompany()).thenReturn(offerCompany);

        when(need.getMaxDistanceKm()).thenReturn(100);
        when(offer.getServiceRadiusKm()).thenReturn(80);

        when(
                distanceCalculator.calculateDistanceKm(
                        needCompany,
                        offerCompany
                )
        ).thenReturn(70.0);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldNotBeSatisfiedWhenNeedDistanceLimitIsExceeded() {
        // given
        Company needCompany = mock(Company.class);
        Company offerCompany = mock(Company.class);

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getCompany()).thenReturn(needCompany);
        when(offer.getCompany()).thenReturn(offerCompany);

        when(need.getMaxDistanceKm()).thenReturn(50);
        when(offer.getServiceRadiusKm()).thenReturn(100);

        when(
                distanceCalculator.calculateDistanceKm(
                        needCompany,
                        offerCompany
                )
        ).thenReturn(70.0);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldNotBeSatisfiedWhenOfferServiceRadiusIsExceeded() {
        // given
        Company needCompany = mock(Company.class);
        Company offerCompany = mock(Company.class);

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getMaxDistanceKm()).thenReturn(100);
        when(offer.getServiceRadiusKm()).thenReturn(50);

        when(need.getCompany()).thenReturn(needCompany);
        when(offer.getCompany()).thenReturn(offerCompany);

        when(
                distanceCalculator.calculateDistanceKm(
                        needCompany,
                        offerCompany
                )
        ).thenReturn(70.0);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldBeSatisfiedWhenDistanceEqualsBothLimits() {
        // given
        Company needCompany = mock(Company.class);
        Company offerCompany = mock(Company.class);

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getCompany()).thenReturn(needCompany);
        when(offer.getCompany()).thenReturn(offerCompany);

        when(need.getMaxDistanceKm()).thenReturn(70);
        when(offer.getServiceRadiusKm()).thenReturn(70);

        when(
                distanceCalculator.calculateDistanceKm(
                        needCompany,
                        offerCompany
                )
        ).thenReturn(70.0);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldBeSatisfiedWhenBothLimitsAreNotSpecified() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getMaxDistanceKm()).thenReturn(null);
        when(offer.getServiceRadiusKm()).thenReturn(null);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
        verifyNoInteractions(distanceCalculator);
    }

    @Test
    void shouldUseOnlyNeedLimitWhenOfferRadiusIsNotSpecified() {
        // given
        Company needCompany = mock(Company.class);
        Company offerCompany = mock(Company.class);

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getCompany()).thenReturn(needCompany);
        when(offer.getCompany()).thenReturn(offerCompany);

        when(need.getMaxDistanceKm()).thenReturn(100);
        when(offer.getServiceRadiusKm()).thenReturn(null);

        when(
                distanceCalculator.calculateDistanceKm(
                        needCompany,
                        offerCompany
                )
        ).thenReturn(70.0);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldUseOnlyOfferRadiusWhenNeedLimitIsNotSpecified() {
        // given
        Company needCompany = mock(Company.class);
        Company offerCompany = mock(Company.class);

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getCompany()).thenReturn(needCompany);
        when(offer.getCompany()).thenReturn(offerCompany);

        when(need.getMaxDistanceKm()).thenReturn(null);
        when(offer.getServiceRadiusKm()).thenReturn(100);

        when(
                distanceCalculator.calculateDistanceKm(
                        needCompany,
                        offerCompany
                )
        ).thenReturn(70.0);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldReturnDistanceLimitExceededFailureReason() {
        assertThat(rule.failureReason())
                .isEqualTo(
                        CompatibilityFailureReason
                                .DISTANCE_LIMIT_EXCEEDED
                );
    }
}
