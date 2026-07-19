package pl.krystian.businesspartnermatching.matching.distance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.company.model.entity.Company;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HaversineDistanceCalculatorTest {

    private HaversineDistanceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new HaversineDistanceCalculator();
    }

    @Test
    void shouldReturnZeroForSameCoordinates() {
        Company firstCompany = company(
                "50.064650",
                "19.944980"
        );

        Company secondCompany = company(
                "50.064650",
                "19.944980"
        );

        double distance = calculator.calculateDistanceKm(
                firstCompany,
                secondCompany
        );

        assertThat(distance).isZero();
    }

    @Test
    void shouldCalculateDistanceBetweenKrakowAndWarsaw() {
        Company krakow = company(
                "50.064650",
                "19.944980"
        );

        Company warsaw = company(
                "52.229676",
                "21.012229"
        );

        double distance = calculator.calculateDistanceKm(
                krakow,
                warsaw
        );

        assertThat(distance)
                .isBetween(250.0, 255.0);
    }

    @Test
    void shouldThrowExceptionWhenCoordinatesAreMissing() {
        Company firstCompany = company(
                null,
                null
        );

        Company secondCompany = company(
                "52.229676",
                "21.012229"
        );

        assertThatThrownBy(
                () -> calculator.calculateDistanceKm(
                        firstCompany,
                        secondCompany
                )
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Both companies must have coordinates"
                );
    }

    private Company company(
            String latitude,
            String longitude
    ) {
        Company company = mock(Company.class);

        when(company.getLatitude())
                .thenReturn(
                        latitude == null
                                ? null
                                : new BigDecimal(latitude)
                );

        when(company.getLongitude())
                .thenReturn(
                        longitude == null
                                ? null
                                : new BigDecimal(longitude)
                );

        when(company.getName()).thenReturn("Test company");

        return company;
    }
}
