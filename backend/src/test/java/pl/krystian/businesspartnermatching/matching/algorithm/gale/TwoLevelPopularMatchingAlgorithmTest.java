package pl.krystian.businesspartnermatching.matching.algorithm.gale;

import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.matching.algorithm.model.MatchingProblem;
import pl.krystian.businesspartnermatching.matching.algorithm.model.ParticipantCapacitySet;
import pl.krystian.businesspartnermatching.matching.algorithm.model.PopularMatchingResult;
import pl.krystian.businesspartnermatching.matching.preference.model.ParticipantPreferenceSet;
import pl.krystian.businesspartnermatching.matching.preference.model.ParticipantPreferences;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TwoLevelPopularMatchingAlgorithmTest {

    private final TwoLevelPopularMatchingAlgorithm<String, String> algorithm =
            new TwoLevelPopularMatchingAlgorithm<>();

    @Test
    void shouldMatchLevelZeroCopyWhenItCanFillItsCapacity() {
        MatchingProblem<String, String> problem = problem(
                preferences("need", "offer-1", "offer-2"),
                preferences("offer-1", "need"),
                preferences("offer-2", "need"),
                1,
                1,
                1
        );

        PopularMatchingResult<String, String> result =
                algorithm.match(problem);

        assertThat(result.matches())
                .containsExactlyInAnyOrder(
                        match("need", "offer-1")
                );
    }

    @Test
    void shouldReplaceLevelZeroMatchWhenLevelOneCopyProposesToTheSameRightParticipant() {
        MatchingProblem<String, String> problem = problem(
                preferences("need", "offer"),
                preferences("offer", "need"),
                2,
                1
        );

        PopularMatchingResult<String, String> result =
                algorithm.match(problem);

        assertThat(result.matches())
                .containsExactlyInAnyOrder(
                        match("need", "offer")
                );
    }

    @Test
    void shouldRespectMutualAcceptability() {
        MatchingProblem<String, String> problem = problem(
                preferences("need", "offer-1", "offer-2"),
                preferences("offer-1", "need"),
                preferences("offer-2", "other-need"),
                1,
                1,
                1
        );

        PopularMatchingResult<String, String> result =
                algorithm.match(problem);

        assertThat(result.matches())
                .containsExactlyInAnyOrder(
                        match("need", "offer-1")
                );
    }

    private static <L, R> MatchingProblem<L, R> problem(
            ParticipantPreferences<L, R> left,
            ParticipantPreferences<R, L> right,
            int leftCapacity,
            int rightCapacity
    ) {
        return new MatchingProblem<>(
                ParticipantPreferenceSet.from(List.of(left)),
                ParticipantPreferenceSet.from(List.of(right)),
                new ParticipantCapacitySet<>(Map.of(
                        left.participant(), leftCapacity
                )),
                new ParticipantCapacitySet<>(Map.of(
                        right.participant(), rightCapacity
                ))
        );
    }

    private static <L, R> MatchingProblem<L, R> problem(
            ParticipantPreferences<L, R> left,
            ParticipantPreferences<R, L> firstRight,
            ParticipantPreferences<R, L> secondRight,
            int leftCapacity,
            int firstRightCapacity,
            int secondRightCapacity
    ) {
        return new MatchingProblem<>(
                ParticipantPreferenceSet.from(List.of(left)),
                ParticipantPreferenceSet.from(List.of(firstRight, secondRight)),
                new ParticipantCapacitySet<>(Map.of(
                        left.participant(), leftCapacity
                )),
                new ParticipantCapacitySet<>(Map.of(
                        firstRight.participant(), firstRightCapacity,
                        secondRight.participant(), secondRightCapacity
                ))
        );
    }

    private static <P, C> ParticipantPreferences<P, C> preferences(
            P participant,
            C... candidates
    ) {
        return new ParticipantPreferences<>(participant, List.of(candidates));
    }

    private static <L, R> pl.krystian.businesspartnermatching.matching.algorithm.model.Match<L, R> match(
            L left,
            R right
    ) {
        return new pl.krystian.businesspartnermatching.matching.algorithm.model.Match<>(
                left,
                right
        );
    }
}
