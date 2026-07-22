package pl.krystian.businesspartnermatching.matching.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krystian.businesspartnermatching.matching.algorithm.PopularMatchingAlgorithm;
import pl.krystian.businesspartnermatching.matching.algorithm.model.MatchingProblem;
import pl.krystian.businesspartnermatching.matching.algorithm.model.PopularMatchingResult;
import pl.krystian.businesspartnermatching.matching.preference.generation.ParticipantPreferencesGenerator;
import pl.krystian.businesspartnermatching.matching.preference.model.ParticipantPreferences;
import pl.krystian.businesspartnermatching.matching.preference.model.Preference;
import pl.krystian.businesspartnermatching.matching.preference.ranking.NeedsForOfferRankingGenerator;
import pl.krystian.businesspartnermatching.matching.preference.ranking.OffersForNeedRankingGenerator;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusinessMatchingServiceTest {

    @Mock
    private OffersForNeedRankingGenerator
            offersForNeedRankingGenerator;

    @Mock
    private NeedsForOfferRankingGenerator
            needsForOfferRankingGenerator;

    @Mock
    private ParticipantPreferencesGenerator
            participantPreferencesGenerator;

    @Mock
    private PopularMatchingAlgorithm<BusinessNeed, BusinessOffer>
            matchingAlgorithm;

    @InjectMocks
    private BusinessMatchingService businessMatchingService;

    @Mock
    private BusinessNeed need;

    @Mock
    private BusinessOffer offer;

    @Mock
    private PopularMatchingResult<BusinessNeed, BusinessOffer>
            expectedResult;

    @Test
    void shouldCreateMatchingProblemAndReturnAlgorithmResult() {
        // given
        List<BusinessNeed> needs =
                List.of(need);

        List<BusinessOffer> offers =
                List.of(offer);

        List<Preference<BusinessOffer>> offersRanking =
                List.of();

        List<Preference<BusinessNeed>> needsRanking =
                List.of();

        ParticipantPreferences<BusinessNeed, BusinessOffer> needPreferences =
                new ParticipantPreferences<>(
                        need,
                        List.of()
                );

        ParticipantPreferences<BusinessOffer, BusinessNeed> offerPreferences =
                new ParticipantPreferences<>(
                        offer,
                        List.of()
                );

        when(need.getMaxPartners())
                .thenReturn(2);

        when(offer.getMaxPartners())
                .thenReturn(3);

        when(
                offersForNeedRankingGenerator.generateRanking(
                        need,
                        offers
                )
        ).thenReturn(offersRanking);

        when(
                needsForOfferRankingGenerator.generateRanking(
                        offer,
                        needs
                )
        ).thenReturn(needsRanking);

        when(
                participantPreferencesGenerator.generate(
                        need,
                        offersRanking
                )
        ).thenReturn(needPreferences);

        when(
                participantPreferencesGenerator.generate(
                        offer,
                        needsRanking
                )
        ).thenReturn(offerPreferences);

        when(matchingAlgorithm.match(
                org.mockito.ArgumentMatchers.any()
        )).thenReturn(expectedResult);

        // when
        PopularMatchingResult<BusinessNeed, BusinessOffer> result =
                businessMatchingService.match(
                        needs,
                        offers
                );

        // then
        assertThat(result)
                .isSameAs(expectedResult);

        verify(offersForNeedRankingGenerator)
                .generateRanking(
                        need,
                        offers
                );

        verify(needsForOfferRankingGenerator)
                .generateRanking(
                        offer,
                        needs
                );

        verify(participantPreferencesGenerator)
                .generate(
                        need,
                        offersRanking
                );

        verify(participantPreferencesGenerator)
                .generate(
                        offer,
                        needsRanking
                );

        ArgumentCaptor<MatchingProblem<BusinessNeed, BusinessOffer>>
                problemCaptor =
                matchingProblemCaptor();

        verify(matchingAlgorithm)
                .match(problemCaptor.capture());

        MatchingProblem<BusinessNeed, BusinessOffer> capturedProblem =
                problemCaptor.getValue();

        assertThat(
                capturedProblem.leftPreferences()
                        .getFor(need)
        ).isSameAs(needPreferences);

        assertThat(
                capturedProblem.rightPreferences()
                        .getFor(offer)
        ).isSameAs(offerPreferences);

        assertThat(
                capturedProblem.leftCapacities()
                        .capacityOf(need)
        ).isEqualTo(2);

        assertThat(
                capturedProblem.rightCapacities()
                        .capacityOf(offer)
        ).isEqualTo(3);
    }

    @SuppressWarnings({
            "unchecked",
            "rawtypes"
    })
    private static ArgumentCaptor<
            MatchingProblem<BusinessNeed, BusinessOffer>
            > matchingProblemCaptor() {
        return ArgumentCaptor.forClass(
                (Class) MatchingProblem.class
        );
    }
}
