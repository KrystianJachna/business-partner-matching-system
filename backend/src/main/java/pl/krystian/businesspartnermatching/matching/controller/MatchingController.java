package pl.krystian.businesspartnermatching.matching.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.krystian.businesspartnermatching.matching.model.dto.MatchingResponse;
import pl.krystian.businesspartnermatching.matching.service.BusinessMatchingService;

@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final BusinessMatchingService businessMatchingService;

    @PostMapping
    public MatchingResponse runMatching() {
        return MatchingResponse.from(
                businessMatchingService.match()
        );
    }
}
