package pl.krystian.businesspartnermatching.catalog.specialization.exception;

import java.util.Set;

public class SpecializationNotFoundException extends RuntimeException {

    public SpecializationNotFoundException(Set<Long> specializationIds) {
        super("Specializations with ids " + specializationIds + " do not exist or are inactive");
    }
}