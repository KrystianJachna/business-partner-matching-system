package pl.krystian.businesspartnermatching.common.validation.coordinates;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CoordinatesValidator.class)
public @interface ValidCoordinates {

    String message() default
            "Latitude and longitude must either both be provided or both be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
