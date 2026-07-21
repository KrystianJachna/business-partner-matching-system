package pl.krystian.businesspartnermatching.matching.preference.ranking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityChecker;
import pl.krystian.businesspartnermatching.matching.preference.model.Preference;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingScore;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingScoreCalculator;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OfferPreferenceRankingGenerator
        implements PreferenceRankingGenerator<BusinessNeed, BusinessOffer> {

    private final CompatibilityChecker compatibilityChecker;
    private final MatchingScoreCalculator matchingScoreCalculator;

    @Override
    public List<Preference<BusinessOffer>> generateRanking(
            BusinessNeed need,
            List<BusinessOffer> offers
    ) {
        Objects.requireNonNull(
                need,
                "Business need cannot be null"
        );

        Objects.requireNonNull(
                offers,
                "Business offers cannot be null"
        );

        if (offers.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Business offers list contains null elements");
        }

        return offers.stream()
                .filter(offer ->
                        compatibilityChecker.isCompatible(
                                need,
                                offer
                        )
                )
                .map(offer ->
                        toPreference(
                                need,
                                offer
                        )
                )
                .sorted(
                        Comparator
                                .comparing(
                                        (Preference<BusinessOffer> preference) ->
                                                preference.score()
                                )
                                .reversed()
                                .thenComparing(
                                        preference -> preference.candidate().getId()
                                )
                )
                .toList();
    }

    private Preference<BusinessOffer> toPreference(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        MatchingScore matchingScore =
                matchingScoreCalculator.calculateForNeed(
                        need,
                        offer
                );

        return new Preference<>(
                offer,
                matchingScore.totalScore()
        );
    }
}
