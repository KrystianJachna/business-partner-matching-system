package pl.krystian.businesspartnermatching.catalog.specialization.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.krystian.businesspartnermatching.catalog.specialization.model.dto.SpecializationGroupResponse;
import pl.krystian.businesspartnermatching.catalog.specialization.model.dto.SpecializationResponse;
import pl.krystian.businesspartnermatching.catalog.specialization.service.SpecializationService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SpecializationController {

    private final SpecializationService specializationService;

    @GetMapping("/industries/{industryId}/specializations")
    public List<SpecializationResponse>
    getActiveSpecializationsByIndustry(
            @PathVariable Long industryId
    ) {
        return specializationService
                .getActiveSpecializationsByIndustry(
                        industryId
                );
    }

    @GetMapping("/specializations/grouped")
    public List<SpecializationGroupResponse>
    getAllActiveSpecializationsGroupedByIndustry() {
        return specializationService
                .getAllActiveSpecializationsGroupedByIndustry();
    }
}
