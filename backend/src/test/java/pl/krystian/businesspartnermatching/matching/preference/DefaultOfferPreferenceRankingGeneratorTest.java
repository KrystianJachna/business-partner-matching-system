package pl.krystian.businesspartnermatching.matching.preference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityChecker;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingScore;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingScoreCalculator;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultOfferPreferenceRankingGeneratorTest {

    @Mock
    private CompatibilityChecker compatibilityChecker;

    @Mock
    private MatchingScoreCalculator matchingScoreCalculator;

    @Mock
    private BusinessNeed need;

    @Mock
    private BusinessOffer firstOffer;

    @Mock
    private BusinessOffer secondOffer;

    @Mock
    private BusinessOffer thirdOffer;

    private DefaultOfferPreferenceRankingGenerator rankingGenerator;

    @BeforeEach
    void setUp() {
        rankingGenerator =
                new DefaultOfferPreferenceRankingGenerator(
                        compatibilityChecker,
                        matchingScoreCalculator
                );
    }

    @Test
    void shouldSortCompatibleOffersByScoreDescending() {
        // given
        when(compatibilityChecker.isCompatible(need, firstOffer))
                .thenReturn(true);

        when(compatibilityChecker.isCompatible(need, secondOffer))
                .thenReturn(true);

        when(compatibilityChecker.isCompatible(need, thirdOffer))
                .thenReturn(true);

        when(matchingScoreCalculator.calculateForNeed(need, firstOffer))
                .thenReturn(matchingScore("0.8000"));

        when(matchingScoreCalculator.calculateForNeed(need, secondOffer))
                .thenReturn(matchingScore("0.5000"));

        when(matchingScoreCalculator.calculateForNeed(need, thirdOffer))
                .thenReturn(matchingScore("0.9500"));

        // when
        List<ScoredOffer> ranking = rankingGenerator.generateRanking(
                need,
                List.of(
                        firstOffer,
                        secondOffer,
                        thirdOffer
                )
        );

        // then
        assertThat(ranking)
                .extracting(ScoredOffer::offer)
                .containsExactly(
                        thirdOffer,
                        firstOffer,
                        secondOffer
                );

        assertThat(ranking)
                .extracting(ScoredOffer::score)
                .containsExactly(
                        new BigDecimal("0.9500"),
                        new BigDecimal("0.8000"),
                        new BigDecimal("0.5000")
                );
    }

    @Test
    void shouldExcludeIncompatibleOfferFromRanking() {
        // given
        when(compatibilityChecker.isCompatible(need, firstOffer))
                .thenReturn(true);

        when(compatibilityChecker.isCompatible(need, secondOffer))
                .thenReturn(false);

        when(matchingScoreCalculator.calculateForNeed(need, firstOffer))
                .thenReturn(matchingScore("0.8000"));

        // when
        List<ScoredOffer> ranking = rankingGenerator.generateRanking(
                need,
                List.of(
                        firstOffer,
                        secondOffer
                )
        );

        // then
        assertThat(ranking)
                .hasSize(1);

        assertThat(ranking.getFirst().offer())
                .isSameAs(firstOffer);

        assertThat(ranking.getFirst().score())
                .isEqualByComparingTo("0.8000");

        verify(
                matchingScoreCalculator,
                never()
        ).calculateForNeed(
                need,
                secondOffer
        );
    }

    @Test
    void shouldReturnEmptyRankingWhenNoOfferIsCompatible() {
        // given
        when(compatibilityChecker.isCompatible(need, firstOffer))
                .thenReturn(false);

        when(compatibilityChecker.isCompatible(need, secondOffer))
                .thenReturn(false);

        // when
        List<ScoredOffer> ranking = rankingGenerator.generateRanking(
                need,
                List.of(
                        firstOffer,
                        secondOffer
                )
        );

        // then
        assertThat(ranking)
                .isEmpty();

        verify(
                matchingScoreCalculator,
                never()
        ).calculateForNeed(
                need,
                firstOffer
        );

        verify(
                matchingScoreCalculator,
                never()
        ).calculateForNeed(
                need,
                secondOffer
        );
    }

    private MatchingScore matchingScore(String totalScore) {
        return new MatchingScore(
                new BigDecimal(totalScore),
                List.of()
        );
    }
}
