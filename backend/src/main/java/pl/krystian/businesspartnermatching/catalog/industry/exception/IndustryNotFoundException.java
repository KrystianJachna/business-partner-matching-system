package pl.krystian.businesspartnermatching.catalog.industry.exception;

public class IndustryNotFoundException extends RuntimeException {

    public IndustryNotFoundException(Long industryId) {
        super("Industry with id " + industryId + " does not exist");
    }
}
