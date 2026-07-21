package pl.krystian.businesspartnermatching.matching.scoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityChecker;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityResult;
import pl.krystian.businesspartnermatching.matching.scoring.exception.IncompatiblePairException;
import pl.krystian.businesspartnermatching.matching.scoring.model.MatchingScore;
import pl.krystian.businesspartnermatching.matching.scoring.model.SingleCriterionScore;
import pl.krystian.businesspartnermatching.matching.scoring.weights.ScoringWeights;
import pl.krystian.businesspartnermatching.matching.scoring.weights.ScoringWeightsProvider;
import pl.krystian.businesspartnermatching.matching.scoring.calculators.CriterionScoreCalculator;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class MatchingScoreCalculatorTest {

    private CompatibilityChecker compatibilityChecker;
    private CriterionScoreCalculator specializationCalculator;
    private CriterionScoreCalculator budgetCalculator;
    private CriterionScoreCalculator dateCalculator;
    private ScoringWeightsProvider scoringWeightsProvider;
    private MatchingScoreCalculator matchingScoreCalculator;

    @BeforeEach
    void setUp() {
        compatibilityChecker = mock(CompatibilityChecker.class);

        specializationCalculator =
                mock(CriterionScoreCalculator.class);

        budgetCalculator =
                mock(CriterionScoreCalculator.class);

        dateCalculator =
                mock(CriterionScoreCalculator.class);

        scoringWeightsProvider =
                mock(ScoringWeightsProvider.class);

        matchingScoreCalculator = new MatchingScoreCalculator(
                compatibilityChecker,
                List.of(
                        specializationCalculator,
                        budgetCalculator,
                        dateCalculator
                ),
                scoringWeightsProvider
        );
    }

    @Test
    void shouldCalculateWeightedScoreForNeed() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        configureCompatiblePair(need, offer);

        configureCalculator(
                specializationCalculator,
                MatchingCriterion.SPECIALIZATION,
                new BigDecimal("0.80"),
                need,
                offer
        );

        configureCalculator(
                budgetCalculator,
                MatchingCriterion.BUDGET,
                new BigDecimal("0.60"),
                need,
                offer
        );

        configureCalculator(
                dateCalculator,
                MatchingCriterion.DATE,
                BigDecimal.ONE,
                need,
                offer
        );

        ScoringWeights profile = new ScoringWeights(
                Map.of(
                        MatchingCriterion.SPECIALIZATION,
                        new BigDecimal("0.50"),
                        MatchingCriterion.BUDGET,
                        new BigDecimal("0.30"),
                        MatchingCriterion.DATE,
                        new BigDecimal("0.20")
                )
        );

        when(scoringWeightsProvider.forNeed(need))
                .thenReturn(profile);

        // when
        MatchingScore result =
                matchingScoreCalculator.calculateForNeed(
                        need,
                        offer
                );

        // then
        assertThat(result.totalScore())
                .isEqualByComparingTo("0.7800");

        assertThat(result.singleCriterionScores())
                .containsExactly(
                        new SingleCriterionScore(
                                MatchingCriterion.SPECIALIZATION,
                                new BigDecimal("0.80")
                        ),
                        new SingleCriterionScore(
                                MatchingCriterion.BUDGET,
                                new BigDecimal("0.60")
                        ),
                        new SingleCriterionScore(
                                MatchingCriterion.DATE,
                                BigDecimal.ONE
                        )
                );
    }

    @Test
    void shouldIgnoreCriterionWithZeroWeight() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        configureCompatiblePair(need, offer);

        configureCalculator(
                specializationCalculator,
                MatchingCriterion.SPECIALIZATION,
                new BigDecimal("0.80"),
                need,
                offer
        );

        configureCalculator(
                budgetCalculator,
                MatchingCriterion.BUDGET,
                BigDecimal.ZERO,
                need,
                offer
        );

        configureCalculator(
                dateCalculator,
                MatchingCriterion.DATE,
                BigDecimal.ZERO,
                need,
                offer
        );

        ScoringWeights profile = new ScoringWeights(
                Map.of(
                        MatchingCriterion.SPECIALIZATION,
                        BigDecimal.ONE
                )
        );

        when(scoringWeightsProvider.forNeed(need))
                .thenReturn(profile);

        // when
        MatchingScore result =
                matchingScoreCalculator.calculateForNeed(
                        need,
                        offer
                );

        // then
        assertThat(result.totalScore())
                .isEqualByComparingTo("0.8000");

        assertThat(result.singleCriterionScores())
                .hasSize(3);
    }

    @Test
    void shouldReturnZeroWhenAllCriterionScoresAreZero() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        configureCompatiblePair(need, offer);

        configureCalculator(
                specializationCalculator,
                MatchingCriterion.SPECIALIZATION,
                BigDecimal.ZERO,
                need,
                offer
        );

        configureCalculator(
                budgetCalculator,
                MatchingCriterion.BUDGET,
                BigDecimal.ZERO,
                need,
                offer
        );

        configureCalculator(
                dateCalculator,
                MatchingCriterion.DATE,
                BigDecimal.ZERO,
                need,
                offer
        );

        ScoringWeights profile = new ScoringWeights(
                Map.of(
                        MatchingCriterion.SPECIALIZATION,
                        new BigDecimal("0.50"),
                        MatchingCriterion.BUDGET,
                        new BigDecimal("0.30"),
                        MatchingCriterion.DATE,
                        new BigDecimal("0.20")
                )
        );

        when(scoringWeightsProvider.forNeed(need))
                .thenReturn(profile);

        // when
        MatchingScore result =
                matchingScoreCalculator.calculateForNeed(
                        need,
                        offer
                );

        // then
        assertThat(result.totalScore())
                .isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldUsePreferenceProfileAssignedToNeed() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        configureCompatiblePair(need, offer);

        configureCalculator(
                specializationCalculator,
                MatchingCriterion.SPECIALIZATION,
                BigDecimal.ONE,
                need,
                offer
        );

        configureCalculator(
                budgetCalculator,
                MatchingCriterion.BUDGET,
                BigDecimal.ZERO,
                need,
                offer
        );

        configureCalculator(
                dateCalculator,
                MatchingCriterion.DATE,
                BigDecimal.ZERO,
                need,
                offer
        );

        ScoringWeights profile = new ScoringWeights(
                Map.of(
                        MatchingCriterion.SPECIALIZATION,
                        BigDecimal.ONE
                )
        );

        when(scoringWeightsProvider.forNeed(need))
                .thenReturn(profile);

        // when
        matchingScoreCalculator.calculateForNeed(need, offer);

        // then
        verify(scoringWeightsProvider).forNeed(need);
    }

    @Test
    void shouldRejectIncompatiblePair() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        List<CompatibilityFailureReason> failureReasons = List.of(
                CompatibilityFailureReason.SAME_COMPANY,
                CompatibilityFailureReason.NO_BUDGET_OVERLAP
        );

        when(compatibilityChecker.check(need, offer))
                .thenReturn(
                        new CompatibilityResult(failureReasons)
                );

        // when / then
        assertThatThrownBy(
                () -> matchingScoreCalculator.calculateForNeed(
                        need,
                        offer
                )
        )
                .isInstanceOf(IncompatiblePairException.class)
                .satisfies(exception -> {
                    IncompatiblePairException incompatibleException =
                            (IncompatiblePairException) exception;

                    assertThat(
                            incompatibleException.getFailureReasons()
                    ).containsExactlyElementsOf(failureReasons);
                });

        verifyNoInteractions(
                specializationCalculator,
                budgetCalculator,
                dateCalculator,
                scoringWeightsProvider
        );
    }

    @Test
    void shouldRejectNullNeed() {
        // given
        BusinessOffer offer = mock(BusinessOffer.class);

        // when / then
        assertThatThrownBy(
                () -> matchingScoreCalculator.calculateForNeed(
                        null,
                        offer
                )
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Business need cannot be null");

        verifyNoInteractions(compatibilityChecker);
    }

    @Test
    void shouldRejectNullOffer() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);

        // when / then
        assertThatThrownBy(
                () -> matchingScoreCalculator.calculateForNeed(
                        need,
                        null
                )
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Business offer cannot be null");

        verifyNoInteractions(compatibilityChecker);
    }

    private void configureCompatiblePair(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        when(compatibilityChecker.check(need, offer))
                .thenReturn(
                        new CompatibilityResult(List.of())
                );
    }

    private void configureCalculator(
            CriterionScoreCalculator calculator,
            MatchingCriterion criterion,
            BigDecimal score,
            BusinessNeed need,
            BusinessOffer offer
    ) {
        when(calculator.criterion())
                .thenReturn(criterion);

        when(calculator.calculateScore(need, offer))
                .thenReturn(score);
    }
}
