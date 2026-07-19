package pl.krystian.businesspartnermatching.matching.compatibility;

import java.util.List;
import java.util.Objects;

public record CompatibilityResult(
        List<CompatibilityFailureReason> failureReasons
) {

    public CompatibilityResult {
        Objects.requireNonNull(
                failureReasons,
                "Failure reasons cannot be null"
        );

        failureReasons = List.copyOf(failureReasons);
    }

    public boolean compatible() {
        return failureReasons.isEmpty();
    }
}
