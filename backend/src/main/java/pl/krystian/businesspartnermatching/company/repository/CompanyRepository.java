package pl.krystian.businesspartnermatching.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krystian.businesspartnermatching.company.model.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
