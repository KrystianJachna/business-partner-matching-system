package pl.krystian.businesspartnermatching.need;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.krystian.businesspartnermatching.need.dto.BusinessNeedResponse;
import pl.krystian.businesspartnermatching.need.dto.CreateBusinessNeedRequest;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BusinessNeedController {

    private final BusinessNeedService businessNeedService;

    @PostMapping("/companies/{companyId}/needs")
    @ResponseStatus(HttpStatus.CREATED)
    public BusinessNeedResponse createBusinessNeed(
            @PathVariable Long companyId,
            @Valid @RequestBody CreateBusinessNeedRequest request
    ) {
        return businessNeedService.createBusinessNeed(companyId, request);
    }

    @GetMapping("/business-needs/{businessNeedId}")
    public BusinessNeedResponse getBusinessNeedById(
            @PathVariable Long businessNeedId
    ) {
        return businessNeedService.getBusinessNeedById(businessNeedId);
    }

    @GetMapping("/companies/{companyId}/needs")
    public List<BusinessNeedResponse> getBusinessNeedsByCompanyId(
            @PathVariable Long companyId
    ) {
        return businessNeedService.getBusinessNeedsByCompanyId(companyId);
    }
}