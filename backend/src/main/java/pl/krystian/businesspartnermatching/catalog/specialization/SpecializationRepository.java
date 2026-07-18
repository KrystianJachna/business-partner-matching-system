package pl.krystian.businesspartnermatching.catalog.specialization;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    List<Specialization> findAllByIndustryIdAndActiveTrueOrderByNameAsc(Long industryId);

    Set<Specialization> findAllByIdInAndActiveTrue(Set<Long> ids);
}
