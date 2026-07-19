package pl.krystian.businesspartnermatching.matching.scoring.calculators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.company.model.entity.Company;
import pl.krystian.businesspartnermatching.matching.criterion.MatchingCriterion;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExperienceScoreCalculatorTest {

    private ExperienceScoreCalculator calculator;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(
                Instant.parse("2026-07-20T12:00:00Z"),
                ZoneOffset.UTC
        );

        calculator = new ExperienceScoreCalculator(fixedClock);
    }

    @Test
    void shouldReturnExperienceCriterion() {
        assertThat(calculator.criterion())
                .isEqualTo(MatchingCriterion.EXPERIENCE);
    }

    @Test
    void shouldReturnOneWhenMinimumExperienceIsNotSpecified() {
        // given
        BusinessNeed need = needWithMinimumExperience(null);
        BusinessOffer offer = mock(BusinessOffer.class);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void shouldReturnOneWhenMinimumExperienceIsZero() {
        // given
        BusinessNeed need = needWithMinimumExperience(0);
        BusinessOffer offer = mock(BusinessOffer.class);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void shouldReturnZeroWhenEstablishmentDateIsMissing() {
        // given
        BusinessNeed need = needWithMinimumExperience(5);

        Company company = companyEstablishedAt(null);
        BusinessOffer offer = offerFromCompany(company);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldReturnZeroWhenPartnerHasLessExperienceThanRequired() {
        // given
        BusinessNeed need = needWithMinimumExperience(5);

        Company company = companyEstablishedAt(
                LocalDate.of(2023, 7, 20)
        );

        BusinessOffer offer = offerFromCompany(company);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldReturnHalfWhenPartnerHasExactlyRequiredExperience() {
        // given
        BusinessNeed need = needWithMinimumExperience(5);

        Company company = companyEstablishedAt(
                LocalDate.of(2021, 7, 20)
        );

        BusinessOffer offer = offerFromCompany(company);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo("0.5000");
    }

    @Test
    void shouldReturnPartialScoreWhenPartnerHasMoreThanRequiredExperience() {
        // given
        BusinessNeed need = needWithMinimumExperience(5);

        Company company = companyEstablishedAt(
                LocalDate.of(2019, 7, 20)
        );

        BusinessOffer offer = offerFromCompany(company);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo("0.7000");
    }

    @Test
    void shouldReturnOneWhenPartnerHasTwiceRequiredExperience() {
        // given
        BusinessNeed need = needWithMinimumExperience(5);

        Company company = companyEstablishedAt(
                LocalDate.of(2016, 7, 20)
        );

        BusinessOffer offer = offerFromCompany(company);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo("1.0000");
    }

    @Test
    void shouldCapScoreAtOneWhenPartnerHasMuchMoreExperience() {
        // given
        BusinessNeed need = needWithMinimumExperience(5);

        Company company = companyEstablishedAt(
                LocalDate.of(2000, 1, 1)
        );

        BusinessOffer offer = offerFromCompany(company);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo("1.0000");
    }

    @Test
    void shouldNotCountIncompleteYearOfExperience() {
        // given
        BusinessNeed need = needWithMinimumExperience(5);

        Company company = companyEstablishedAt(
                LocalDate.of(2021, 7, 21)
        );

        BusinessOffer offer = offerFromCompany(company);

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ZERO);
    }

    private BusinessNeed needWithMinimumExperience(
            Integer minimumExperienceYears
    ) {
        BusinessNeed need = mock(BusinessNeed.class);

        when(need.getMinPartnerExperienceYears())
                .thenReturn(minimumExperienceYears);

        return need;
    }

    private BusinessOffer offerFromCompany(Company company) {
        BusinessOffer offer = mock(BusinessOffer.class);

        when(offer.getCompany()).thenReturn(company);

        return offer;
    }

    private Company companyEstablishedAt(LocalDate establishedAt) {
        Company company = mock(Company.class);

        when(company.getEstablishedAt())
                .thenReturn(establishedAt);

        return company;
    }
}
