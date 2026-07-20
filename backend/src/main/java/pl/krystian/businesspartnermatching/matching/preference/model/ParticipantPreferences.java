package pl.krystian.businesspartnermatching.matching.preference.model;

import java.util.List;
import java.util.Objects;

public record ParticipantPreferences<P, C>(
        P participant,
        List<C> preferredCandidates
) {

    public ParticipantPreferences {
        Objects.requireNonNull(
                participant,
                "Preference participant cannot be null"
        );

        Objects.requireNonNull(
                preferredCandidates,
                "Preferred candidates cannot be null"
        );

        if (preferredCandidates.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(
                    "Preferred candidates cannot contain null"
            );
        }

        preferredCandidates = List.copyOf(
                preferredCandidates
        );
    }

    public boolean contains(C candidate) {
        return preferredCandidates.contains(candidate);
    }

    public int positionOf(C candidate) {
        int position =
                preferredCandidates.indexOf(candidate);

        if (position < 0) {
            throw new IllegalArgumentException(
                    "Candidate is not present in participant preferences"
            );
        }

        return position;
    }

    public boolean prefers(
            C firstCandidate,
            C secondCandidate
    ) {
        return positionOf(firstCandidate)
                < positionOf(secondCandidate);
    }

    public boolean isEmpty() {
        return preferredCandidates.isEmpty();
    }

    public int size() {
        return preferredCandidates.size();
    }
}
