package pl.krystian.businesspartnermatching.matching.distance;

import pl.krystian.businesspartnermatching.company.model.entity.Company;

public interface DistanceCalculator {

    double calculateDistanceKm(
            Company firstCompany,
            Company secondCompany
    );
}
