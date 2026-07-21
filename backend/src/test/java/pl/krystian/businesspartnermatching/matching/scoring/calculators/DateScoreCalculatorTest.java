package pl.krystian.businesspartnermatching.matching.scoring.calculators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.common.time.DateRange;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingCriterion;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DateScoreCalculatorTest {

    private DateScoreCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new DateScoreCalculator();
    }

    @Test
    void shouldReturnDateCriterion() {
        assertThat(calculator.criterion())
                .isEqualTo(MatchingCriterion.DATE);
    }

    @Test
    void shouldReturnOneWhenAvailabilityFullyCoversRequiredPeriod() {
        // given
        BusinessNeed need = needWithRequiredPeriod(
                dateRange("2026-08-01", "2026-08-20")
        );

        BusinessOffer offer = offerWithAvailabilityPeriod(
                dateRange("2026-07-20", "2026-08-31")
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo("1.0000");
    }

    @Test
    void shouldReturnPartialScoreWhenPeriodsPartiallyOverlap() {
        // given
        BusinessNeed need = needWithRequiredPeriod(
                dateRange("2026-08-01", "2026-08-20")
        );

        BusinessOffer offer = offerWithAvailabilityPeriod(
                dateRange("2026-08-10", "2026-08-20")
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo("0.5500");
    }

    @Test
    void shouldReturnScoreForSingleOverlappingDay() {
        // given
        BusinessNeed need = needWithRequiredPeriod(
                dateRange("2026-08-01", "2026-08-20")
        );

        BusinessOffer offer = offerWithAvailabilityPeriod(
                dateRange("2026-08-20", "2026-09-10")
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo("0.0500");
    }

    @Test
    void shouldReturnZeroWhenPeriodsDoNotOverlap() {
        // given
        BusinessNeed need = needWithRequiredPeriod(
                dateRange("2026-08-01", "2026-08-20")
        );

        BusinessOffer offer = offerWithAvailabilityPeriod(
                dateRange("2026-08-21", "2026-09-10")
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldReturnOneWhenRequiredPeriodIsNotSpecified() {
        // given
        BusinessNeed need = needWithRequiredPeriod(null);

        BusinessOffer offer = offerWithAvailabilityPeriod(
                dateRange("2026-08-01", "2026-08-20")
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void shouldReturnOneWhenAvailabilityPeriodIsNotSpecified() {
        // given
        BusinessNeed need = needWithRequiredPeriod(
                dateRange("2026-08-01", "2026-08-20")
        );

        BusinessOffer offer = offerWithAvailabilityPeriod(null);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void shouldReturnOneForMatchingSingleDayPeriods() {
        // given
        BusinessNeed need = needWithRequiredPeriod(
                dateRange("2026-08-20", "2026-08-20")
        );

        BusinessOffer offer = offerWithAvailabilityPeriod(
                dateRange("2026-08-20", "2026-08-20")
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo("1.0000");
    }

    private BusinessNeed needWithRequiredPeriod(
            DateRange requiredPeriod
    ) {
        BusinessNeed need = mock(BusinessNeed.class);

        when(need.getRequiredPeriod())
                .thenReturn(requiredPeriod);

        return need;
    }

    private BusinessOffer offerWithAvailabilityPeriod(
            DateRange availabilityPeriod
    ) {
        BusinessOffer offer = mock(BusinessOffer.class);

        when(offer.getAvailabilityPeriod())
                .thenReturn(availabilityPeriod);

        return offer;
    }

    private DateRange dateRange(
            String from,
            String until
    ) {
        return new DateRange(
                LocalDate.parse(from),
                LocalDate.parse(until)
        );
    }
}
