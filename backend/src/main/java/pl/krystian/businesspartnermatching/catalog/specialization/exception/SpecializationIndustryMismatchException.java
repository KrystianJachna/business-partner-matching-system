package pl.krystian.businesspartnermatching.catalog.specialization.exception;

public class SpecializationIndustryMismatchException extends RuntimeException {

    public SpecializationIndustryMismatchException(Long specializationId, Long industryId) {
        super(
                "Specialization with id " + specializationId
                        + " does not belong to industry with id " + industryId
        );
    }
}