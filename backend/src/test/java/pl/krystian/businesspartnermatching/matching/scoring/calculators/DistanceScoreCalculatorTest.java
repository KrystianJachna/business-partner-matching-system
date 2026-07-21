package pl.krystian.businesspartnermatching.matching.scoring.calculators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.company.model.entity.Company;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingCriterion;
import pl.krystian.businesspartnermatching.matching.distance.DistanceCalculator;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class DistanceScoreCalculatorTest {

    private DistanceCalculator distanceCalculator;
    private DistanceScoreCalculator calculator;

    @BeforeEach
    void setUp() {
        distanceCalculator = mock(DistanceCalculator.class);
        calculator = new DistanceScoreCalculator(distanceCalculator);
    }

    @Test
    void shouldReturnDistanceCriterion() {
        assertThat(calculator.criterion())
                .isEqualTo(MatchingCriterion.DISTANCE);
    }

    @Test
    void shouldReturnOneWhenBothDistanceLimitsAreNotSpecified() {
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getMaxDistanceKm()).thenReturn(null);
        when(offer.getServiceRadiusKm()).thenReturn(null);

        BigDecimal score = calculator.calculateScore(need, offer);

        assertThat(score).isEqualByComparingTo(BigDecimal.ONE);
        verifyNoInteractions(distanceCalculator);
    }

    @Test
    void shouldReturnOneWhenCompaniesAreInSameLocation() {
        Company needCompany = mock(Company.class);
        Company offerCompany = mock(Company.class);

        BusinessNeed need = need(
                needCompany,
                100
        );

        BusinessOffer offer = offer(
                offerCompany,
                100
        );

        when(distanceCalculator.calculateDistanceKm(
                needCompany,
                offerCompany
        )).thenReturn(0.0);

        BigDecimal score = calculator.calculateScore(need, offer);

        assertThat(score).isEqualByComparingTo("1.0000");
    }

    @Test
    void shouldReturnPartialScoreBasedOnDistanceAndLimit() {
        Company needCompany = mock(Company.class);
        Company offerCompany = mock(Company.class);

        BusinessNeed need = need(
                needCompany,
                100
        );

        BusinessOffer offer = offer(
                offerCompany,
                100
        );

        when(distanceCalculator.calculateDistanceKm(
                needCompany,
                offerCompany
        )).thenReturn(25.0);

        BigDecimal score = calculator.calculateScore(need, offer);

        assertThat(score).isEqualByComparingTo("0.7500");
    }

    @Test
    void shouldUseMoreRestrictiveLimit() {
        Company needCompany = mock(Company.class);
        Company offerCompany = mock(Company.class);

        BusinessNeed need = need(
                needCompany,
                100
        );

        BusinessOffer offer = offer(
                offerCompany,
                50
        );

        when(distanceCalculator.calculateDistanceKm(
                needCompany,
                offerCompany
        )).thenReturn(25.0);

        BigDecimal score = calculator.calculateScore(need, offer);

        assertThat(score).isEqualByComparingTo("0.5000");
    }

    @Test
    void shouldReturnZeroWhenDistanceEqualsEffectiveLimit() {
        Company needCompany = mock(Company.class);
        Company offerCompany = mock(Company.class);

        BusinessNeed need = need(
                needCompany,
                100
        );

        BusinessOffer offer = offer(
                offerCompany,
                80
        );

        when(distanceCalculator.calculateDistanceKm(
                needCompany,
                offerCompany
        )).thenReturn(80.0);

        BigDecimal score = calculator.calculateScore(need, offer);

        assertThat(score).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldReturnZeroWhenDistanceExceedsEffectiveLimit() {
        Company needCompany = mock(Company.class);
        Company offerCompany = mock(Company.class);

        BusinessNeed need = need(
                needCompany,
                100
        );

        BusinessOffer offer = offer(
                offerCompany,
                80
        );

        when(distanceCalculator.calculateDistanceKm(
                needCompany,
                offerCompany
        )).thenReturn(90.0);

        BigDecimal score = calculator.calculateScore(need, offer);

        assertThat(score).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldUseOnlyNeedLimitWhenOfferLimitIsNotSpecified() {
        Company needCompany = mock(Company.class);
        Company offerCompany = mock(Company.class);

        BusinessNeed need = need(
                needCompany,
                100
        );

        BusinessOffer offer = offer(
                offerCompany,
                null
        );

        when(distanceCalculator.calculateDistanceKm(
                needCompany,
                offerCompany
        )).thenReturn(40.0);

        BigDecimal score = calculator.calculateScore(need, offer);

        assertThat(score).isEqualByComparingTo("0.6000");
    }

    @Test
    void shouldUseOnlyOfferLimitWhenNeedLimitIsNotSpecified() {
        Company needCompany = mock(Company.class);
        Company offerCompany = mock(Company.class);

        BusinessNeed need = need(
                needCompany,
                null
        );

        BusinessOffer offer = offer(
                offerCompany,
                100
        );

        when(distanceCalculator.calculateDistanceKm(
                needCompany,
                offerCompany
        )).thenReturn(40.0);

        BigDecimal score = calculator.calculateScore(need, offer);

        assertThat(score).isEqualByComparingTo("0.6000");
    }

    private BusinessNeed need(
            Company company,
            Integer maxDistanceKm
    ) {
        BusinessNeed need = mock(BusinessNeed.class);

        when(need.getCompany()).thenReturn(company);
        when(need.getMaxDistanceKm()).thenReturn(maxDistanceKm);

        return need;
    }

    private BusinessOffer offer(
            Company company,
            Integer serviceRadiusKm
    ) {
        BusinessOffer offer = mock(BusinessOffer.class);

        when(offer.getCompany()).thenReturn(company);
        when(offer.getServiceRadiusKm()).thenReturn(serviceRadiusKm);

        return offer;
    }
}
