package pl.krystian.businesspartnermatching.matching.preference;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityChecker;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingScore;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingScoreCalculator;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DefaultNeedPreferenceRankingGenerator
        implements NeedPreferenceRankingGenerator {

    private final CompatibilityChecker compatibilityChecker;
    private final MatchingScoreCalculator matchingScoreCalculator;

    @Override
    public List<ScoredNeed> generateRanking(
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

        return needs.stream()
                .filter(Objects::nonNull)
                .filter(need ->
                        compatibilityChecker.isCompatible(
                                need,
                                offer
                        )
                )
                .map(need ->
                        toScoredNeed(
                                offer,
                                need
                        )
                )
                .sorted(
                        Comparator.comparing(ScoredNeed::score)
                                .reversed()
                )
                .toList();
    }

    private ScoredNeed toScoredNeed(
            BusinessOffer offer,
            BusinessNeed need
    ) {
        MatchingScore matchingScore =
                matchingScoreCalculator.calculateForOffer(
                        offer,
                        need
                );

        return new ScoredNeed(
                need,
                matchingScore.totalScore()
        );
    }
}
