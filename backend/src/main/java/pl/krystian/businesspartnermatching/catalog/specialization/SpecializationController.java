package pl.krystian.businesspartnermatching.catalog.specialization;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.krystian.businesspartnermatching.catalog.specialization.dto.SpecializationResponse;

import java.util.List;


@RestController
@RequestMapping("/api/industries/{industryId}/specializations")
@RequiredArgsConstructor
public class SpecializationController {

    private final SpecializationService specializationService;

    @GetMapping
    public List<SpecializationResponse> getActiveSpecializationsByIndustry(@PathVariable Long industryId) {
        return specializationService.getActiveSpecializationsByIndustry(industryId);
    }
}
