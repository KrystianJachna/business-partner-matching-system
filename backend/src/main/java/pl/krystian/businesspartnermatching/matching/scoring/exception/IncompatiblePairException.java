package pl.krystian.businesspartnermatching.matching.scoring.exception;

import lombok.Getter;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;

import java.util.List;

@Getter
public class IncompatiblePairException extends RuntimeException {

    private final List<CompatibilityFailureReason> failureReasons;

    public IncompatiblePairException(
            List<CompatibilityFailureReason> failureReasons
    ) {
        super(
                "Cannot calculate score for incompatible pair: "
                        + failureReasons
        );

        this.failureReasons = List.copyOf(failureReasons);
    }

}
