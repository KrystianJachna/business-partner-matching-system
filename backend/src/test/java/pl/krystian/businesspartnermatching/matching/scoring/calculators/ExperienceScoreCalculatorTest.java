package pl.krystian.businesspartnermatching.matching.scoring.calculators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingCriterion;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExperienceScoreCalculatorTest {

    private ExperienceScoreCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new ExperienceScoreCalculator();
    }

    @Test
    void shouldReturnExperienceCriterion() {
        assertThat(calculator.criterion())
                .isEqualTo(MatchingCriterion.EXPERIENCE);
    }

    @Test
    void shouldReturnNoScoreWhenMinimumExperienceIsNotSpecified() {
        // given
        BusinessNeed need = needWithMinimumExperience(null);
        BusinessOffer offer = mock(BusinessOffer.class);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isNull();
    }

    @Test
    void shouldReturnNoScoreWhenMinimumExperienceIsZero() {
        // given
        BusinessNeed need = needWithMinimumExperience(0);
        BusinessOffer offer = mock(BusinessOffer.class);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isNull();
    }

    @Test
    void shouldReturnZeroWhenOfferExperienceIsMissing() {
        // given
        BusinessNeed need = needWithMinimumExperience(5);
        BusinessOffer offer = offerWithExperience(null);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score)
                .isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldReturnZeroWhenOfferHasLessExperienceThanRequired() {
        // given
        BusinessNeed need = needWithMinimumExperience(5);
        BusinessOffer offer = offerWithExperience(3);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score)
                .isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldReturnHalfWhenOfferHasExactlyRequiredExperience() {
        // given
        BusinessNeed need = needWithMinimumExperience(5);
        BusinessOffer offer = offerWithExperience(5);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score)
                .isEqualByComparingTo("0.5000");
    }

    @Test
    void shouldReturnPartialScoreWhenOfferHasMoreThanRequiredExperience() {
        // given
        BusinessNeed need = needWithMinimumExperience(5);
        BusinessOffer offer = offerWithExperience(7);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score)
                .isEqualByComparingTo("0.7000");
    }

    @Test
    void shouldReturnOneWhenOfferHasTwiceRequiredExperience() {
        // given
        BusinessNeed need = needWithMinimumExperience(5);
        BusinessOffer offer = offerWithExperience(10);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score)
                .isEqualByComparingTo("1.0000");
    }

    @Test
    void shouldCapScoreAtOneWhenOfferHasMuchMoreExperience() {
        // given
        BusinessNeed need = needWithMinimumExperience(5);
        BusinessOffer offer = offerWithExperience(20);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score)
                .isEqualByComparingTo("1.0000");
    }

    private BusinessNeed needWithMinimumExperience(
            Integer minimumExperienceYears
    ) {
        BusinessNeed need = mock(BusinessNeed.class);

        when(need.getMinPartnerExperienceYears())
                .thenReturn(minimumExperienceYears);

        return need;
    }

    private BusinessOffer offerWithExperience(
            Integer experienceYears
    ) {
        BusinessOffer offer = mock(BusinessOffer.class);

        when(offer.getExperienceYears())
                .thenReturn(experienceYears);

        return offer;
    }
}
