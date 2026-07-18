package pl.krystian.businesspartnermatching.offer.exception;

public class BusinessOfferNotFoundException
        extends RuntimeException {

    public BusinessOfferNotFoundException(Long businessOfferId) {
        super(
                "Business offer with id "
                        + businessOfferId
                        + " does not exist"
        );
    }
}
