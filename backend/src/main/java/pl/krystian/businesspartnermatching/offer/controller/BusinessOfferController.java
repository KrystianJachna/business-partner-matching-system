package pl.krystian.businesspartnermatching.offer.controller;

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
import pl.krystian.businesspartnermatching.offer.service.BusinessOfferService;
import pl.krystian.businesspartnermatching.offer.model.dto.BusinessOfferResponse;
import pl.krystian.businesspartnermatching.offer.model.dto.CreateBusinessOfferRequest;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BusinessOfferController {

    private final BusinessOfferService businessOfferService;

    @PostMapping("/companies/{companyId}/offers")
    @ResponseStatus(HttpStatus.CREATED)
    public BusinessOfferResponse createBusinessOffer(
            @PathVariable Long companyId,
            @Valid @RequestBody CreateBusinessOfferRequest request
    ) {
        return businessOfferService.createBusinessOffer(
                companyId,
                request
        );
    }

    @GetMapping("/business-offers/{businessOfferId}")
    public BusinessOfferResponse getBusinessOfferById(
            @PathVariable Long businessOfferId
    ) {
        return businessOfferService.getBusinessOfferById(
                businessOfferId
        );
    }

    @GetMapping("/companies/{companyId}/offers")
    public List<BusinessOfferResponse> getBusinessOffersByCompanyId(
            @PathVariable Long companyId
    ) {
        return businessOfferService.getBusinessOffersByCompanyId(
                companyId
        );
    }
}
