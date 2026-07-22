package pl.krystian.businesspartnermatching.matching.algorithm.gale;

import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.matching.algorithm.model.Match;
import pl.krystian.businesspartnermatching.matching.algorithm.model.MatchingProblem;
import pl.krystian.businesspartnermatching.matching.algorithm.model.ParticipantCapacitySet;
import pl.krystian.businesspartnermatching.matching.algorithm.model.PopularMatchingResult;
import pl.krystian.businesspartnermatching.matching.preference.model.ParticipantPreferenceSet;
import pl.krystian.businesspartnermatching.matching.preference.model.ParticipantPreferences;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ManyToManyGaleShapleyAlgorithmTest {

    private final ManyToManyGaleShapleyAlgorithm<String, String> algorithm =
            new ManyToManyGaleShapleyAlgorithm<>();

    @Test
    void shouldCreateOneToOneMatchesAccordingToPreferences() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                preferenceSet(
                        preferences(
                                "need-1",
                                "offer-1",
                                "offer-2"
                        ),
                        preferences(
                                "need-2",
                                "offer-2",
                                "offer-1"
                        )
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                preferenceSet(
                        preferences(
                                "offer-1",
                                "need-1",
                                "need-2"
                        ),
                        preferences(
                                "offer-2",
                                "need-2",
                                "need-1"
                        )
                );

        MatchingProblem<String, String> problem =
                problem(
                        leftPreferences,
                        rightPreferences,
                        Map.of(
                                "need-1", 1,
                                "need-2", 1
                        ),
                        Map.of(
                                "offer-1", 1,
                                "offer-2", 1
                        )
                );

        // when
        PopularMatchingResult<String, String> result =
                algorithm.match(problem);

        // then
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.contains("need-1", "offer-1"))
                .isTrue();

        assertThat(result.contains("need-2", "offer-2"))
                .isTrue();
    }

    @Test
    void shouldCreateMultipleMatchesWhenParticipantsHaveHigherCapacities() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                preferenceSet(
                        preferences(
                                "need-1",
                                "offer-1",
                                "offer-2"
                        ),
                        preferences(
                                "need-2",
                                "offer-1",
                                "offer-2"
                        )
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                preferenceSet(
                        preferences(
                                "offer-1",
                                "need-1",
                                "need-2"
                        ),
                        preferences(
                                "offer-2",
                                "need-1",
                                "need-2"
                        )
                );

        MatchingProblem<String, String> problem =
                problem(
                        leftPreferences,
                        rightPreferences,
                        Map.of(
                                "need-1", 2,
                                "need-2", 2
                        ),
                        Map.of(
                                "offer-1", 2,
                                "offer-2", 2
                        )
                );

        // when
        PopularMatchingResult<String, String> result =
                algorithm.match(problem);

        // then
        assertThat(result.size()).isEqualTo(4);

        assertThat(result.contains("need-1", "offer-1"))
                .isTrue();

        assertThat(result.contains("need-1", "offer-2"))
                .isTrue();

        assertThat(result.contains("need-2", "offer-1"))
                .isTrue();

        assertThat(result.contains("need-2", "offer-2"))
                .isTrue();

        assertThat(result.matchesForLeftParticipant("need-1"))
                .hasSize(2);

        assertThat(result.matchesForLeftParticipant("need-2"))
                .hasSize(2);

        assertThat(result.matchesForRightParticipant("offer-1"))
                .hasSize(2);

        assertThat(result.matchesForRightParticipant("offer-2"))
                .hasSize(2);
    }

    @Test
    void shouldReplaceWorseCurrentMatchWithMorePreferredParticipant() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                preferenceSet(
                        preferences(
                                "need-1",
                                "offer-1",
                                "offer-2"
                        ),
                        preferences(
                                "need-2",
                                "offer-1",
                                "offer-2"
                        )
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                preferenceSet(
                        preferences(
                                "offer-1",
                                "need-2",
                                "need-1"
                        ),
                        preferences(
                                "offer-2",
                                "need-1",
                                "need-2"
                        )
                );

        MatchingProblem<String, String> problem =
                problem(
                        leftPreferences,
                        rightPreferences,
                        Map.of(
                                "need-1", 1,
                                "need-2", 1
                        ),
                        Map.of(
                                "offer-1", 1,
                                "offer-2", 1
                        )
                );

        // when
        PopularMatchingResult<String, String> result =
                algorithm.match(problem);

        // then
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.contains("need-2", "offer-1"))
                .isTrue();

        assertThat(result.contains("need-1", "offer-2"))
                .isTrue();

        assertThat(result.contains("need-1", "offer-1"))
                .isFalse();

        assertThat(result.contains("need-2", "offer-2"))
                .isFalse();
    }

    @Test
    void shouldNotCreateMatchWhenRightParticipantDoesNotAcceptLeftParticipant() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                preferenceSet(
                        preferences(
                                "need-1",
                                "offer-1"
                        )
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                preferenceSet(
                        preferences(
                                "offer-1",
                                "need-2"
                        )
                );

        MatchingProblem<String, String> problem =
                problem(
                        leftPreferences,
                        rightPreferences,
                        Map.of(
                                "need-1", 1
                        ),
                        Map.of(
                                "offer-1", 1
                        )
                );

        // when
        PopularMatchingResult<String, String> result =
                algorithm.match(problem);

        // then
        assertThat(result.size()).isZero();

        assertThat(result.contains("need-1", "offer-1"))
                .isFalse();
    }

    @Test
    void shouldLeaveParticipantUnmatchedWhenPreferenceListIsEmpty() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                preferenceSet(
                        preferences("need-1")
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                preferenceSet(
                        preferences(
                                "offer-1",
                                "need-1"
                        )
                );

        MatchingProblem<String, String> problem =
                problem(
                        leftPreferences,
                        rightPreferences,
                        Map.of(
                                "need-1", 1
                        ),
                        Map.of(
                                "offer-1", 1
                        )
                );

        // when
        PopularMatchingResult<String, String> result =
                algorithm.match(problem);

        // then
        assertThat(result.size()).isZero();

        assertThat(result.matchesForLeftParticipant("need-1"))
                .isEmpty();

        assertThat(result.matchesForRightParticipant("offer-1"))
                .isEmpty();
    }

    @Test
    void shouldProposeToNextParticipantAfterProposalIsRejected() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                preferenceSet(
                        preferences(
                                "need-1",
                                "offer-1",
                                "offer-2"
                        )
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                preferenceSet(
                        preferences(
                                "offer-1",
                                "need-2"
                        ),
                        preferences(
                                "offer-2",
                                "need-1"
                        )
                );

        MatchingProblem<String, String> problem =
                problem(
                        leftPreferences,
                        rightPreferences,
                        Map.of(
                                "need-1", 1
                        ),
                        Map.of(
                                "offer-1", 1,
                                "offer-2", 1
                        )
                );

        // when
        PopularMatchingResult<String, String> result =
                algorithm.match(problem);

        // then
        assertThat(result.size()).isEqualTo(1);

        assertThat(result.contains("need-1", "offer-1"))
                .isFalse();

        assertThat(result.contains("need-1", "offer-2"))
                .isTrue();
    }

    @Test
    void shouldNotCreateMoreMatchesThanAvailableCandidates() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                preferenceSet(
                        preferences(
                                "need-1",
                                "offer-1",
                                "offer-2"
                        )
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                preferenceSet(
                        preferences(
                                "offer-1",
                                "need-1"
                        ),
                        preferences(
                                "offer-2",
                                "need-1"
                        )
                );

        MatchingProblem<String, String> problem =
                problem(
                        leftPreferences,
                        rightPreferences,
                        Map.of(
                                "need-1", 5
                        ),
                        Map.of(
                                "offer-1", 1,
                                "offer-2", 1
                        )
                );

        // when
        PopularMatchingResult<String, String> result =
                algorithm.match(problem);

        // then
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.matchesForLeftParticipant("need-1"))
                .hasSize(2);

        assertThat(result.contains("need-1", "offer-1"))
                .isTrue();

        assertThat(result.contains("need-1", "offer-2"))
                .isTrue();
    }

    @Test
    void shouldNotExceedParticipantCapacities() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                preferenceSet(
                        preferences(
                                "need-1",
                                "offer-1",
                                "offer-2",
                                "offer-3"
                        ),
                        preferences(
                                "need-2",
                                "offer-1",
                                "offer-2",
                                "offer-3"
                        ),
                        preferences(
                                "need-3",
                                "offer-1",
                                "offer-2",
                                "offer-3"
                        )
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                preferenceSet(
                        preferences(
                                "offer-1",
                                "need-1",
                                "need-2",
                                "need-3"
                        ),
                        preferences(
                                "offer-2",
                                "need-2",
                                "need-3",
                                "need-1"
                        ),
                        preferences(
                                "offer-3",
                                "need-3",
                                "need-1",
                                "need-2"
                        )
                );

        MatchingProblem<String, String> problem =
                problem(
                        leftPreferences,
                        rightPreferences,
                        Map.of(
                                "need-1", 2,
                                "need-2", 2,
                                "need-3", 2
                        ),
                        Map.of(
                                "offer-1", 1,
                                "offer-2", 1,
                                "offer-3", 1
                        )
                );

        // when
        PopularMatchingResult<String, String> result =
                algorithm.match(problem);

        // then
        assertThat(result.matchesForLeftParticipant("need-1"))
                .hasSizeLessThanOrEqualTo(2);

        assertThat(result.matchesForLeftParticipant("need-2"))
                .hasSizeLessThanOrEqualTo(2);

        assertThat(result.matchesForLeftParticipant("need-3"))
                .hasSizeLessThanOrEqualTo(2);

        assertThat(result.matchesForRightParticipant("offer-1"))
                .hasSizeLessThanOrEqualTo(1);

        assertThat(result.matchesForRightParticipant("offer-2"))
                .hasSizeLessThanOrEqualTo(1);

        assertThat(result.matchesForRightParticipant("offer-3"))
                .hasSizeLessThanOrEqualTo(1);
    }

    @Test
    void shouldNotCreateDuplicateMatches() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                preferenceSet(
                        preferences(
                                "need-1",
                                "offer-1",
                                "offer-2"
                        ),
                        preferences(
                                "need-2",
                                "offer-1",
                                "offer-2"
                        )
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                preferenceSet(
                        preferences(
                                "offer-1",
                                "need-1",
                                "need-2"
                        ),
                        preferences(
                                "offer-2",
                                "need-1",
                                "need-2"
                        )
                );

        MatchingProblem<String, String> problem =
                problem(
                        leftPreferences,
                        rightPreferences,
                        Map.of(
                                "need-1", 2,
                                "need-2", 2
                        ),
                        Map.of(
                                "offer-1", 2,
                                "offer-2", 2
                        )
                );

        // when
        PopularMatchingResult<String, String> result =
                algorithm.match(problem);

        // then
        List<Match<String, String>> matches =
                result.matches();

        assertThat(matches)
                .doesNotHaveDuplicates();

        assertThat(matches)
                .hasSize(4);
    }

    @Test
    void shouldProduceStableMatchingWithoutBlockingPair() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                preferenceSet(
                        preferences(
                                "need-1",
                                "offer-1",
                                "offer-2",
                                "offer-3"
                        ),
                        preferences(
                                "need-2",
                                "offer-1",
                                "offer-2",
                                "offer-3"
                        ),
                        preferences(
                                "need-3",
                                "offer-2",
                                "offer-1",
                                "offer-3"
                        )
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                preferenceSet(
                        preferences(
                                "offer-1",
                                "need-2",
                                "need-1",
                                "need-3"
                        ),
                        preferences(
                                "offer-2",
                                "need-1",
                                "need-3",
                                "need-2"
                        ),
                        preferences(
                                "offer-3",
                                "need-3",
                                "need-2",
                                "need-1"
                        )
                );

        Map<String, Integer> leftCapacities = Map.of(
                "need-1", 1,
                "need-2", 1,
                "need-3", 1
        );

        Map<String, Integer> rightCapacities = Map.of(
                "offer-1", 1,
                "offer-2", 1,
                "offer-3", 1
        );

        MatchingProblem<String, String> problem =
                problem(
                        leftPreferences,
                        rightPreferences,
                        leftCapacities,
                        rightCapacities
                );

        // when
        PopularMatchingResult<String, String> result =
                algorithm.match(problem);

        // then
        assertThat(
                hasBlockingPair(
                        result,
                        leftPreferences,
                        rightPreferences,
                        leftCapacities,
                        rightCapacities
                )
        ).isFalse();
    }

    @SafeVarargs
    private static <P, C> ParticipantPreferenceSet<P, C> preferenceSet(
            ParticipantPreferences<P, C>... preferences
    ) {
        return ParticipantPreferenceSet.from(
                List.of(preferences)
        );
    }

    @SafeVarargs
    private static <P, C> ParticipantPreferences<P, C> preferences(
            P participant,
            C... preferredCandidates
    ) {
        return new ParticipantPreferences<>(
                participant,
                List.of(preferredCandidates)
        );
    }

    private static <L, R> MatchingProblem<L, R> problem(
            ParticipantPreferenceSet<L, R> leftPreferences,
            ParticipantPreferenceSet<R, L> rightPreferences,
            Map<L, Integer> leftCapacities,
            Map<R, Integer> rightCapacities
    ) {
        return new MatchingProblem<>(
                leftPreferences,
                rightPreferences,
                new ParticipantCapacitySet<>(leftCapacities),
                new ParticipantCapacitySet<>(rightCapacities)
        );
    }

    private static <L, R> boolean hasBlockingPair(
            PopularMatchingResult<L, R> result,
            ParticipantPreferenceSet<L, R> leftPreferences,
            ParticipantPreferenceSet<R, L> rightPreferences,
            Map<L, Integer> leftCapacities,
            Map<R, Integer> rightCapacities
    ) {
        for (L leftParticipant : leftPreferences.participants()) {
            ParticipantPreferences<L, R> leftParticipantPreferences =
                    leftPreferences.getFor(leftParticipant);

            for (R rightParticipant
                    : leftParticipantPreferences.preferredCandidates()) {

                if (!rightPreferences.containsParticipant(rightParticipant)) {
                    continue;
                }

                ParticipantPreferences<R, L> rightParticipantPreferences =
                        rightPreferences.getFor(rightParticipant);

                if (!rightParticipantPreferences.contains(leftParticipant)) {
                    continue;
                }

                if (result.contains(leftParticipant, rightParticipant)) {
                    continue;
                }

                boolean leftWouldPreferPair =
                        hasFreeCapacityOrPrefersRightCandidate(
                                leftParticipant,
                                rightParticipant,
                                result.matchesForLeftParticipant(
                                        leftParticipant
                                ),
                                leftParticipantPreferences,
                                leftCapacities.get(leftParticipant)
                        );

                boolean rightWouldPreferPair =
                        hasFreeCapacityOrPrefersLeftCandidate(
                                rightParticipant,
                                leftParticipant,
                                result.matchesForRightParticipant(
                                        rightParticipant
                                ),
                                rightParticipantPreferences,
                                rightCapacities.get(rightParticipant)
                        );

                if (leftWouldPreferPair && rightWouldPreferPair) {
                    return true;
                }
            }
        }

        return false;
    }

    private static <L, R> boolean hasFreeCapacityOrPrefersRightCandidate(
            L leftParticipant,
            R rightCandidate,
            List<Match<L, R>> currentMatches,
            ParticipantPreferences<L, R> preferences,
            int capacity
    ) {
        if (currentMatches.size() < capacity) {
            return true;
        }

        R worstCurrentCandidate =
                currentMatches.stream()
                        .map(Match::rightParticipant)
                        .max(Comparator.comparingInt(preferences::positionOf)
                        )
                        .orElseThrow(() -> new IllegalStateException(
                                "Left participant %s has no current matches"
                                        .formatted(leftParticipant)
                        ));

        return preferences.prefers(
                rightCandidate,
                worstCurrentCandidate
        );
    }

    private static <L, R> boolean hasFreeCapacityOrPrefersLeftCandidate(
            R rightParticipant,
            L leftCandidate,
            List<Match<L, R>> currentMatches,
            ParticipantPreferences<R, L> preferences,
            int capacity
    ) {
        if (currentMatches.size() < capacity) {
            return true;
        }

        L worstCurrentCandidate =
                currentMatches.stream()
                        .map(Match::leftParticipant)
                        .max(Comparator.comparingInt(preferences::positionOf)
                        )
                        .orElseThrow(() -> new IllegalStateException(
                                "Right participant %s has no current matches"
                                        .formatted(rightParticipant)
                        ));

        return preferences.prefers(
                leftCandidate,
                worstCurrentCandidate
        );
    }
}
