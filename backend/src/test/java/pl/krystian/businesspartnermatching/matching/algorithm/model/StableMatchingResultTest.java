package pl.krystian.businesspartnermatching.matching.algorithm.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class StableMatchingResultTest {

    @Test
    void shouldCreateEmptyResult() {
        // when
        StableMatchingResult<String, String> result =
                new StableMatchingResult<>(List.of());

        // then
        assertThat(result.isEmpty()).isTrue();
        assertThat(result.size()).isZero();
        assertThat(result.matches()).isEmpty();
    }

    @Test
    void shouldCreateResultContainingMatches() {
        // given
        StableMatch<String, String> firstMatch =
                new StableMatch<>("need-1", "offer-1");

        StableMatch<String, String> secondMatch =
                new StableMatch<>("need-2", "offer-2");

        // when
        StableMatchingResult<String, String> result =
                new StableMatchingResult<>(
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
        StableMatch<String, String> firstMatch =
                new StableMatch<>("need-1", "offer-1");

        StableMatch<String, String> secondMatch =
                new StableMatch<>("need-1", "offer-2");

        StableMatch<String, String> thirdMatch =
                new StableMatch<>("need-2", "offer-3");

        StableMatchingResult<String, String> result =
                new StableMatchingResult<>(
                        List.of(
                                firstMatch,
                                secondMatch,
                                thirdMatch
                        )
                );

        // when
        List<StableMatch<String, String>> matches =
                result.matchesForLeftParticipant("need-1");

        // then
        assertThat(matches)
                .containsExactly(firstMatch, secondMatch);
    }

    @Test
    void shouldReturnMatchesForRightParticipant() {
        // given
        StableMatch<String, String> firstMatch =
                new StableMatch<>("need-1", "offer-1");

        StableMatch<String, String> secondMatch =
                new StableMatch<>("need-2", "offer-1");

        StableMatch<String, String> thirdMatch =
                new StableMatch<>("need-3", "offer-2");

        StableMatchingResult<String, String> result =
                new StableMatchingResult<>(
                        List.of(
                                firstMatch,
                                secondMatch,
                                thirdMatch
                        )
                );

        // when
        List<StableMatch<String, String>> matches =
                result.matchesForRightParticipant("offer-1");

        // then
        assertThat(matches)
                .containsExactly(firstMatch, secondMatch);
    }

    @Test
    void shouldReturnEmptyListWhenLeftParticipantHasNoMatches() {
        // given
        StableMatchingResult<String, String> result =
                new StableMatchingResult<>(
                        List.of(
                                new StableMatch<>(
                                        "need-1",
                                        "offer-1"
                                )
                        )
                );

        // when
        List<StableMatch<String, String>> matches =
                result.matchesForLeftParticipant("need-2");

        // then
        assertThat(matches).isEmpty();
    }

    @Test
    void shouldReturnTrueWhenResultContainsMatch() {
        // given
        StableMatchingResult<String, String> result =
                new StableMatchingResult<>(
                        List.of(
                                new StableMatch<>(
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
        StableMatchingResult<String, String> result =
                new StableMatchingResult<>(
                        List.of(
                                new StableMatch<>(
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
        List<StableMatch<String, String>> matches =
                new ArrayList<>();

        matches.add(
                new StableMatch<>(
                        "need-1",
                        "offer-1"
                )
        );

        StableMatchingResult<String, String> result =
                new StableMatchingResult<>(matches);

        // when
        matches.add(
                new StableMatch<>(
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
                .isThrownBy(() -> new StableMatchingResult<>(
                        List.of(
                                new StableMatch<>(
                                        "need-1",
                                        "offer-1"
                                ),
                                new StableMatch<>(
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
                        new StableMatchingResult<String, String>(null)
                )
                .withMessage(
                        "Stable matches cannot be null"
                );
    }

    @Test
    void shouldRejectListContainingNullMatch() {
        // given
        List<StableMatch<String, String>> matches =
                new ArrayList<>();

        matches.add(
                new StableMatch<>(
                        "need-1",
                        "offer-1"
                )
        );
        matches.add(null);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->
                        new StableMatchingResult<>(matches)
                )
                .withMessage(
                        "Stable matches cannot contain null"
                );
    }

    @Test
    void shouldRejectDuplicateMatches() {
        // given
        StableMatch<String, String> match =
                new StableMatch<>(
                        "need-1",
                        "offer-1"
                );

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->
                        new StableMatchingResult<>(
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
        StableMatchingResult<String, String> result =
                new StableMatchingResult<>(List.of());

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
        StableMatchingResult<String, String> result =
                new StableMatchingResult<>(List.of());

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
        StableMatchingResult<String, String> result =
                new StableMatchingResult<>(List.of());

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
        StableMatchingResult<String, String> result =
                new StableMatchingResult<>(List.of());

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
