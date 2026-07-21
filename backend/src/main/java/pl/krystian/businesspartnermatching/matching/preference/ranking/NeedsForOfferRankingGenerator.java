package pl.krystian.businesspartnermatching.matching.preference.ranking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityChecker;
import pl.krystian.businesspartnermatching.matching.preference.model.Preference;
import pl.krystian.businesspartnermatching.matching.scoring.model.MatchingScore;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingScoreCalculator;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class NeedsForOfferRankingGenerator
        implements PreferenceRankingGenerator<BusinessOffer, BusinessNeed> {

    private final CompatibilityChecker compatibilityChecker;
    private final MatchingScoreCalculator matchingScoreCalculator;

    @Override
    public List<Preference<BusinessNeed>> generateRanking(
            BusinessOffer offer,
            List<BusinessNeed> needs
    ) {
        Objects.requireNonNull(
                offer,
                "Business offer cannot be null"
        );

        Objects.requireNonNull(
                needs,
                "Business needs cannot be null"
        );

        if (needs.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Business needs list contains null elements");
        }

        return needs.stream()
                .filter(Objects::nonNull)
                .filter(need ->
                        compatibilityChecker.isCompatible(
                                need,
                                offer
                        )
                )
                .map(need ->
                        toPreference(
                                offer,
                                need
                        )
                )
                .sorted(
                        Comparator
                                .comparing(
                                        (Preference<BusinessNeed> preference) ->
                                                preference.score()
                                )
                                .reversed()
                                .thenComparing(
                                        preference -> preference.candidate().getId()
                                )
                )
                .toList();
    }

    private Preference<BusinessNeed> toPreference(
            BusinessOffer offer,
            BusinessNeed need
    ) {
        MatchingScore matchingScore =
                matchingScoreCalculator.calculateForOffer(
                        offer,
                        need
                );

        return new Preference<>(
                need,
                matchingScore.totalScore()
        );
    }
}
