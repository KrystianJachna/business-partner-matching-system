package pl.krystian.businesspartnermatching.need.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DateRangeRequest(

        @NotNull
        LocalDate from,

        @NotNull
        LocalDate until
) {
}