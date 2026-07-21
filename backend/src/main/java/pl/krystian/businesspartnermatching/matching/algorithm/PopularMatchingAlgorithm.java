package pl.krystian.businesspartnermatching.matching.algorithm;

import pl.krystian.businesspartnermatching.matching.algorithm.model.MatchingProblem;
import pl.krystian.businesspartnermatching.matching.algorithm.model.PopularMatchingResult;

public interface PopularMatchingAlgorithm<L, R> {

    PopularMatchingResult<L, R> match(
            MatchingProblem<L, R> problem
    );
}
