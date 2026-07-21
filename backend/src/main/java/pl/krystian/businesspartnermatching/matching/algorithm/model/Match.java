package pl.krystian.businesspartnermatching.matching.algorithm.model;

import java.util.Objects;

public record Match<L, R>(
        L leftParticipant,
        R rightParticipant
) {

    public Match {
        Objects.requireNonNull(
                leftParticipant,
                "Left participant cannot be null"
        );
        Objects.requireNonNull(
                rightParticipant,
                "Right participant cannot be null"
        );
    }
}
