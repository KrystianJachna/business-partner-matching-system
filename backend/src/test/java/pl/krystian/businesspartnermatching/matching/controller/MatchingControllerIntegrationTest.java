package pl.krystian.businesspartnermatching.matching.controller;

import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.IntegrationTest;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MatchingControllerIntegrationTest extends IntegrationTest {

    @Test
    void shouldReturnMatchesGeneratedFromDemoData() throws Exception {
        mockMvc.perform(
                        post("/api/matching")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchCount").value(3))
                .andExpect(jsonPath("$.matches").isArray())
                .andExpect(jsonPath("$.matches", hasSize(3)))
                .andExpect(jsonPath("$.matches[0].needCompanyName").isString())
                .andExpect(jsonPath("$.matches[0].offerCompanyName").isString())
                .andExpect(jsonPath("$.matches[0].totalScore").isNumber())
                .andExpect(jsonPath("$.matches[0].criterionScores").isArray())
                .andExpect(jsonPath("$.matches[0].criterionScores[0].criterion").isString())
                .andExpect(jsonPath("$.matches[0].criterionScores[0].score").isNumber())
                .andExpect(jsonPath("$.matches[0].criterionScores[0].weight").isNumber())
                .andExpect(jsonPath("$.matches[0].criterionScores[0].weightedScore").isNumber())
                .andExpect(jsonPath("$.matches[0].compatibilityReasons").isArray())

                .andExpect(jsonPath(
                        "$.matches[?("
                                + "@.needTitle == 'Marketing analytics platform' "
                                + "&& @.offerTitle == "
                                + "'Software development and data analytics'"
                                + ")]",
                        hasSize(1)
                ))

                .andExpect(jsonPath(
                        "$.matches[?("
                                + "@.needTitle == "
                                + "'Development of a partner portal' "
                                + "&& @.offerTitle == "
                                + "'Custom software and cloud implementation'"
                                + ")]",
                        hasSize(1)
                ))

                .andExpect(jsonPath(
                        "$.matches[?("
                                + "@.needTitle == "
                                + "'Development of a partner portal' "
                                + "&& @.offerTitle == "
                                + "'Software development and data analytics'"
                                + ")]",
                        hasSize(1)
                ))

                .andExpect(jsonPath(
                        "$.matches[?("
                                + "@.needTitle == "
                                + "'Industrial production line automation'"
                                + ")]",
                        hasSize(0)
                ));
    }
}
