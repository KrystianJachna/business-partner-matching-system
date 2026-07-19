package pl.krystian.businesspartnermatching.matching.distance;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.company.model.entity.Company;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class HaversineDistanceCalculator
        implements DistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0088;

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

        validateCoordinates(firstCompany);
        validateCoordinates(secondCompany);

        double firstLatitude = Math.toRadians(
                firstCompany.getLatitude().doubleValue()
        );

        double firstLongitude = Math.toRadians(
                firstCompany.getLongitude().doubleValue()
        );

        double secondLatitude = Math.toRadians(
                secondCompany.getLatitude().doubleValue()
        );

        double secondLongitude = Math.toRadians(
                secondCompany.getLongitude().doubleValue()
        );

        double latitudeDifference =
                secondLatitude - firstLatitude;

        double longitudeDifference =
                secondLongitude - firstLongitude;

        double haversine =
                Math.pow(
                        Math.sin(latitudeDifference / 2),
                        2
                )
                        + Math.cos(firstLatitude)
                        * Math.cos(secondLatitude)
                        * Math.pow(
                        Math.sin(longitudeDifference / 2),
                        2
                );

        double angularDistance = 2 * Math.atan2(
                Math.sqrt(haversine),
                Math.sqrt(1 - haversine)
        );

        return EARTH_RADIUS_KM * angularDistance;
    }

    private void validateCoordinates(Company company) {
        BigDecimal latitude = company.getLatitude();
        BigDecimal longitude = company.getLongitude();

        if (latitude == null || longitude == null) {
            throw new CoordinatesNotAvailableException(
                    "Coordinates are not available for company: "
                            + company.getName()
            );
        }

        if (latitude.compareTo(BigDecimal.valueOf(-90)) < 0
                || latitude.compareTo(BigDecimal.valueOf(90)) > 0) {
            throw new IllegalArgumentException(
                    "Latitude must be between -90 and 90"
            );
        }

        if (longitude.compareTo(BigDecimal.valueOf(-180)) < 0
                || longitude.compareTo(BigDecimal.valueOf(180)) > 0) {
            throw new IllegalArgumentException(
                    "Longitude must be between -180 and 180"
            );
        }
    }
}
