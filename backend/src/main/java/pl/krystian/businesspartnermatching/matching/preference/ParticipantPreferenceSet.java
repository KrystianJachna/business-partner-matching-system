package pl.krystian.businesspartnermatching.matching.preference;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public record ParticipantPreferenceSet<P, C>(
        Map<P, ParticipantPreferences<P, C>> preferencesByParticipant
) {

    public ParticipantPreferenceSet {
        Objects.requireNonNull(
                preferencesByParticipant,
                "Participant preferences map cannot be null"
        );

        if (preferencesByParticipant
                .entrySet()
                .stream()
                .anyMatch(entry ->
                        entry.getKey() == null
                                || entry.getValue() == null
                )) {
            throw new IllegalArgumentException(
                    "Participant preferences map cannot contain null keys or values"
            );
        }

        boolean containsMismatchedParticipant =
                preferencesByParticipant
                        .entrySet()
                        .stream()
                        .anyMatch(entry ->
                                !entry.getKey().equals(
                                        entry.getValue().participant()
                                )
                        );

        if (containsMismatchedParticipant) {
            throw new IllegalArgumentException(
                    "Map key must match the participant stored in preferences"
            );
        }

        preferencesByParticipant = Map.copyOf(
                new LinkedHashMap<>(preferencesByParticipant)
        );
    }

    public static <P, C> ParticipantPreferenceSet<P, C> from(
            Collection<ParticipantPreferences<P, C>> preferences
    ) {
        Objects.requireNonNull(
                preferences,
                "Participant preferences collection cannot be null"
        );

        if (preferences.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(
                    "Participant preferences collection cannot contain null"
            );
        }

        Map<P, ParticipantPreferences<P, C>> preferencesByParticipant =
                new LinkedHashMap<>();

        for (ParticipantPreferences<P, C> participantPreferences
                : preferences) {
            ParticipantPreferences<P, C> previous =
                    preferencesByParticipant.put(
                            participantPreferences.participant(),
                            participantPreferences
                    );

            if (previous != null) {
                throw new IllegalArgumentException(
                        "Participant preferences collection cannot contain duplicate participants"
                );
            }
        }

        return new ParticipantPreferenceSet<>(
                preferencesByParticipant
        );
    }

    public Optional<ParticipantPreferences<P, C>> findFor(
            P participant
    ) {
        Objects.requireNonNull(
                participant,
                "Participant cannot be null"
        );

        return Optional.ofNullable(
                preferencesByParticipant.get(participant)
        );
    }

    public ParticipantPreferences<P, C> getFor(
            P participant
    ) {
        return findFor(participant)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Preferences for participant are not available"
                        )
                );
    }

    public boolean containsParticipant(P participant) {
        Objects.requireNonNull(
                participant,
                "Participant cannot be null"
        );

        return preferencesByParticipant.containsKey(participant);
    }

    public Collection<P> participants() {
        return preferencesByParticipant.keySet();
    }

    public Collection<ParticipantPreferences<P, C>> preferences() {
        return preferencesByParticipant.values();
    }

    public int size() {
        return preferencesByParticipant.size();
    }

    public boolean isEmpty() {
        return preferencesByParticipant.isEmpty();
    }
}
