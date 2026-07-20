package pl.krystian.businesspartnermatching.matching.compatibility.rules;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.company.model.entity.Company;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

@Component
public class DifferentCompanyRule implements CompatibilityRule {

    @Override
    public boolean isSatisfied(
            BusinessNeed need,
            BusinessOffer offer
    ) {
        Company needCompany = need.getCompany();
        Company offerCompany = offer.getCompany();

        if (needCompany == offerCompany) {
            return false;
        }

        Long needCompanyId = needCompany.getId();
        Long offerCompanyId = offerCompany.getId();

        if (needCompanyId == null || offerCompanyId == null) {
            return true;
        }

        return !needCompanyId.equals(offerCompanyId);
    }

    @Override
    public CompatibilityFailureReason failureReason() {
        return CompatibilityFailureReason.SAME_COMPANY;
    }
}
