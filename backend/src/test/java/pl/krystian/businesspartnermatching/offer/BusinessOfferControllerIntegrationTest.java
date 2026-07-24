package pl.krystian.businesspartnermatching.offer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import pl.krystian.businesspartnermatching.IntegrationTest;
import pl.krystian.businesspartnermatching.catalog.industry.model.entity.Industry;
import pl.krystian.businesspartnermatching.catalog.industry.repository.IndustryRepository;
import pl.krystian.businesspartnermatching.catalog.specialization.model.entity.Specialization;
import pl.krystian.businesspartnermatching.catalog.specialization.repository.SpecializationRepository;
import pl.krystian.businesspartnermatching.company.model.entity.Company;
import pl.krystian.businesspartnermatching.company.repository.CompanyRepository;
import pl.krystian.businesspartnermatching.offer.repository.BusinessOfferRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BusinessOfferControllerIntegrationTest
        extends IntegrationTest {

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BusinessOfferRepository businessOfferRepository;

    @Test
    void shouldCreateBusinessOffer() throws Exception {
        long initialNeedCount =
                businessOfferRepository.count();

        Industry industry = industryRepository.save(
                new Industry(
                        "TEST_OFFER_IT",
                        "Test Offer Information Technology"
                )
        );

        Specialization specialization =
                specializationRepository.save(
                        new Specialization(
                                industry,
                                "TEST_OFFER_JAVA",
                                "Test Offer Java Development"
                        )
                );

        Company company = companyRepository.save(
                new Company(
                        "Integration Test Offer Company",
                        "Company created for an offer test",
                        industry,
                        Set.of(specialization),
                        "Poland",
                        "Krakow",
                        new BigDecimal("50.064650"),
                        new BigDecimal("19.944980"),
                        LocalDate.of(2020, 1, 1),
                        "Software development"
                )
        );

        String requestBody = """
                {
                  "title": "Tworzenie aplikacji mobilnych",
                  "description": "Oferujemy kompleksową realizację aplikacji.",
                  "cooperationType": "OUTSOURCING",
                  "offeredSpecializationIds": [%d],
                  "priceRange": {
                    "min": 25000,
                    "max": 50000,
                    "currency": "PLN"
                  },
                  "availabilityPeriod": {
                    "from": "2026-08-01",
                    "until": "2027-03-31"
                  },
                  "serviceRadiusKm": 300,
                  "maxPartners": 3
                }
                """.formatted(specialization.getId());

        mockMvc.perform(
                        post(
                                "/api/companies/{companyId}/offers",
                                company.getId()
                        )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(
                        jsonPath("$.companyId")
                                .value(company.getId())
                )
                .andExpect(
                        jsonPath("$.title")
                                .value(
                                        "Tworzenie aplikacji mobilnych"
                                )
                )
                .andExpect(
                        jsonPath("$.cooperationType")
                                .value("OUTSOURCING")
                )
                .andExpect(
                        jsonPath(
                                "$.offeredSpecializations[0].id"
                        ).value(specialization.getId())
                )
                .andExpect(jsonPath("$.priceRange.min")
                        .value(25000))
                .andExpect(jsonPath("$.priceRange.max")
                        .value(50000))
                .andExpect(jsonPath("$.priceRange.currency")
                        .value("PLN"))
                .andExpect(jsonPath("$.availabilityPeriod.from")
                        .value("2026-08-01"))
                .andExpect(jsonPath("$.availabilityPeriod.until")
                        .value("2027-03-31"))
                .andExpect(
                        jsonPath("$.serviceRadiusKm")
                                .value(300)
                )
                .andExpect(
                        jsonPath("$.maxPartners")
                                .value(3)
                )
                .andExpect(
                        jsonPath("$.active")
                                .value(true)
                );


        assertThat(businessOfferRepository.count())
                .isEqualTo(initialNeedCount + 1);
    }
}
