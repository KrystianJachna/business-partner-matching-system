package pl.krystian.businesspartnermatching.catalog.industry;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.krystian.businesspartnermatching.catalog.industry.dto.IndustryResponse;

import java.util.List;

@RestController
@RequestMapping("/api/industries")
@RequiredArgsConstructor
public class IndustryController {

    private final IndustryService industryService;

    @GetMapping
    public List<IndustryResponse> getActiveIndustries() {
        return industryService.getAllActiveIndustries();
    }
}
