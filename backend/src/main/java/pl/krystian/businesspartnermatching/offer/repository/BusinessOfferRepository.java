package pl.krystian.businesspartnermatching.offer.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.util.List;

public interface BusinessOfferRepository
        extends JpaRepository<BusinessOffer, Long> {

    List<BusinessOffer> findAllByCompanyIdOrderByCreatedAtDesc(
            Long companyId
    );

    List<BusinessOffer> findAllByActiveTrue();

    @EntityGraph(attributePaths = {
            "company",
            "offeredSpecializations",
            "offeredSpecializations.industry"
    })
    List<BusinessOffer> findAllByActiveTrueOrderByCreatedAtDesc();
}
