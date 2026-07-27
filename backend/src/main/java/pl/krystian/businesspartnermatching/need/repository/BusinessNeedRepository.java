package pl.krystian.businesspartnermatching.need.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;

import java.util.List;

public interface BusinessNeedRepository
        extends JpaRepository<BusinessNeed, Long> {

    List<BusinessNeed> findAllByCompanyIdOrderByCreatedAtDesc(
            Long companyId
    );

    List<BusinessNeed> findAllByActiveTrue();

    @EntityGraph(attributePaths = {
            "company",
            "requiredSpecializations",
            "requiredSpecializations.industry"
    })
    List<BusinessNeed> findAllByActiveTrueOrderByCreatedAtDesc();
}
