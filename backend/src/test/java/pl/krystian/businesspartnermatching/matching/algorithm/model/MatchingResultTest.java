package pl.krystian.businesspartnermatching.matching.algorithm.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class MatchingResultTest {

    @Test
    void shouldCreateEmptyResult() {
        // when
        PopularMatchingResult<String, String> result =
                new PopularMatchingResult<>(List.of());

        // then
        assertThat(result.isEmpty()).isTrue();
        assertThat(result.size()).isZero();
        assertThat(result.matches()).isEmpty();
    }

    @Test
    void shouldCreateResultContainingMatches() {
        // given
        Match<String, String> firstMatch =
                new Match<>("need-1", "offer-1");

        Match<String, String> secondMatch =
                new Match<>("need-2", "offer-2");

        // when
        PopularMatchingResult<String, String> result =
                new PopularMatchingResult<>(
                        List.of(firstMatch, secondMatch)
                );

        // then
        assertThat(result.matches())
                .containsExactly(firstMatch, secondMatch);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    void shouldReturnMatchesForLeftParticipant() {
        // given
        Match<String, String> firstMatch =
                new Match<>("need-1", "offer-1");

        Match<String, String> secondMatch =
                new Match<>("need-1", "offer-2");

        Match<String, String> thirdMatch =
                new Match<>("need-2", "offer-3");

        PopularMatchingResult<String, String> result =
                new PopularMatchingResult<>(
                        List.of(
                                firstMatch,
                                secondMatch,
                                thirdMatch
                        )
                );

        // when
        List<Match<String, String>> matches =
                result.matchesForLeftParticipant("need-1");

        // then
        assertThat(matches)
                .containsExactly(firstMatch, secondMatch);
    }

    @Test
    void shouldReturnMatchesForRightParticipant() {
        // given
        Match<String, String> firstMatch =
                new Match<>("need-1", "offer-1");

        Match<String, String> secondMatch =
                new Match<>("need-2", "offer-1");

        Match<String, String> thirdMatch =
                new Match<>("need-3", "offer-2");

        PopularMatchingResult<String, String> result =
                new PopularMatchingResult<>(
                        List.of(
                                firstMatch,
                                secondMatch,
                                thirdMatch
                        )
                );

        // when
        List<Match<String, String>> matches =
                result.matchesForRightParticipant("offer-1");

        // then
        assertThat(matches)
                .containsExactly(firstMatch, secondMatch);
    }

    @Test
    void shouldReturnEmptyListWhenLeftParticipantHasNoMatches() {
        // given
        PopularMatchingResult<String, String> result =
                new PopularMatchingResult<>(
                        List.of(
                                new Match<>(
                                        "need-1",
                                        "offer-1"
                                )
                        )
                );

        // when
        List<Match<String, String>> matches =
                result.matchesForLeftParticipant("need-2");

        // then
        assertThat(matches).isEmpty();
    }

    @Test
    void shouldReturnTrueWhenResultContainsMatch() {
        // given
        PopularMatchingResult<String, String> result =
                new PopularMatchingResult<>(
                        List.of(
                                new Match<>(
                                        "need-1",
                                        "offer-1"
                                )
                        )
                );

        // when
        boolean contains = result.contains(
                "need-1",
                "offer-1"
        );

        // then
        assertThat(contains).isTrue();
    }

    @Test
    void shouldReturnFalseWhenResultDoesNotContainMatch() {
        // given
        PopularMatchingResult<String, String> result =
                new PopularMatchingResult<>(
                        List.of(
                                new Match<>(
                                        "need-1",
                                        "offer-1"
                                )
                        )
                );

        // when
        boolean contains = result.contains(
                "need-1",
                "offer-2"
        );

        // then
        assertThat(contains).isFalse();
    }

    @Test
    void shouldCreateDefensiveCopyOfMatches() {
        // given
        List<Match<String, String>> matches =
                new ArrayList<>();

        matches.add(
                new Match<>(
                        "need-1",
                        "offer-1"
                )
        );

        PopularMatchingResult<String, String> result =
                new PopularMatchingResult<>(matches);

        // when
        matches.add(
                new Match<>(
                        "need-2",
                        "offer-2"
                )
        );

        // then
        assertThat(result.matches()).hasSize(1);
    }

    @Test
    void shouldExposeUnmodifiableMatches() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new PopularMatchingResult<>(
                        List.of(
                                new Match<>(
                                        "need-1",
                                        "offer-1"
                                ),
                                new Match<>(
                                        "need-1",
                                        "offer-1"
                                )
                        )
                ));
    }

    @Test
    void shouldRejectNullMatchesList() {
        assertThatNullPointerException()
                .isThrownBy(() ->
                        new PopularMatchingResult<String, String>(null)
                )
                .withMessage(
                        "Stable matches cannot be null"
                );
    }

    @Test
    void shouldRejectListContainingNullMatch() {
        // given
        List<Match<String, String>> matches =
                new ArrayList<>();

        matches.add(
                new Match<>(
                        "need-1",
                        "offer-1"
                )
        );
        matches.add(null);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->
                        new PopularMatchingResult<>(matches)
                )
                .withMessage(
                        "Stable matches cannot contain null"
                );
    }

    @Test
    void shouldRejectDuplicateMatches() {
        // given
        Match<String, String> match =
                new Match<>(
                        "need-1",
                        "offer-1"
                );

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->
                        new PopularMatchingResult<>(
                                List.of(match, match)
                        )
                )
                .withMessage(
                        "Stable matches cannot contain duplicates"
                );
    }

    @Test
    void shouldRejectNullLeftParticipantWhenSearchingMatches() {
        // given
        PopularMatchingResult<String, String> result =
                new PopularMatchingResult<>(List.of());

        // then
        assertThatNullPointerException()
                .isThrownBy(() ->
                        result.matchesForLeftParticipant(null)
                )
                .withMessage(
                        "Left participant cannot be null"
                );
    }

    @Test
    void shouldRejectNullRightParticipantWhenSearchingMatches() {
        // given
        PopularMatchingResult<String, String> result =
                new PopularMatchingResult<>(List.of());

        // then
        assertThatNullPointerException()
                .isThrownBy(() ->
                        result.matchesForRightParticipant(null)
                )
                .withMessage(
                        "Right participant cannot be null"
                );
    }

    @Test
    void shouldRejectNullLeftParticipantWhenCheckingMatch() {
        // given
        PopularMatchingResult<String, String> result =
                new PopularMatchingResult<>(List.of());

        // then
        assertThatNullPointerException()
                .isThrownBy(() ->
                        result.contains(null, "offer-1")
                )
                .withMessage(
                        "Left participant cannot be null"
                );
    }

    @Test
    void shouldRejectNullRightParticipantWhenCheckingMatch() {
        // given
        PopularMatchingResult<String, String> result =
                new PopularMatchingResult<>(List.of());

        // then
        assertThatNullPointerException()
                .isThrownBy(() ->
                        result.contains("need-1", null)
                )
                .withMessage(
                        "Right participant cannot be null"
                );
    }
}
