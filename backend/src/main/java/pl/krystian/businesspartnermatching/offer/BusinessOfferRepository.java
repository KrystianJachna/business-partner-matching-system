package pl.krystian.businesspartnermatching.offer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessOfferRepository
        extends JpaRepository<BusinessOffer, Long> {

    List<BusinessOffer> findAllByCompanyIdOrderByCreatedAtDesc(
            Long companyId
    );
}
