package pl.krystian.businesspartnermatching.catalog.specialization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krystian.businesspartnermatching.catalog.specialization.model.entity.Specialization;

import java.util.List;
import java.util.Set;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    List<Specialization> findAllByIndustryIdAndActiveTrueOrderByNameAsc(Long industryId);

    Set<Specialization> findAllByIdInAndActiveTrue(Set<Long> ids);
}
