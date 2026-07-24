package pl.krystian.businesspartnermatching.company.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.krystian.businesspartnermatching.company.model.entity.Company;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @EntityGraph(attributePaths = {
            "industry",
            "specializations"
    })
    List<Company> findAllByActiveTrueOrderByNameAsc();

}
