package pl.krystian.businesspartnermatching.need;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessNeedRepository
        extends JpaRepository<BusinessNeed, Long> {

    List<BusinessNeed> findAllByCompanyIdOrderByCreatedAtDesc(
            Long companyId
    );
}