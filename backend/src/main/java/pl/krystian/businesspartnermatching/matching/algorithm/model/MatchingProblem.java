package pl.krystian.businesspartnermatching.matching.algorithm.model;

import pl.krystian.businesspartnermatching.matching.preference.model.ParticipantPreferenceSet;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public record MatchingProblem<L, R>(
        ParticipantPreferenceSet<L, R> leftPreferences,
        ParticipantPreferenceSet<R, L> rightPreferences,
        ParticipantCapacitySet<L> leftCapacities,
        ParticipantCapacitySet<R> rightCapacities
) {

    public MatchingProblem {
        Objects.requireNonNull(
                leftPreferences,
                "Left preferences cannot be null"
        );
        Objects.requireNonNull(
                rightPreferences,
                "Right preferences cannot be null"
        );
        Objects.requireNonNull(
                leftCapacities,
                "Left capacities cannot be null"
        );
        Objects.requireNonNull(
                rightCapacities,
                "Right capacities cannot be null"
        );

        validateParticipants(
                leftPreferences.participants(),
                leftCapacities.participants(),
                "Left"
        );

        validateParticipants(
                rightPreferences.participants(),
                rightCapacities.participants(),
                "Right"
        );
    }

    private static <P> void validateParticipants(
            Collection<P> preferenceParticipants,
            Collection<P> capacityParticipants,
            String side
    ) {
        Set<P> preferenceParticipantSet =
                Set.copyOf(preferenceParticipants);

        Set<P> capacityParticipantSet =
                Set.copyOf(capacityParticipants);

        if (!preferenceParticipantSet.equals(capacityParticipantSet)) {
            throw new IllegalArgumentException(
                    side
                            + " preference participants and "
                            + side.toLowerCase()
                            + " capacity participants must contain exactly the same participants"
            );
        }
    }
}
