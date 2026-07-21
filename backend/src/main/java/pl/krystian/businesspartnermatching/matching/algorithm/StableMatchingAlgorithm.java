package pl.krystian.businesspartnermatching.matching.algorithm;

import pl.krystian.businesspartnermatching.matching.algorithm.model.MatchingProblem;
import pl.krystian.businesspartnermatching.matching.algorithm.model.StableMatchingResult;

public interface StableMatchingAlgorithm<L, R> {

    StableMatchingResult<L, R> match(
            MatchingProblem<L, R> problem
    );
}
