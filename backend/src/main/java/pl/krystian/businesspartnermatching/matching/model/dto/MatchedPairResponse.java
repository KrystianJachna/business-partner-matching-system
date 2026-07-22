package pl.krystian.businesspartnermatching.matching.model.dto;

import pl.krystian.businesspartnermatching.matching.algorithm.model.Match;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

public record MatchedPairResponse(
        Long needId,
        String needTitle,
        Long needCompanyId,
        Long offerId,
        String offerTitle,
        Long offerCompanyId
) {

    public static MatchedPairResponse from(
            Match<BusinessNeed, BusinessOffer> match
    ) {
        BusinessNeed need =
                match.leftParticipant();

        BusinessOffer offer =
                match.rightParticipant();

        return new MatchedPairResponse(
                need.getId(),
                need.getTitle(),
                need.getCompany().getId(),
                offer.getId(),
                offer.getTitle(),
                offer.getCompany().getId()
        );
    }
}
