package pl.krystian.businesspartnermatching.matching.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.krystian.businesspartnermatching.matching.algorithm.MatchingAlgorithmType;
import pl.krystian.businesspartnermatching.matching.model.dto.MatchingResponse;
import pl.krystian.businesspartnermatching.matching.service.BusinessMatchingService;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingScoreCalculator;
import pl.krystian.businesspartnermatching.matching.scoring.weights.ScoringWeightsProvider;

@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final BusinessMatchingService businessMatchingService;
    private final MatchingScoreCalculator matchingScoreCalculator;
    private final ScoringWeightsProvider scoringWeightsProvider;

    @PostMapping
    public MatchingResponse runMatching(
            @RequestParam(defaultValue = "STABLE")
            MatchingAlgorithmType algorithmType
    ) {
        return MatchingResponse.from(
                businessMatchingService.match(
                        algorithmType
                ),
                matchingScoreCalculator,
                scoringWeightsProvider
        );
    }
}
