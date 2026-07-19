package pl.krystian.businesspartnermatching.matching.distance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.company.model.entity.Company;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StaticDistanceCalculatorTest {

    private StaticDistanceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new StaticDistanceCalculator();
    }

    @Test
    void shouldReturnZeroForCompaniesInSameLocation() {
        // given
        Company firstCompany = company("Poland", "Kraków");
        Company secondCompany = company("Poland", "Kraków");

        // when
        double distance = calculator.calculateDistanceKm(
                firstCompany,
                secondCompany
        );

        // then
        assertThat(distance).isZero();
    }

    @Test
    void shouldReturnConfiguredDistanceBetweenCities() {
        // given
        Company firstCompany = company("Poland", "Kraków");
        Company secondCompany = company("Poland", "Warszawa");

        // when
        double distance = calculator.calculateDistanceKm(
                firstCompany,
                secondCompany
        );

        // then
        assertThat(distance).isEqualTo(295.0);
    }

    @Test
    void shouldReturnSameDistanceRegardlessOfCompanyOrder() {
        // given
        Company krakowCompany = company("Poland", "Kraków");
        Company warsawCompany = company("Poland", "Warszawa");

        // when
        double firstDistance = calculator.calculateDistanceKm(
                krakowCompany,
                warsawCompany
        );

        double secondDistance = calculator.calculateDistanceKm(
                warsawCompany,
                krakowCompany
        );

        // then
        assertThat(firstDistance).isEqualTo(secondDistance);
    }

    @Test
    void shouldIgnoreCaseAndSurroundingWhitespace() {
        // given
        Company firstCompany = company(" POLAND ", " KRAKÓW ");
        Company secondCompany = company("poland", "warszawa");

        // when
        double distance = calculator.calculateDistanceKm(
                firstCompany,
                secondCompany
        );

        // then
        assertThat(distance).isEqualTo(295.0);
    }

    @Test
    void shouldThrowExceptionWhenDistanceIsNotAvailable() {
        // given
        Company firstCompany = company("Poland", "Rzeszów");
        Company secondCompany = company("Poland", "Szczecin");

        // when / then
        assertThatThrownBy(
                () -> calculator.calculateDistanceKm(
                        firstCompany,
                        secondCompany
                )
        )
                .isInstanceOf(
                        DistanceNotAvailableException.class
                )
                .hasMessageContaining(
                        "Distance is not available"
                );
    }

    private Company company(
            String country,
            String city
    ) {
        Company company = mock(Company.class);

        when(company.getCountry()).thenReturn(country);
        when(company.getCity()).thenReturn(city);

        return company;
    }
}
