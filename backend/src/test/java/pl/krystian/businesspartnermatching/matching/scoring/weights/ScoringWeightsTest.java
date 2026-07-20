package pl.krystian.businesspartnermatching.matching.scoring.weights;

import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.matching.criterion.MatchingCriterion;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScoringWeightsTest {

    @Test
    void shouldCreatePreferenceProfileWhenWeightsSumToOne() {
        ScoringWeights profile = new ScoringWeights(
                Map.of(
                        MatchingCriterion.SPECIALIZATION,
                        new BigDecimal("0.60"),
                        MatchingCriterion.BUDGET,
                        new BigDecimal("0.40")
                )
        );

        assertThat(
                profile.weightOf(MatchingCriterion.SPECIALIZATION)
        ).isEqualByComparingTo("0.60");

        assertThat(
                profile.weightOf(MatchingCriterion.BUDGET)
        ).isEqualByComparingTo("0.40");
    }

    @Test
    void shouldRejectWeightsThatDoNotSumToOne() {
        Map<MatchingCriterion, BigDecimal> weights = Map.of(
                MatchingCriterion.SPECIALIZATION,
                new BigDecimal("0.50"),
                MatchingCriterion.BUDGET,
                new BigDecimal("0.30")
        );

        assertThatThrownBy(
                () -> new ScoringWeights(weights)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Criterion weights must sum to 1");
    }

    @Test
    void shouldRejectNegativeWeight() {
        Map<MatchingCriterion, BigDecimal> weights = Map.of(
                MatchingCriterion.SPECIALIZATION,
                new BigDecimal("1.10"),
                MatchingCriterion.BUDGET,
                new BigDecimal("-0.10")
        );

        assertThatThrownBy(
                () -> new ScoringWeights(weights)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Criterion weight cannot be negative");
    }

    @Test
    void shouldReturnZeroForMissingCriterion() {
        ScoringWeights profile = new ScoringWeights(
                Map.of(
                        MatchingCriterion.SPECIALIZATION,
                        BigDecimal.ONE
                )
        );

        assertThat(
                profile.weightOf(MatchingCriterion.BUDGET)
        ).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
