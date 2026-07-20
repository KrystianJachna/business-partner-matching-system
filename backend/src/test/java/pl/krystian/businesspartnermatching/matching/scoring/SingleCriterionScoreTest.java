package pl.krystian.businesspartnermatching.matching.scoring;

import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.matching.criterion.MatchingCriterion;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SingleCriterionScoreTest {

    @Test
    void shouldCreateCriterionScoreForValueBetweenZeroAndOne() {
        SingleCriterionScore score = new SingleCriterionScore(
                MatchingCriterion.BUDGET,
                new BigDecimal("0.75")
        );

        assertThat(score.criterion())
                .isEqualTo(MatchingCriterion.BUDGET);

        assertThat(score.value())
                .isEqualByComparingTo("0.75");
    }

    @Test
    void shouldAllowZero() {
        SingleCriterionScore score = new SingleCriterionScore(
                MatchingCriterion.DATE,
                BigDecimal.ZERO
        );

        assertThat(score.value())
                .isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldAllowOne() {
        SingleCriterionScore score = new SingleCriterionScore(
                MatchingCriterion.SPECIALIZATION,
                BigDecimal.ONE
        );

        assertThat(score.value())
                .isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void shouldRejectValueLowerThanZero() {
        assertThatThrownBy(
                () -> new SingleCriterionScore(
                        MatchingCriterion.DISTANCE,
                        new BigDecimal("-0.01")
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Criterion score must be between 0 and 1"
                );
    }

    @Test
    void shouldRejectValueGreaterThanOne() {
        assertThatThrownBy(
                () -> new SingleCriterionScore(
                        MatchingCriterion.EXPERIENCE,
                        new BigDecimal("1.01")
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Criterion score must be between 0 and 1"
                );
    }

    @Test
    void shouldRejectNullCriterion() {
        assertThatThrownBy(
                () -> new SingleCriterionScore(
                        null,
                        new BigDecimal("0.50")
                )
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage(
                        "Matching criterion cannot be null"
                );
    }

    @Test
    void shouldRejectNullValue() {
        assertThatThrownBy(
                () -> new SingleCriterionScore(
                        MatchingCriterion.BUDGET,
                        null
                )
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage(
                        "Criterion score value cannot be null"
                );
    }
}
