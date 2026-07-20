package pl.krystian.businesspartnermatching.matching.preference.profile;

import pl.krystian.businesspartnermatching.matching.criterion.MatchingCriterion;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public record PreferenceProfile(
        Map<MatchingCriterion, BigDecimal> weights
) {

    private static final BigDecimal EXPECTED_SUM = BigDecimal.ONE;

    public PreferenceProfile {
        if (weights == null || weights.isEmpty()) {
            throw new IllegalArgumentException(
                    "Preference profile must contain weights"
            );
        }

        EnumMap<MatchingCriterion, BigDecimal> copiedWeights =
                new EnumMap<>(MatchingCriterion.class);

        copiedWeights.putAll(weights);

        validate(copiedWeights);

        weights = Map.copyOf(copiedWeights);
    }

    public BigDecimal weightOf(MatchingCriterion criterion) {
        Objects.requireNonNull(
                criterion,
                "Matching criterion cannot be null"
        );

        return weights.getOrDefault(
                criterion,
                BigDecimal.ZERO
        );
    }

    private static void validate(
            Map<MatchingCriterion, BigDecimal> weights
    ) {
        weights.forEach((criterion, weight) -> {
            if (criterion == null) {
                throw new IllegalArgumentException(
                        "Matching criterion cannot be null"
                );
            }

            if (weight == null) {
                throw new IllegalArgumentException(
                        "Criterion weight cannot be null"
                );
            }

            if (weight.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException(
                        "Criterion weight cannot be negative"
                );
            }
        });

        BigDecimal sum = weights.values()
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sum.compareTo(EXPECTED_SUM) != 0) {
            throw new IllegalArgumentException(
                    "Criterion weights must sum to 1"
            );
        }
    }
}
