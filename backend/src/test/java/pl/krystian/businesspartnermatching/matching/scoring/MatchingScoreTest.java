package pl.krystian.businesspartnermatching.matching.scoring;

import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.matching.criterion.MatchingCriterion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MatchingScoreTest {

    @Test
    void shouldCreateMatchingScoreForValueBetweenZeroAndOne() {
        // given
        List<SingleCriterionScore> singleCriterionScores = List.of(
                new SingleCriterionScore(
                        MatchingCriterion.SPECIALIZATION,
                        new BigDecimal("0.7500")
                ),
                new SingleCriterionScore(
                        MatchingCriterion.BUDGET,
                        new BigDecimal("0.9000")
                )
        );

        // when
        MatchingScore matchingScore = new MatchingScore(
                new BigDecimal("0.8250"),
                singleCriterionScores
        );

        // then
        assertThat(matchingScore.totalScore())
                .isEqualByComparingTo("0.8250");

        assertThat(matchingScore.singleCriterionScores())
                .containsExactlyElementsOf(singleCriterionScores);
    }

    @Test
    void shouldAllowZeroTotalScore() {
        MatchingScore matchingScore = new MatchingScore(
                BigDecimal.ZERO,
                List.of()
        );

        assertThat(matchingScore.totalScore())
                .isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldAllowOneTotalScore() {
        MatchingScore matchingScore = new MatchingScore(
                BigDecimal.ONE,
                List.of()
        );

        assertThat(matchingScore.totalScore())
                .isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void shouldRejectTotalScoreLowerThanZero() {
        assertThatThrownBy(
                () -> new MatchingScore(
                        new BigDecimal("-0.01"),
                        List.of()
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Total matching score must be between 0 and 1"
                );
    }

    @Test
    void shouldRejectTotalScoreGreaterThanOne() {
        assertThatThrownBy(
                () -> new MatchingScore(
                        new BigDecimal("1.01"),
                        List.of()
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Total matching score must be between 0 and 1"
                );
    }

    @Test
    void shouldRejectNullTotalScore() {
        assertThatThrownBy(
                () -> new MatchingScore(
                        null,
                        List.of()
                )
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage(
                        "Total matching score cannot be null"
                );
    }

    @Test
    void shouldRejectNullCriterionScores() {
        assertThatThrownBy(
                () -> new MatchingScore(
                        new BigDecimal("0.50"),
                        null
                )
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage(
                        "Criterion scores cannot be null"
                );
    }

    @Test
    void shouldMakeCriterionScoresImmutable() {
        // given
        List<SingleCriterionScore> originalScores = new ArrayList<>();

        originalScores.add(
                new SingleCriterionScore(
                        MatchingCriterion.BUDGET,
                        new BigDecimal("0.8000")
                )
        );

        MatchingScore matchingScore = new MatchingScore(
                new BigDecimal("0.8000"),
                originalScores
        );

        // when
        originalScores.clear();

        // then
        assertThat(matchingScore.singleCriterionScores())
                .hasSize(1);

        assertThatThrownBy(
                () -> matchingScore.singleCriterionScores().add(
                        new SingleCriterionScore(
                                MatchingCriterion.DATE,
                                BigDecimal.ONE
                        )
                )
        )
                .isInstanceOf(
                        UnsupportedOperationException.class
                );
    }
}
