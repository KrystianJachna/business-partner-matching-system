package pl.krystian.businesspartnermatching.matching.preference.model;

import java.math.BigDecimal;
import java.util.Objects;

public record Preference<T>(
        T candidate,
        BigDecimal score
) {

    public Preference {
        Objects.requireNonNull(
                candidate,
                "Preference candidate cannot be null"
        );

        Objects.requireNonNull(
                score,
                "Preference score cannot be null"
        );

        if (score.compareTo(BigDecimal.ZERO) < 0
                || score.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException(
                    "Preference score must be between 0 and 1"
            );
        }
    }
}
