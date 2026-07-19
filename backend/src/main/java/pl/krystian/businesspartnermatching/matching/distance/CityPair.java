package pl.krystian.businesspartnermatching.matching.distance;

import java.util.Locale;
import java.util.Objects;

record CityPair(
        String firstCountry,
        String firstCity,
        String secondCountry,
        String secondCity
) {

    static CityPair of(
            String firstCountry,
            String firstCity,
            String secondCountry,
            String secondCity
    ) {
        String normalizedFirstCountry = normalize(firstCountry);
        String normalizedFirstCity = normalize(firstCity);
        String normalizedSecondCountry = normalize(secondCountry);
        String normalizedSecondCity = normalize(secondCity);

        String firstLocation =
                normalizedFirstCountry + ":" + normalizedFirstCity;

        String secondLocation =
                normalizedSecondCountry + ":" + normalizedSecondCity;

        if (firstLocation.compareTo(secondLocation) <= 0) {
            return new CityPair(
                    normalizedFirstCountry,
                    normalizedFirstCity,
                    normalizedSecondCountry,
                    normalizedSecondCity
            );
        }

        return new CityPair(
                normalizedSecondCountry,
                normalizedSecondCity,
                normalizedFirstCountry,
                normalizedFirstCity
        );
    }

    boolean sameLocation() {
        return firstCountry.equals(secondCountry)
                && firstCity.equals(secondCity);
    }

    private static String normalize(String value) {
        Objects.requireNonNull(
                value,
                "Country and city cannot be null"
        );

        return value.strip().toLowerCase(Locale.ROOT);
    }
}
