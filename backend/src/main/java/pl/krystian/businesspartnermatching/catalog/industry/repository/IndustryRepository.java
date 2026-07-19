package pl.krystian.businesspartnermatching.catalog.industry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krystian.businesspartnermatching.catalog.industry.model.entity.Industry;

import java.util.List;
import java.util.Optional;

public interface IndustryRepository extends JpaRepository<Industry, Long> {

    List<Industry> findAllByActiveTrueOrderByNameAsc();

    Optional<Industry> findByIdAndActiveTrue(Long id);
}
