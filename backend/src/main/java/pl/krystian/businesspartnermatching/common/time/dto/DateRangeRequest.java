package pl.krystian.businesspartnermatching.common.time.model.dto;

import jakarta.validation.constraints.NotNull;
import pl.krystian.businesspartnermatching.common.time.DateRange;

import java.time.LocalDate;

public record DateRangeRequest(

        @NotNull
        LocalDate from,

        @NotNull
        LocalDate until
) {

    private DateRange toDateRange() {
        return new DateRange(
                from,
                until
        );
    }

    public static DateRange fromNullable(
            DateRangeRequest request
    ) {
        return request == null
                ? null
                : request.toDateRange();
    }
}
