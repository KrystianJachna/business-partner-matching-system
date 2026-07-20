package pl.krystian.businesspartnermatching.matching.preference;

import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;

public record ScoredOffer(
        BusinessOffer offer,
        BigDecimal score
) {
}
