package pl.krystian.businesspartnermatching.matching.preference;

import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;

import java.math.BigDecimal;

public record ScoredNeed(
        BusinessNeed need,
        BigDecimal score
) {
}
