package pl.krystian.businesspartnermatching.company.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.krystian.businesspartnermatching.company.service.CompanyService;
import pl.krystian.businesspartnermatching.company.model.dto.CompanyResponse;
import pl.krystian.businesspartnermatching.company.model.dto.CreateCompanyRequest;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public CompanyResponse createCompany(@Valid @RequestBody CreateCompanyRequest request) {
        return companyService.createCompany(request);
    }

    @GetMapping("/{companyId}")
    public CompanyResponse getCompanyById(@PathVariable Long companyId) {
        return companyService.getCompanyById(companyId);
    }
}
