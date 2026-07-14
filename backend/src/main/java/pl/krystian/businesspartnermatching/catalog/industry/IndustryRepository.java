package pl.krystian.businesspartnermatching.catalog.industry;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndustryRepository extends JpaRepository<Industry, Long> {
    List<Industry> findAllByActiveTrueOrderByNameAsc();
}
