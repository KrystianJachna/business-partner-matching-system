package pl.krystian.businesspartnermatching.matching.preference;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParticipantPreferencesTest {

    @Test
    void shouldCreateParticipantPreferencesWithCandidatesInGivenOrder() {
        // given
        String participant = "participant";
        String firstCandidate = "first";
        String secondCandidate = "second";
        String thirdCandidate = "third";

        // when
        ParticipantPreferences<String, String> preferences =
                new ParticipantPreferences<>(
                        participant,
                        List.of(
                                firstCandidate,
                                secondCandidate,
                                thirdCandidate
                        )
                );

        // then
        assertThat(preferences.participant())
                .isEqualTo(participant);

        assertThat(preferences.preferredCandidates())
                .containsExactly(
                        firstCandidate,
                        secondCandidate,
                        thirdCandidate
                );
    }

    @Test
    void shouldReturnNumberOfPreferredCandidates() {
        // given
        ParticipantPreferences<String, String> preferences =
                new ParticipantPreferences<>(
                        "participant",
                        List.of(
                                "first",
                                "second",
                                "third"
                        )
                );

        // when
        int size = preferences.size();

        // then
        assertThat(size)
                .isEqualTo(3);
    }

    @Test
    void shouldReturnTrueWhenPreferencesAreEmpty() {
        // given
        ParticipantPreferences<String, String> preferences =
                new ParticipantPreferences<>(
                        "participant",
                        List.of()
                );

        // when
        boolean empty = preferences.isEmpty();

        // then
        assertThat(empty)
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenPreferencesAreNotEmpty() {
        // given
        ParticipantPreferences<String, String> preferences =
                new ParticipantPreferences<>(
                        "participant",
                        List.of("candidate")
                );

        // when
        boolean empty = preferences.isEmpty();

        // then
        assertThat(empty)
                .isFalse();
    }

    @Test
    void shouldReturnTrueWhenCandidateIsPresent() {
        // given
        String candidate = "candidate";

        ParticipantPreferences<String, String> preferences =
                new ParticipantPreferences<>(
                        "participant",
                        List.of(candidate)
                );

        // when
        boolean contains = preferences.contains(candidate);

        // then
        assertThat(contains)
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenCandidateIsNotPresent() {
        // given
        ParticipantPreferences<String, String> preferences =
                new ParticipantPreferences<>(
                        "participant",
                        List.of("first")
                );

        // when
        boolean contains = preferences.contains("second");

        // then
        assertThat(contains)
                .isFalse();
    }

    @Test
    void shouldReturnCandidatePosition() {
        // given
        String firstCandidate = "first";
        String secondCandidate = "second";
        String thirdCandidate = "third";

        ParticipantPreferences<String, String> preferences =
                new ParticipantPreferences<>(
                        "participant",
                        List.of(
                                firstCandidate,
                                secondCandidate,
                                thirdCandidate
                        )
                );

        // when
        int position = preferences.positionOf(secondCandidate);

        // then
        assertThat(position)
                .isEqualTo(1);
    }

    @Test
    void shouldReturnTrueWhenFirstCandidateIsPreferred() {
        // given
        String preferredCandidate = "preferred";
        String lessPreferredCandidate = "less preferred";

        ParticipantPreferences<String, String> preferences =
                new ParticipantPreferences<>(
                        "participant",
                        List.of(
                                preferredCandidate,
                                lessPreferredCandidate
                        )
                );

        // when
        boolean prefers = preferences.prefers(
                preferredCandidate,
                lessPreferredCandidate
        );

        // then
        assertThat(prefers)
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenSecondCandidateIsPreferred() {
        // given
        String preferredCandidate = "preferred";
        String lessPreferredCandidate = "less preferred";

        ParticipantPreferences<String, String> preferences =
                new ParticipantPreferences<>(
                        "participant",
                        List.of(
                                preferredCandidate,
                                lessPreferredCandidate
                        )
                );

        // when
        boolean prefers = preferences.prefers(
                lessPreferredCandidate,
                preferredCandidate
        );

        // then
        assertThat(prefers)
                .isFalse();
    }

    @Test
    void shouldRejectPositionRequestForUnknownCandidate() {
        // given
        ParticipantPreferences<String, String> preferences =
                new ParticipantPreferences<>(
                        "participant",
                        List.of("first")
                );

        // when / then
        assertThatThrownBy(() ->
                preferences.positionOf("unknown")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Candidate is not present in participant preferences"
                );
    }

    @Test
    void shouldRejectNullParticipant() {
        // when / then
        assertThatThrownBy(() ->
                new ParticipantPreferences<String, String>(
                        null,
                        List.of()
                )
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage(
                        "Preference participant cannot be null"
                );
    }

    @Test
    void shouldRejectNullPreferredCandidates() {
        // when / then
        assertThatThrownBy(() ->
                new ParticipantPreferences<String, String>(
                        "participant",
                        null
                )
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage(
                        "Preferred candidates cannot be null"
                );
    }

    @Test
    void shouldRejectPreferredCandidatesContainingNull() {
        // given
        List<String> candidates = new ArrayList<>();
        candidates.add("first");
        candidates.add(null);

        // when / then
        assertThatThrownBy(() ->
                new ParticipantPreferences<>(
                        "participant",
                        candidates
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Preferred candidates cannot contain null"
                );
    }

    @Test
    void shouldCreateDefensiveCopyOfPreferredCandidates() {
        // given
        List<String> candidates =
                new ArrayList<>(
                        List.of(
                                "first",
                                "second"
                        )
                );

        ParticipantPreferences<String, String> preferences =
                new ParticipantPreferences<>(
                        "participant",
                        candidates
                );

        // when
        candidates.add("third");

        // then
        assertThat(preferences.preferredCandidates())
                .containsExactly(
                        "first",
                        "second"
                );
    }

    @Test
    void shouldExposeUnmodifiablePreferredCandidates() {
        // given
        ParticipantPreferences<String, String> preferences =
                new ParticipantPreferences<>(
                        "participant",
                        List.of(
                                "first",
                                "second"
                        )
                );

        // when / then
        assertThatThrownBy(() ->
                preferences
                        .preferredCandidates()
                        .add("third")
        )
                .isInstanceOf(
                        UnsupportedOperationException.class
                );
    }
}
