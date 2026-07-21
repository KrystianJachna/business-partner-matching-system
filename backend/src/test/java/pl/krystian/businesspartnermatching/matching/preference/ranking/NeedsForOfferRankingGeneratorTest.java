package pl.krystian.businesspartnermatching.matching.preference.ranking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityChecker;
import pl.krystian.businesspartnermatching.matching.preference.model.Preference;
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
class NeedsForOfferRankingGeneratorTest {

    @Mock
    private CompatibilityChecker compatibilityChecker;

    @Mock
    private MatchingScoreCalculator matchingScoreCalculator;

    @Mock
    private BusinessOffer offer;

    @Mock
    private BusinessNeed firstNeed;

    @Mock
    private BusinessNeed secondNeed;

    @Mock
    private BusinessNeed thirdNeed;

    private NeedsForOfferRankingGenerator rankingGenerator;

    @BeforeEach
    void setUp() {
        rankingGenerator =
                new NeedsForOfferRankingGenerator(
                        compatibilityChecker,
                        matchingScoreCalculator
                );
    }

    @Test
    void shouldSortCompatibleNeedsByScoreDescending() {
        // given
        when(compatibilityChecker.isCompatible(firstNeed, offer))
                .thenReturn(true);

        when(compatibilityChecker.isCompatible(secondNeed, offer))
                .thenReturn(true);

        when(compatibilityChecker.isCompatible(thirdNeed, offer))
                .thenReturn(true);

        when(matchingScoreCalculator.calculateForOffer(offer, firstNeed))
                .thenReturn(matchingScore("0.8000"));

        when(matchingScoreCalculator.calculateForOffer(offer, secondNeed))
                .thenReturn(matchingScore("0.5000"));

        when(matchingScoreCalculator.calculateForOffer(offer, thirdNeed))
                .thenReturn(matchingScore("0.9500"));

        // when
        List<Preference<BusinessNeed>> ranking =
                rankingGenerator.generateRanking(
                        offer,
                        List.of(
                                firstNeed,
                                secondNeed,
                                thirdNeed
                        )
                );

        // then
        assertThat(ranking)
                .extracting(Preference::candidate)
                .containsExactly(
                        thirdNeed,
                        firstNeed,
                        secondNeed
                );

        assertThat(ranking)
                .extracting(Preference::score)
                .containsExactly(
                        new BigDecimal("0.9500"),
                        new BigDecimal("0.8000"),
                        new BigDecimal("0.5000")
                );
    }

    @Test
    void shouldExcludeIncompatibleNeedFromRanking() {
        // given
        when(compatibilityChecker.isCompatible(firstNeed, offer))
                .thenReturn(true);

        when(compatibilityChecker.isCompatible(secondNeed, offer))
                .thenReturn(false);

        when(matchingScoreCalculator.calculateForOffer(offer, firstNeed))
                .thenReturn(matchingScore("0.8000"));

        // when
        List<Preference<BusinessNeed>> ranking =
                rankingGenerator.generateRanking(
                        offer,
                        List.of(
                                firstNeed,
                                secondNeed
                        )
                );

        // then
        assertThat(ranking)
                .hasSize(1);

        assertThat(ranking.getFirst().candidate())
                .isSameAs(firstNeed);

        assertThat(ranking.getFirst().score())
                .isEqualByComparingTo("0.8000");

        verify(
                matchingScoreCalculator,
                never()
        ).calculateForOffer(
                offer,
                secondNeed
        );
    }

    @Test
    void shouldReturnEmptyRankingWhenNoNeedIsCompatible() {
        // given
        when(compatibilityChecker.isCompatible(firstNeed, offer))
                .thenReturn(false);

        when(compatibilityChecker.isCompatible(secondNeed, offer))
                .thenReturn(false);

        // when
        List<Preference<BusinessNeed>> ranking =
                rankingGenerator.generateRanking(
                        offer,
                        List.of(
                                firstNeed,
                                secondNeed
                        )
                );

        // then
        assertThat(ranking)
                .isEmpty();

        verify(
                matchingScoreCalculator,
                never()
        ).calculateForOffer(
                offer,
                firstNeed
        );

        verify(
                matchingScoreCalculator,
                never()
        ).calculateForOffer(
                offer,
                secondNeed
        );
    }

    private MatchingScore matchingScore(String totalScore) {
        return new MatchingScore(
                new BigDecimal(totalScore),
                List.of()
        );
    }
}
