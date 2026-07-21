package pl.krystian.businesspartnermatching.matching.algorithm.model;

import java.util.List;
import java.util.Objects;

public record PopularMatchingResult<L, R>(
        List<Match<L, R>> matches
) {

    public PopularMatchingResult {
        Objects.requireNonNull(
                matches,
                "Stable matches cannot be null"
        );

        if (matches.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(
                    "Stable matches cannot contain null"
            );
        }

        if (matches.stream().distinct().count() != matches.size()) {
            throw new IllegalArgumentException(
                    "Stable matches cannot contain duplicates"
            );
        }

        matches = List.copyOf(matches);
    }

    public List<Match<L, R>> matchesForLeftParticipant(
            L leftParticipant
    ) {
        Objects.requireNonNull(
                leftParticipant,
                "Left participant cannot be null"
        );

        return matches.stream()
                .filter(match -> match.leftParticipant().equals(leftParticipant))
                .toList();
    }

    public List<Match<L, R>> matchesForRightParticipant(
            R rightParticipant
    ) {
        Objects.requireNonNull(
                rightParticipant,
                "Right participant cannot be null"
        );

        return matches.stream()
                .filter(match -> match.rightParticipant().equals(rightParticipant))
                .toList();
    }

    public boolean contains(
            L leftParticipant,
            R rightParticipant
    ) {
        Objects.requireNonNull(
                leftParticipant,
                "Left participant cannot be null"
        );
        Objects.requireNonNull(
                rightParticipant,
                "Right participant cannot be null"
        );

        return matches.contains(
                new Match<>(
                        leftParticipant,
                        rightParticipant
                )
        );
    }

    public boolean isEmpty() {
        return matches.isEmpty();
    }

    public int size() {
        return matches.size();
    }
}
