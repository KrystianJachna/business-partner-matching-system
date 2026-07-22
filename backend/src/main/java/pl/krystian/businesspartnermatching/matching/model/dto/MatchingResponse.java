package pl.krystian.businesspartnermatching.matching.model.dto;

import pl.krystian.businesspartnermatching.matching.algorithm.model.PopularMatchingResult;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.util.List;
import java.util.Objects;

public record MatchingResponse(
        int matchCount,
        List<MatchedPairResponse> matches
) {

    public MatchingResponse {
        Objects.requireNonNull(
                matches,
                "Matches cannot be null"
        );

        matches = List.copyOf(matches);

        if (matchCount != matches.size()) {
            throw new IllegalArgumentException(
                    "Match count must be equal to the number of matches"
            );
        }
    }

    public static MatchingResponse from(
            PopularMatchingResult<BusinessNeed, BusinessOffer> result
    ) {
        Objects.requireNonNull(
                result,
                "Matching result cannot be null"
        );

        List<MatchedPairResponse> matches =
                result.matches()
                        .stream()
                        .map(MatchedPairResponse::from)
                        .toList();

        return new MatchingResponse(
                matches.size(),
                matches
        );
    }
}
