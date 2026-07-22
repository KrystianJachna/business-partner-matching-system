package pl.krystian.businesspartnermatching.matching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.krystian.businesspartnermatching.matching.algorithm.PopularMatchingAlgorithm;
import pl.krystian.businesspartnermatching.matching.algorithm.model.MatchingProblem;
import pl.krystian.businesspartnermatching.matching.algorithm.model.ParticipantCapacitySet;
import pl.krystian.businesspartnermatching.matching.algorithm.model.PopularMatchingResult;
import pl.krystian.businesspartnermatching.matching.preference.generation.ParticipantPreferencesGenerator;
import pl.krystian.businesspartnermatching.matching.preference.model.ParticipantPreferenceSet;
import pl.krystian.businesspartnermatching.matching.preference.model.ParticipantPreferences;
import pl.krystian.businesspartnermatching.matching.preference.model.Preference;
import pl.krystian.businesspartnermatching.matching.preference.ranking.NeedsForOfferRankingGenerator;
import pl.krystian.businesspartnermatching.matching.preference.ranking.OffersForNeedRankingGenerator;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.need.repository.BusinessNeedRepository;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;
import pl.krystian.businesspartnermatching.offer.repository.BusinessOfferRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BusinessMatchingService {

    private final OffersForNeedRankingGenerator
            offersForNeedRankingGenerator;

    private final NeedsForOfferRankingGenerator
            needsForOfferRankingGenerator;

    private final ParticipantPreferencesGenerator
            participantPreferencesGenerator;

    private final PopularMatchingAlgorithm<BusinessNeed, BusinessOffer>
            matchingAlgorithm;

    private final BusinessNeedRepository businessNeedRepository;

    private final BusinessOfferRepository businessOfferRepository;

    @Transactional
    public PopularMatchingResult<BusinessNeed, BusinessOffer> match() {
        List<BusinessNeed> needs =
                businessNeedRepository.findAllByActiveTrue();

        List<BusinessOffer> offers =
                businessOfferRepository.findAllByActiveTrue();

        return match(
                needs,
                offers
        );
    }

    public PopularMatchingResult<BusinessNeed, BusinessOffer> match(
            List<BusinessNeed> needs,
            List<BusinessOffer> offers
    ) {
        validateInput(needs, offers);

        MatchingProblem<BusinessNeed, BusinessOffer> problem =
                createMatchingProblem(needs, offers);

        return matchingAlgorithm.match(problem);
    }

    private MatchingProblem<BusinessNeed, BusinessOffer>
    createMatchingProblem(
            List<BusinessNeed> needs,
            List<BusinessOffer> offers
    ) {
        ParticipantPreferenceSet<BusinessNeed, BusinessOffer>
                needPreferences =
                createNeedPreferences(needs, offers);

        ParticipantPreferenceSet<BusinessOffer, BusinessNeed>
                offerPreferences =
                createOfferPreferences(needs, offers);

        ParticipantCapacitySet<BusinessNeed> needCapacities =
                createNeedCapacities(needs);

        ParticipantCapacitySet<BusinessOffer> offerCapacities =
                createOfferCapacities(offers);

        return new MatchingProblem<>(
                needPreferences,
                offerPreferences,
                needCapacities,
                offerCapacities
        );
    }

    private ParticipantPreferenceSet<BusinessNeed, BusinessOffer>
    createNeedPreferences(
            List<BusinessNeed> needs,
            List<BusinessOffer> offers
    ) {
        List<ParticipantPreferences<BusinessNeed, BusinessOffer>>
                preferences = needs.stream()
                .map(need ->
                        createPreferencesForNeed(
                                need,
                                offers
                        )
                )
                .toList();

        return ParticipantPreferenceSet.from(preferences);
    }

    private ParticipantPreferences<BusinessNeed, BusinessOffer>
    createPreferencesForNeed(
            BusinessNeed need,
            List<BusinessOffer> offers
    ) {
        List<Preference<BusinessOffer>> ranking =
                offersForNeedRankingGenerator.generateRanking(
                        need,
                        offers
                );

        return participantPreferencesGenerator.generate(
                need,
                ranking
        );
    }

    private ParticipantPreferenceSet<BusinessOffer, BusinessNeed>
    createOfferPreferences(
            List<BusinessNeed> needs,
            List<BusinessOffer> offers
    ) {
        List<ParticipantPreferences<BusinessOffer, BusinessNeed>>
                preferences = offers.stream()
                .map(offer ->
                        createPreferencesForOffer(
                                offer,
                                needs
                        )
                )
                .toList();

        return ParticipantPreferenceSet.from(preferences);
    }

    private ParticipantPreferences<BusinessOffer, BusinessNeed>
    createPreferencesForOffer(
            BusinessOffer offer,
            List<BusinessNeed> needs
    ) {
        List<Preference<BusinessNeed>> ranking =
                needsForOfferRankingGenerator.generateRanking(
                        offer,
                        needs
                );

        return participantPreferencesGenerator.generate(
                offer,
                ranking
        );
    }

    private ParticipantCapacitySet<BusinessNeed> createNeedCapacities(
            List<BusinessNeed> needs
    ) {
        Map<BusinessNeed, Integer> capacities =
                new LinkedHashMap<>();

        for (BusinessNeed need : needs) {
            capacities.put(
                    need,
                    need.getMaxPartners()
            );
        }

        return new ParticipantCapacitySet<>(capacities);
    }

    private ParticipantCapacitySet<BusinessOffer> createOfferCapacities(
            List<BusinessOffer> offers
    ) {
        Map<BusinessOffer, Integer> capacities =
                new LinkedHashMap<>();

        for (BusinessOffer offer : offers) {
            capacities.put(
                    offer,
                    offer.getMaxPartners()
            );
        }

        return new ParticipantCapacitySet<>(capacities);
    }

    private void validateInput(
            List<BusinessNeed> needs,
            List<BusinessOffer> offers
    ) {
        Objects.requireNonNull(
                needs,
                "Business needs cannot be null"
        );

        Objects.requireNonNull(
                offers,
                "Business offers cannot be null"
        );

        if (needs.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(
                    "Business needs cannot contain null"
            );
        }

        if (offers.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(
                    "Business offers cannot contain null"
            );
        }
    }
}
