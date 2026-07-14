package pl.krystian.businesspartnermatching.catalog.specialization;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    List<Specialization> findAllByIndustryIdAndActiveTrueOrderByNameAsc(Long industryId);

    Long id(Long id);
}
