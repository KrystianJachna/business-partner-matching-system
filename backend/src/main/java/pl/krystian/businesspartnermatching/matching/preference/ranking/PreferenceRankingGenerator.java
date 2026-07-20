package pl.krystian.businesspartnermatching.matching.preference.ranking;

import pl.krystian.businesspartnermatching.matching.preference.model.Preference;

import java.util.List;

public interface PreferenceRankingGenerator<P, C> {

    List<Preference<C>> generateRanking(
            P participant,
            List<C> candidates
    );
}
