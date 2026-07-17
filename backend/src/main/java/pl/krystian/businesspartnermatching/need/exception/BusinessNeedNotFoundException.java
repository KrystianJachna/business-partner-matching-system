package pl.krystian.businesspartnermatching.need.exception;

public class BusinessNeedNotFoundException extends RuntimeException {

    public BusinessNeedNotFoundException(Long businessNeedId) {
        super("Business need with id " + businessNeedId + " was not found");
    }
}