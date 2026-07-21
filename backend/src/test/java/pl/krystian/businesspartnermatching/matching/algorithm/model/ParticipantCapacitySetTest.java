package pl.krystian.businesspartnermatching.matching.algorithm.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParticipantCapacitySetTest {

    @Test
    void shouldCreateEmptyCapacitySet() {
        // when
        ParticipantCapacitySet<String> capacitySet =
                new ParticipantCapacitySet<>(Map.of());

        // then
        assertThat(capacitySet.isEmpty()).isTrue();
        assertThat(capacitySet.size()).isZero();
        assertThat(capacitySet.participants()).isEmpty();
        assertThat(capacitySet.capacities()).isEmpty();
    }

    @Test
    void shouldCreateCapacitySetContainingParticipants() {
        // given
        Map<String, Integer> capacities = Map.of(
                "need-1", 2,
                "need-2", 1
        );

        // when
        ParticipantCapacitySet<String> capacitySet =
                new ParticipantCapacitySet<>(capacities);

        // then
        assertThat(capacitySet.isEmpty()).isFalse();
        assertThat(capacitySet.size()).isEqualTo(2);

        assertThat(capacitySet.participants())
                .containsExactlyInAnyOrder(
                        "need-1",
                        "need-2"
                );

        assertThat(capacitySet.capacities())
                .containsEntry("need-1", 2)
                .containsEntry("need-2", 1);
    }

    @Test
    void shouldReturnCapacityForParticipant() {
        // given
        ParticipantCapacitySet<String> capacitySet =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "need-1", 3
                        )
                );

        // when
        int capacity = capacitySet.capacityOf("need-1");

        // then
        assertThat(capacity).isEqualTo(3);
    }

    @Test
    void shouldReturnTrueWhenParticipantExists() {
        // given
        ParticipantCapacitySet<String> capacitySet =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "need-1", 2
                        )
                );

        // when
        boolean containsParticipant =
                capacitySet.containsParticipant("need-1");

        // then
        assertThat(containsParticipant).isTrue();
    }

    @Test
    void shouldReturnFalseWhenParticipantDoesNotExist() {
        // given
        ParticipantCapacitySet<String> capacitySet =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "need-1", 2
                        )
                );

        // when
        boolean containsParticipant =
                capacitySet.containsParticipant("need-2");

        // then
        assertThat(containsParticipant).isFalse();
    }

    @Test
    void shouldCreateDefensiveCopyOfCapacities() {
        // given
        Map<String, Integer> capacities = new HashMap<>();
        capacities.put("need-1", 2);

        ParticipantCapacitySet<String> capacitySet =
                new ParticipantCapacitySet<>(capacities);

        // when
        capacities.put("need-2", 3);

        // then
        assertThat(capacitySet.capacities())
                .containsOnlyKeys("need-1");

        assertThat(capacitySet.size()).isEqualTo(1);
    }

    @Test
    void shouldExposeUnmodifiableCapacities() {
        // given
        ParticipantCapacitySet<String> capacitySet =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "need-1", 2
                        )
                );

        // then
        assertThatThrownBy(() ->
                capacitySet.capacities().put(
                        "need-2",
                        1
                )
        ).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldRejectNullCapacitiesMap() {
        assertThatNullPointerException()
                .isThrownBy(() ->
                        new ParticipantCapacitySet<String>(null)
                )
                .withMessage(
                        "Participant capacities cannot be null"
                );
    }

    @Test
    void shouldRejectNullParticipant() {
        // given
        Map<String, Integer> capacities = new HashMap<>();
        capacities.put(null, 1);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->
                        new ParticipantCapacitySet<>(capacities)
                )
                .withMessage(
                        "Participant capacities cannot contain null participants"
                );
    }

    @Test
    void shouldRejectNullCapacity() {
        // given
        Map<String, Integer> capacities = new HashMap<>();
        capacities.put("need-1", null);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->
                        new ParticipantCapacitySet<>(capacities)
                )
                .withMessage(
                        "Participant capacities cannot contain null capacities"
                );
    }

    @Test
    void shouldRejectZeroCapacity() {
        // given
        Map<String, Integer> capacities = Map.of(
                "need-1", 0
        );

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->
                        new ParticipantCapacitySet<>(capacities)
                )
                .withMessage(
                        "Participant capacity must be greater than zero"
                );
    }

    @Test
    void shouldRejectNegativeCapacity() {
        // given
        Map<String, Integer> capacities = Map.of(
                "need-1", -1
        );

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->
                        new ParticipantCapacitySet<>(capacities)
                )
                .withMessage(
                        "Participant capacity must be greater than zero"
                );
    }

    @Test
    void shouldRejectNullParticipantWhenGettingCapacity() {
        // given
        ParticipantCapacitySet<String> capacitySet =
                new ParticipantCapacitySet<>(Map.of());

        // then
        assertThatNullPointerException()
                .isThrownBy(() ->
                        capacitySet.capacityOf(null)
                )
                .withMessage(
                        "Participant cannot be null"
                );
    }

    @Test
    void shouldRejectParticipantWithoutDefinedCapacity() {
        // given
        ParticipantCapacitySet<String> capacitySet =
                new ParticipantCapacitySet<>(
                        Map.of(
                                "need-1", 2
                        )
                );

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->
                        capacitySet.capacityOf("need-2")
                )
                .withMessage(
                        "Capacity is not defined for participant"
                );
    }

    @Test
    void shouldRejectNullParticipantWhenCheckingPresence() {
        // given
        ParticipantCapacitySet<String> capacitySet =
                new ParticipantCapacitySet<>(Map.of());

        // then
        assertThatNullPointerException()
                .isThrownBy(() ->
                        capacitySet.containsParticipant(null)
                )
                .withMessage(
                        "Participant cannot be null"
                );
    }
}
