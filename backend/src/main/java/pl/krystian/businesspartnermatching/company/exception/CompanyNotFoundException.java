package pl.krystian.businesspartnermatching.company.exception;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException(Long companyId) {
        super("Company with id " + companyId + " does not exist");
    }
}