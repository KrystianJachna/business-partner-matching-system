package pl.krystian.businesspartnermatching.matching.preference;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParticipantPreferenceSetTest {

    @Test
    void shouldCreatePreferenceSetFromParticipantPreferences() {
        // given
        ParticipantPreferences<String, String> firstPreferences =
                preferences(
                        "first participant",
                        "candidate A",
                        "candidate B"
                );

        ParticipantPreferences<String, String> secondPreferences =
                preferences(
                        "second participant",
                        "candidate B",
                        "candidate A"
                );

        // when
        ParticipantPreferenceSet<String, String> preferenceSet =
                ParticipantPreferenceSet.from(
                        List.of(
                                firstPreferences,
                                secondPreferences
                        )
                );

        // then
        assertThat(preferenceSet.size())
                .isEqualTo(2);

        assertThat(preferenceSet.participants())
                .containsExactlyInAnyOrder(
                        "first participant",
                        "second participant"
                );

        assertThat(preferenceSet.getFor("first participant"))
                .isSameAs(firstPreferences);

        assertThat(preferenceSet.getFor("second participant"))
                .isSameAs(secondPreferences);
    }

    @Test
    void shouldCreateEmptyPreferenceSet() {
        // when
        ParticipantPreferenceSet<String, String> preferenceSet =
                ParticipantPreferenceSet.from(List.of());

        // then
        assertThat(preferenceSet.isEmpty())
                .isTrue();

        assertThat(preferenceSet.size())
                .isZero();

        assertThat(preferenceSet.participants())
                .isEmpty();

        assertThat(preferenceSet.preferences())
                .isEmpty();
    }

    @Test
    void shouldFindPreferencesForExistingParticipant() {
        // given
        ParticipantPreferences<String, String> preferences =
                preferences(
                        "participant",
                        "first",
                        "second"
                );

        ParticipantPreferenceSet<String, String> preferenceSet =
                ParticipantPreferenceSet.from(
                        List.of(preferences)
                );

        // when
        var result = preferenceSet.findFor("participant");

        // then
        assertThat(result)
                .containsSame(preferences);
    }

    @Test
    void shouldReturnEmptyOptionalForUnknownParticipant() {
        // given
        ParticipantPreferenceSet<String, String> preferenceSet =
                ParticipantPreferenceSet.from(
                        List.of(
                                preferences(
                                        "participant",
                                        "candidate"
                                )
                        )
                );

        // when
        var result = preferenceSet.findFor("unknown");

        // then
        assertThat(result)
                .isEmpty();
    }

    @Test
    void shouldReturnTrueWhenParticipantIsPresent() {
        // given
        ParticipantPreferenceSet<String, String> preferenceSet =
                ParticipantPreferenceSet.from(
                        List.of(
                                preferences(
                                        "participant",
                                        "candidate"
                                )
                        )
                );

        // when
        boolean contains =
                preferenceSet.containsParticipant("participant");

        // then
        assertThat(contains)
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenParticipantIsNotPresent() {
        // given
        ParticipantPreferenceSet<String, String> preferenceSet =
                ParticipantPreferenceSet.from(
                        List.of(
                                preferences(
                                        "participant",
                                        "candidate"
                                )
                        )
                );

        // when
        boolean contains =
                preferenceSet.containsParticipant("unknown");

        // then
        assertThat(contains)
                .isFalse();
    }

    @Test
    void shouldRejectGetForUnknownParticipant() {
        // given
        ParticipantPreferenceSet<String, String> preferenceSet =
                ParticipantPreferenceSet.from(
                        List.of(
                                preferences(
                                        "participant",
                                        "candidate"
                                )
                        )
                );

        // when / then
        assertThatThrownBy(() ->
                preferenceSet.getFor("unknown")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Preferences for participant are not available"
                );
    }

    @Test
    void shouldRejectDuplicateParticipants() {
        // given
        ParticipantPreferences<String, String> firstPreferences =
                preferences(
                        "participant",
                        "first candidate"
                );

        ParticipantPreferences<String, String> secondPreferences =
                preferences(
                        "participant",
                        "second candidate"
                );

        // when / then
        assertThatThrownBy(() ->
                ParticipantPreferenceSet.from(
                        List.of(
                                firstPreferences,
                                secondPreferences
                        )
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Participant preferences collection cannot contain duplicate participants"
                );
    }

    @Test
    void shouldRejectNullPreferencesMap() {
        // when / then
        assertThatThrownBy(() ->
                new ParticipantPreferenceSet<String, String>(
                        null
                )
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage(
                        "Participant preferences map cannot be null"
                );
    }

    @Test
    void shouldRejectMapContainingNullKey() {
        // given
        Map<String, ParticipantPreferences<String, String>> map =
                new LinkedHashMap<>();

        map.put(
                null,
                preferences(
                        "participant",
                        "candidate"
                )
        );

        // when / then
        assertThatThrownBy(() ->
                new ParticipantPreferenceSet<>(map)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Participant preferences map cannot contain null keys or values"
                );
    }

    @Test
    void shouldRejectMapContainingNullValue() {
        // given
        Map<String, ParticipantPreferences<String, String>> map =
                new LinkedHashMap<>();

        map.put(
                "participant",
                null
        );

        // when / then
        assertThatThrownBy(() ->
                new ParticipantPreferenceSet<>(map)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Participant preferences map cannot contain null keys or values"
                );
    }

    @Test
    void shouldRejectMapWithMismatchedParticipant() {
        // given
        Map<String, ParticipantPreferences<String, String>> map =
                Map.of(
                        "map participant",
                        preferences(
                                "stored participant",
                                "candidate"
                        )
                );

        // when / then
        assertThatThrownBy(() ->
                new ParticipantPreferenceSet<>(map)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Map key must match the participant stored in preferences"
                );
    }

    @Test
    void shouldRejectNullPreferencesCollection() {
        // when / then
        assertThatThrownBy(() ->
                ParticipantPreferenceSet
                        .<String, String>from(null)
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage(
                        "Participant preferences collection cannot be null"
                );
    }

    @Test
    void shouldRejectPreferencesCollectionContainingNull() {
        // given
        List<ParticipantPreferences<String, String>> preferences =
                new ArrayList<>();

        preferences.add(
                preferences(
                        "participant",
                        "candidate"
                )
        );

        preferences.add(null);

        // when / then
        assertThatThrownBy(() ->
                ParticipantPreferenceSet.from(preferences)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Participant preferences collection cannot contain null"
                );
    }

    @Test
    void shouldRejectNullParticipantInFindFor() {
        // given
        ParticipantPreferenceSet<String, String> preferenceSet =
                ParticipantPreferenceSet.from(List.of());

        // when / then
        assertThatThrownBy(() ->
                preferenceSet.findFor(null)
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage(
                        "Participant cannot be null"
                );
    }

    @Test
    void shouldRejectNullParticipantInContainsParticipant() {
        // given
        ParticipantPreferenceSet<String, String> preferenceSet =
                ParticipantPreferenceSet.from(List.of());

        // when / then
        assertThatThrownBy(() ->
                preferenceSet.containsParticipant(null)
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage(
                        "Participant cannot be null"
                );
    }

    @Test
    void shouldCreateDefensiveCopyOfPreferencesMap() {
        // given
        ParticipantPreferences<String, String> firstPreferences =
                preferences(
                        "first participant",
                        "candidate"
                );

        Map<String, ParticipantPreferences<String, String>> map =
                new LinkedHashMap<>();

        map.put(
                "first participant",
                firstPreferences
        );

        ParticipantPreferenceSet<String, String> preferenceSet =
                new ParticipantPreferenceSet<>(map);

        // when
        map.put(
                "second participant",
                preferences(
                        "second participant",
                        "candidate"
                )
        );

        // then
        assertThat(preferenceSet.size())
                .isEqualTo(1);

        assertThat(preferenceSet.containsParticipant("second participant"))
                .isFalse();
    }

    @Test
    void shouldExposeUnmodifiablePreferencesMap() {
        // given
        ParticipantPreferenceSet<String, String> preferenceSet =
                ParticipantPreferenceSet.from(
                        List.of(
                                preferences(
                                        "participant",
                                        "candidate"
                                )
                        )
                );

        // when / then
        assertThatThrownBy(() ->
                preferenceSet
                        .preferencesByParticipant()
                        .put(
                                "second participant",
                                preferences(
                                        "second participant",
                                        "candidate"
                                )
                        )
        )
                .isInstanceOf(
                        UnsupportedOperationException.class
                );
    }

    private ParticipantPreferences<String, String> preferences(
            String participant,
            String... candidates
    ) {
        return new ParticipantPreferences<>(
                participant,
                List.of(candidates)
        );
    }
}
