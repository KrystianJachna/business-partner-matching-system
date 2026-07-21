package pl.krystian.businesspartnermatching.matching.algorithm.model;

import java.util.Objects;

public record StableMatch<L, R>(
        L leftParticipant,
        R rightParticipant
) {

    public StableMatch {
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
