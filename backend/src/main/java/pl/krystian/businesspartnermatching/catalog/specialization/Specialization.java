package pl.krystian.businesspartnermatching.catalog.specialization;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.krystian.businesspartnermatching.catalog.industry.Industry;

@Entity
@Table(name = "specializations", uniqueConstraints = {@UniqueConstraint(name = "uk_specializations_code", columnNames = "code"), @UniqueConstraint(name = "uk_specializations_industry_name", columnNames = {"industry_id", "name"})})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "industry_id", nullable = false)
    private Industry industry;

    public Specialization(String code, String name, Industry industry) {
        this.code = code;
        this.name = name;
        this.industry = industry;
    }
}
