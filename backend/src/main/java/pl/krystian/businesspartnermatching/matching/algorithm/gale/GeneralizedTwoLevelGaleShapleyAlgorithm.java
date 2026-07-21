package pl.krystian.businesspartnermatching.matching.algorithm.gale;

import pl.krystian.businesspartnermatching.matching.algorithm.PopularMatchingAlgorithm;
import pl.krystian.businesspartnermatching.matching.algorithm.model.MatchingProblem;
import pl.krystian.businesspartnermatching.matching.algorithm.model.PopularMatchingResult;

public class GeneralizedTwoLevelGaleShapleyAlgorithm<L, R>
        implements PopularMatchingAlgorithm<L, R> {

    @Override
    public PopularMatchingResult<L, R> match(
            MatchingProblem<L, R> problem
    ) {
        throw new UnsupportedOperationException(
                "Not implemented yet"
        );
    }
}
