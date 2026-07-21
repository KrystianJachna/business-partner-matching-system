package pl.krystian.businesspartnermatching.matching.algorithm.model;

import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.matching.preference.model.ParticipantPreferenceSet;
import pl.krystian.businesspartnermatching.matching.preference.model.ParticipantPreferences;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class MatchingProblemTest {

    @Test
    void shouldCreateMatchingProblemWhenParticipantsMatchCapacities() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                participantPreferenceSet(
                        participantPreferences(
                                "need-1",
                                "offer-1",
                                "offer-2"
                        ),
                        participantPreferences(
                                "need-2",
                                "offer-2",
                                "offer-1"
                        )
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                participantPreferenceSet(
                        participantPreferences(
                                "offer-1",
                                "need-1",
                                "need-2"
                        ),
                        participantPreferences(
                                "offer-2",
                                "need-2",
                                "need-1"
                        )
                );

        ParticipantCapacitySet<String> leftCapacities =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "need-1", 1,
                                "need-2", 2
                        )
                );

        ParticipantCapacitySet<String> rightCapacities =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "offer-1", 1,
                                "offer-2", 2
                        )
                );

        // when
        MatchingProblem<String, String> problem =
                new MatchingProblem<>(
                        leftPreferences,
                        rightPreferences,
                        leftCapacities,
                        rightCapacities
                );

        // then
        assertThat(problem.leftPreferences())
                .isSameAs(leftPreferences);

        assertThat(problem.rightPreferences())
                .isSameAs(rightPreferences);

        assertThat(problem.leftCapacities())
                .isSameAs(leftCapacities);

        assertThat(problem.rightCapacities())
                .isSameAs(rightCapacities);
    }

    @Test
    void shouldCreateEmptyMatchingProblem() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                participantPreferenceSet();

        ParticipantPreferenceSet<String, String> rightPreferences =
                participantPreferenceSet();

        ParticipantCapacitySet<String> leftCapacities =
                new ParticipantCapacitySet<>(Map.of());

        ParticipantCapacitySet<String> rightCapacities =
                new ParticipantCapacitySet<>(Map.of());

        // when
        MatchingProblem<String, String> problem =
                new MatchingProblem<>(
                        leftPreferences,
                        rightPreferences,
                        leftCapacities,
                        rightCapacities
                );

        // then
        assertThat(problem.leftPreferences().isEmpty()).isTrue();
        assertThat(problem.rightPreferences().isEmpty()).isTrue();
        assertThat(problem.leftCapacities().isEmpty()).isTrue();
        assertThat(problem.rightCapacities().isEmpty()).isTrue();
    }

    @Test
    void shouldRejectMissingLeftCapacityParticipant() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                participantPreferenceSet(
                        participantPreferences(
                                "need-1",
                                "offer-1"
                        ),
                        participantPreferences(
                                "need-2",
                                "offer-1"
                        )
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                participantPreferenceSet(
                        participantPreferences(
                                "offer-1",
                                "need-1",
                                "need-2"
                        )
                );

        ParticipantCapacitySet<String> leftCapacities =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "need-1", 1
                        )
                );

        ParticipantCapacitySet<String> rightCapacities =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "offer-1", 1
                        )
                );

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->
                        new MatchingProblem<>(
                                leftPreferences,
                                rightPreferences,
                                leftCapacities,
                                rightCapacities
                        )
                )
                .withMessage(
                        "Left preference participants and left capacity "
                                + "participants must contain exactly the same participants"
                );
    }

    @Test
    void shouldRejectAdditionalLeftCapacityParticipant() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                participantPreferenceSet(
                        participantPreferences(
                                "need-1",
                                "offer-1"
                        )
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                participantPreferenceSet(
                        participantPreferences(
                                "offer-1",
                                "need-1"
                        )
                );

        ParticipantCapacitySet<String> leftCapacities =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "need-1", 1,
                                "need-2", 1
                        )
                );

        ParticipantCapacitySet<String> rightCapacities =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "offer-1", 1
                        )
                );

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->
                        new MatchingProblem<>(
                                leftPreferences,
                                rightPreferences,
                                leftCapacities,
                                rightCapacities
                        )
                )
                .withMessage(
                        "Left preference participants and left capacity "
                                + "participants must contain exactly the same participants"
                );
    }

    @Test
    void shouldRejectDifferentLeftParticipantsWithSameSetSize() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                participantPreferenceSet(
                        participantPreferences(
                                "need-1",
                                "offer-1"
                        ),
                        participantPreferences(
                                "need-2",
                                "offer-1"
                        )
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                participantPreferenceSet(
                        participantPreferences(
                                "offer-1",
                                "need-1",
                                "need-2"
                        )
                );

        ParticipantCapacitySet<String> leftCapacities =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "need-1", 1,
                                "need-3", 1
                        )
                );

        ParticipantCapacitySet<String> rightCapacities =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "offer-1", 1
                        )
                );

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->
                        new MatchingProblem<>(
                                leftPreferences,
                                rightPreferences,
                                leftCapacities,
                                rightCapacities
                        )
                )
                .withMessage(
                        "Left preference participants and left capacity "
                                + "participants must contain exactly the same participants"
                );
    }

    @Test
    void shouldRejectMissingRightCapacityParticipant() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                participantPreferenceSet(
                        participantPreferences(
                                "need-1",
                                "offer-1",
                                "offer-2"
                        )
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                participantPreferenceSet(
                        participantPreferences(
                                "offer-1",
                                "need-1"
                        ),
                        participantPreferences(
                                "offer-2",
                                "need-1"
                        )
                );

        ParticipantCapacitySet<String> leftCapacities =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "need-1", 1
                        )
                );

        ParticipantCapacitySet<String> rightCapacities =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "offer-1", 1
                        )
                );

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->
                        new MatchingProblem<>(
                                leftPreferences,
                                rightPreferences,
                                leftCapacities,
                                rightCapacities
                        )
                )
                .withMessage(
                        "Right preference participants and right capacity "
                                + "participants must contain exactly the same participants"
                );
    }

    @Test
    void shouldRejectAdditionalRightCapacityParticipant() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                participantPreferenceSet(
                        participantPreferences(
                                "need-1",
                                "offer-1"
                        )
                );

        ParticipantPreferenceSet<String, String> rightPreferences =
                participantPreferenceSet(
                        participantPreferences(
                                "offer-1",
                                "need-1"
                        )
                );

        ParticipantCapacitySet<String> leftCapacities =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "need-1", 1
                        )
                );

        ParticipantCapacitySet<String> rightCapacities =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "offer-1", 1,
                                "offer-2", 1
                        )
                );

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->
                        new MatchingProblem<>(
                                leftPreferences,
                                rightPreferences,
                                leftCapacities,
                                rightCapacities
                        )
                )
                .withMessage(
                        "Right preference participants and right capacity "
                                + "participants must contain exactly the same participants"
                );
    }

    @Test
    void shouldRejectNullLeftPreferences() {
        // given
        ParticipantPreferenceSet<String, String> rightPreferences =
                participantPreferenceSet();

        ParticipantCapacitySet<String> leftCapacities =
                new ParticipantCapacitySet<>(Map.of());

        ParticipantCapacitySet<String> rightCapacities =
                new ParticipantCapacitySet<>(Map.of());

        // then
        assertThatNullPointerException()
                .isThrownBy(() ->
                        new MatchingProblem<>(
                                null,
                                rightPreferences,
                                leftCapacities,
                                rightCapacities
                        )
                )
                .withMessage(
                        "Left preferences cannot be null"
                );
    }

    @Test
    void shouldRejectNullRightPreferences() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                participantPreferenceSet();

        ParticipantCapacitySet<String> leftCapacities =
                new ParticipantCapacitySet<>(Map.of());

        ParticipantCapacitySet<String> rightCapacities =
                new ParticipantCapacitySet<>(Map.of());

        // then
        assertThatNullPointerException()
                .isThrownBy(() ->
                        new MatchingProblem<>(
                                leftPreferences,
                                null,
                                leftCapacities,
                                rightCapacities
                        )
                )
                .withMessage(
                        "Right preferences cannot be null"
                );
    }

    @Test
    void shouldRejectNullLeftCapacities() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                participantPreferenceSet();

        ParticipantPreferenceSet<String, String> rightPreferences =
                participantPreferenceSet();

        ParticipantCapacitySet<String> rightCapacities =
                new ParticipantCapacitySet<>(Map.of());

        // then
        assertThatNullPointerException()
                .isThrownBy(() ->
                        new MatchingProblem<>(
                                leftPreferences,
                                rightPreferences,
                                null,
                                rightCapacities
                        )
                )
                .withMessage(
                        "Left capacities cannot be null"
                );
    }

    @Test
    void shouldRejectNullRightCapacities() {
        // given
        ParticipantPreferenceSet<String, String> leftPreferences =
                participantPreferenceSet();

        ParticipantPreferenceSet<String, String> rightPreferences =
                participantPreferenceSet();

        ParticipantCapacitySet<String> leftCapacities =
                new ParticipantCapacitySet<>(Map.of());

        // then
        assertThatNullPointerException()
                .isThrownBy(() ->
                        new MatchingProblem<>(
                                leftPreferences,
                                rightPreferences,
                                leftCapacities,
                                null
                        )
                )
                .withMessage(
                        "Right capacities cannot be null"
                );
    }

    @SafeVarargs
    private static <P, C> ParticipantPreferenceSet<P, C>
    participantPreferenceSet(
            ParticipantPreferences<P, C>... preferences
    ) {
        return ParticipantPreferenceSet.from(
                List.of(preferences)
        );
    }

    @SafeVarargs
    private static <P, C> ParticipantPreferences<P, C>
    participantPreferences(
            P participant,
            C... preferredCandidates
    ) {
        return new ParticipantPreferences<>(
                participant,
                List.of(preferredCandidates)
        );
    }
}
