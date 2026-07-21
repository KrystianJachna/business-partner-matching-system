package pl.krystian.businesspartnermatching.matching.algorithm.model;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public record ParticipantCapacitySet<P>(
        Map<P, Integer> capacities
) {

    public ParticipantCapacitySet {
        Objects.requireNonNull(
                capacities,
                "Participant capacities cannot be null"
        );

        if (capacities.keySet().stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(
                    "Participant capacities cannot contain null participants"
            );
        }

        if (capacities.values().stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(
                    "Participant capacities cannot contain null capacities"
            );
        }

        if (capacities.values().stream().anyMatch(capacity -> capacity <= 0)) {
            throw new IllegalArgumentException(
                    "Participant capacity must be greater than zero"
            );
        }

        capacities = Map.copyOf(capacities);
    }

    public int capacityOf(P participant) {
        Objects.requireNonNull(
                participant,
                "Participant cannot be null"
        );

        Integer capacity = capacities.get(participant);

        if (capacity == null) {
            throw new IllegalArgumentException(
                    "Capacity is not defined for participant"
            );
        }

        return capacity;
    }

    public boolean containsParticipant(P participant) {
        Objects.requireNonNull(
                participant,
                "Participant cannot be null"
        );

        return capacities.containsKey(participant);
    }

    public Set<P> participants() {
        return capacities.keySet();
    }

    public boolean isEmpty() {
        return capacities.isEmpty();
    }

    public int size() {
        return capacities.size();
    }
}
