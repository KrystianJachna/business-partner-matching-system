package pl.krystian.businesspartnermatching.need;

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
import pl.krystian.businesspartnermatching.need.repository.BusinessNeedRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class BusinessNeedControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BusinessNeedRepository businessNeedRepository;

    @Test
    void shouldCreateBusinessNeed() throws Exception {
        Industry industry = industryRepository.save(
                new Industry(
                        "TEST_IT",
                        "Test Information Technology"
                )
        );

        Specialization specialization = specializationRepository.save(
                new Specialization(
                        industry,
                        "TEST_JAVA",
                        "Test Java Development"
                )
        );

        Company company = companyRepository.save(
                new Company(
                        "Integration Test Company",
                        "Company created for an integration test",
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
                  "title": "Partner do wykonania aplikacji mobilnej",
                  "description": "Poszukujemy firmy do realizacji aplikacji.",
                  "cooperationType": "OUTSOURCING",
                  "requiredSpecializationIds": [%d],
                  "budget": {
                    "min": 20000,
                    "max": 40000,
                    "currency": "PLN"
                  },
                  "requiredPeriod": {
                    "from": "2026-08-01",
                    "until": "2026-12-31"
                  },
                  "maxDistanceKm": 200,
                  "minPartnerExperienceYears": 3,
                  "maxPartners": 2
                }
                """.formatted(specialization.getId());

        mockMvc.perform(
                        post(
                                "/api/companies/{companyId}/needs",
                                company.getId()
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.companyId")
                        .value(company.getId()))
                .andExpect(jsonPath("$.title")
                        .value("Partner do wykonania aplikacji mobilnej"))
                .andExpect(jsonPath("$.cooperationType")
                        .value("OUTSOURCING"))
                .andExpect(jsonPath("$.requiredSpecializations[0].id")
                        .value(specialization.getId()))
                .andExpect(jsonPath("$.budget.min")
                        .value(20000))
                .andExpect(jsonPath("$.budget.max")
                        .value(40000))
                .andExpect(jsonPath("$.budget.currency")
                        .value("PLN"))
                .andExpect(jsonPath("$.requiredPeriod.from")
                        .value("2026-08-01"))
                .andExpect(jsonPath("$.requiredPeriod.until")
                        .value("2026-12-31"))
                .andExpect(jsonPath("$.maxDistanceKm")
                        .value(200))
                .andExpect(jsonPath("$.minPartnerExperienceYears")
                        .value(3))
                .andExpect(jsonPath("$.maxPartners")
                        .value(2))
                .andExpect(jsonPath("$.active")
                        .value(true));

        assertThat(businessNeedRepository.count())
                .isEqualTo(1);
    }
}
