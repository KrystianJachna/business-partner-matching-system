package pl.krystian.businesspartnermatching.common.time;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateRange {

    private LocalDate from;
    private LocalDate until;

    public DateRange(LocalDate from, LocalDate until) {
        validate(from, until);

        this.from = from;
        this.until = until;
    }

    private void validate(LocalDate from, LocalDate until) {
        if (from == null || until == null) {
            throw new IllegalArgumentException(
                    "Both start date and end date must be provided"
            );
        }

        if (from.isAfter(until)) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date"
            );
        }
    }
}