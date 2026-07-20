package pl.krystian.businesspartnermatching.matching.preference.generation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.matching.preference.model.ParticipantPreferences;
import pl.krystian.businesspartnermatching.matching.preference.model.Preference;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParticipantPreferencesGeneratorTest {

    private ParticipantPreferencesGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new ParticipantPreferencesGenerator();
    }

    @Test
    void shouldGenerateParticipantPreferencesFromRanking() {
        // given
        String participant = "participant";

        Preference<String> firstPreference =
                new Preference<>(
                        "first",
                        new BigDecimal("0.9000")
                );

        Preference<String> secondPreference =
                new Preference<>(
                        "second",
                        new BigDecimal("0.7000")
                );

        Preference<String> thirdPreference =
                new Preference<>(
                        "third",
                        new BigDecimal("0.5000")
                );

        List<Preference<String>> ranking = List.of(
                firstPreference,
                secondPreference,
                thirdPreference
        );

        // when
        ParticipantPreferences<String, String> preferences =
                generator.generate(
                        participant,
                        ranking
                );

        // then
        assertThat(preferences.participant())
                .isEqualTo(participant);

        assertThat(preferences.preferredCandidates())
                .containsExactly(
                        "first",
                        "second",
                        "third"
                );
    }

    @Test
    void shouldGenerateEmptyPreferencesFromEmptyRanking() {
        // given
        String participant = "participant";

        // when
        ParticipantPreferences<String, String> preferences =
                generator.generate(
                        participant,
                        List.of()
                );

        // then
        assertThat(preferences.participant())
                .isEqualTo(participant);

        assertThat(preferences.preferredCandidates())
                .isEmpty();
    }

    @Test
    void shouldIgnorePreferenceScoresAndPreserveRankingOrder() {
        // given
        List<Preference<String>> ranking = List.of(
                new Preference<>(
                        "first",
                        new BigDecimal("0.1000")
                ),
                new Preference<>(
                        "second",
                        new BigDecimal("0.9000")
                ),
                new Preference<>(
                        "third",
                        new BigDecimal("0.5000")
                )
        );

        // when
        ParticipantPreferences<String, String> preferences =
                generator.generate(
                        "participant",
                        ranking
                );

        // then
        assertThat(preferences.preferredCandidates())
                .containsExactly(
                        "first",
                        "second",
                        "third"
                );
    }

    @Test
    void shouldRejectNullParticipant() {
        // given
        List<Preference<String>> ranking = List.of(
                new Preference<>(
                        "candidate",
                        BigDecimal.ONE
                )
        );

        // when / then
        assertThatThrownBy(() ->
                generator.generate(
                        null,
                        ranking
                )
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage(
                        "Preference participant cannot be null"
                );
    }

    @Test
    void shouldRejectNullRanking() {
        // when / then
        assertThatThrownBy(() ->
                generator.generate(
                        "participant",
                        null
                )
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage(
                        "Preference ranking cannot be null"
                );
    }

    @Test
    void shouldRejectRankingContainingNull() {
        // given
        List<Preference<String>> ranking = new ArrayList<>();
        ranking.add(
                new Preference<>(
                        "first",
                        BigDecimal.ONE
                )
        );
        ranking.add(null);

        // when / then
        assertThatThrownBy(() ->
                generator.generate(
                        "participant",
                        ranking
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Preference ranking cannot contain null"
                );
    }
}
