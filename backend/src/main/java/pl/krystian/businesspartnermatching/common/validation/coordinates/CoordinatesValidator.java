package pl.krystian.businesspartnermatching.common.validation.coordinates;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.krystian.businesspartnermatching.company.model.dto.CreateCompanyRequest;

public class CoordinatesValidator
        implements ConstraintValidator<
        ValidCoordinates,
        CreateCompanyRequest
        > {

    @Override
    public boolean isValid(
            CreateCompanyRequest request,
            ConstraintValidatorContext context
    ) {
        if (request == null) {
            return true;
        }

        boolean latitudeProvided = request.latitude() != null;
        boolean longitudeProvided = request.longitude() != null;

        return latitudeProvided == longitudeProvided;
    }
}
