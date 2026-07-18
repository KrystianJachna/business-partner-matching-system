package pl.krystian.businesspartnermatching.catalog.industry;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IndustryRepository extends JpaRepository<Industry, Long> {

    List<Industry> findAllByActiveTrueOrderByNameAsc();

    Optional<Industry> findByIdAndActiveTrue(Long id);
}
