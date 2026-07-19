package pl.krystian.businesspartnermatching.matching.distance;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.company.model.entity.Company;

import java.util.Map;
import java.util.Objects;

@Component
public class StaticDistanceCalculator implements DistanceCalculator {

    private static final Map<CityPair, Double> DISTANCES_KM = Map.of(
            CityPair.of("Poland", "Kraków", "Poland", "Warszawa"),
            295.0,

            CityPair.of("Poland", "Kraków", "Poland", "Katowice"),
            80.0,

            CityPair.of("Poland", "Kraków", "Poland", "Wrocław"),
            270.0,

            CityPair.of("Poland", "Warszawa", "Poland", "Wrocław"),
            355.0,

            CityPair.of("Poland", "Warszawa", "Poland", "Gdańsk"),
            340.0
    );

    @Override
    public double calculateDistanceKm(
            Company firstCompany,
            Company secondCompany
    ) {
        Objects.requireNonNull(
                firstCompany,
                "First company cannot be null"
        );

        Objects.requireNonNull(
                secondCompany,
                "Second company cannot be null"
        );

        CityPair cityPair = CityPair.of(
                firstCompany.getCountry(),
                firstCompany.getCity(),
                secondCompany.getCountry(),
                secondCompany.getCity()
        );

        if (cityPair.sameLocation()) {
            return 0.0;
        }

        Double distance = DISTANCES_KM.get(cityPair);

        if (distance == null) {
            throw new DistanceNotAvailableException(
                    "Distance is not available between %s, %s and %s, %s"
                            .formatted(
                                    firstCompany.getCity(),
                                    firstCompany.getCountry(),
                                    secondCompany.getCity(),
                                    secondCompany.getCountry()
                            )
            );
        }

        return distance;
    }
}
