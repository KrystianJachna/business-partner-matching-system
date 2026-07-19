package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.common.time.DateRange;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DateRangeOverlapRuleTest {

    private DateRangeOverlapRule rule;

    @BeforeEach
    void setUp() {
        rule = new DateRangeOverlapRule();
    }

    @Test
    void shouldBeSatisfiedWhenDateRangesOverlap() {
        // given
        DateRange requiredPeriod = dateRange(
                "2026-08-01",
                "2026-08-20"
        );

        DateRange availabilityPeriod = dateRange(
                "2026-08-15",
                "2026-09-01"
        );

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getRequiredPeriod())
                .thenReturn(requiredPeriod);

        when(offer.getAvailabilityPeriod())
                .thenReturn(availabilityPeriod);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldBeSatisfiedWhenDateRangesTouchAtBoundary() {
        // given
        DateRange requiredPeriod = dateRange(
                "2026-08-01",
                "2026-08-20"
        );

        DateRange availabilityPeriod = dateRange(
                "2026-08-20",
                "2026-09-01"
        );

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getRequiredPeriod())
                .thenReturn(requiredPeriod);

        when(offer.getAvailabilityPeriod())
                .thenReturn(availabilityPeriod);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldNotBeSatisfiedWhenDateRangesDoNotOverlap() {
        // given
        DateRange requiredPeriod = dateRange(
                "2026-08-01",
                "2026-08-20"
        );

        DateRange availabilityPeriod = dateRange(
                "2026-08-21",
                "2026-09-01"
        );

        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getRequiredPeriod())
                .thenReturn(requiredPeriod);

        when(offer.getAvailabilityPeriod())
                .thenReturn(availabilityPeriod);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isFalse();
    }

    @Test
    void shouldBeSatisfiedWhenRequiredPeriodIsNotSpecified() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getRequiredPeriod()).thenReturn(null);

        when(offer.getAvailabilityPeriod())
                .thenReturn(
                        dateRange(
                                "2026-08-01",
                                "2026-08-20"
                        )
                );

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldBeSatisfiedWhenAvailabilityPeriodIsNotSpecified() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        when(need.getRequiredPeriod())
                .thenReturn(
                        dateRange(
                                "2026-08-01",
                                "2026-08-20"
                        )
                );

        when(offer.getAvailabilityPeriod()).thenReturn(null);

        // when
        boolean satisfied = rule.isSatisfied(need, offer);

        // then
        assertThat(satisfied).isTrue();
    }

    @Test
    void shouldReturnNoDateOverlapFailureReason() {
        assertThat(rule.failureReason())
                .isEqualTo(
                        CompatibilityFailureReason.NO_DATE_OVERLAP
                );
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
