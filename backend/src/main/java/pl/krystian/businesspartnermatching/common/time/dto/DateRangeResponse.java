package pl.krystian.businesspartnermatching.common.time.model.dto;

import pl.krystian.businesspartnermatching.common.time.DateRange;

import java.time.LocalDate;

public record DateRangeResponse(
        LocalDate from,
        LocalDate until
) {

    public static DateRangeResponse from(DateRange dateRange) {
        if (dateRange == null) {
            return null;
        }

        return new DateRangeResponse(
                dateRange.getFrom(),
                dateRange.getUntil()
        );
    }
}
