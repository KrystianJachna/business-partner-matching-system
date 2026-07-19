package pl.krystian.businesspartnermatching.matching.distance;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.company.model.entity.Company;

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

        if (!hasCoordinates(firstCompany)
                || !hasCoordinates(secondCompany)) {
            throw new IllegalStateException(
                    "Both companies must have coordinates"
            );
        }

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

    private boolean hasCoordinates(Company company) {
        return company.getLatitude() != null
                && company.getLongitude() != null;
    }
}
