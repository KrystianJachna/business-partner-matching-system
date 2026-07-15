package pl.krystian.businesspartnermatching.company;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.krystian.businesspartnermatching.catalog.industry.Industry;
import pl.krystian.businesspartnermatching.catalog.specialization.Specialization;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "industry_id", nullable = false)
    private Industry industry;

    @ManyToMany
    @JoinTable(
            name = "company_specializations",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private Set<Specialization> specializations = new HashSet<>();

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false, length = 150)
    private String city;

    @Column(name = "established_at")
    private LocalDate establishedAt;

    @Column(columnDefinition = "TEXT")
    private String capabilities;

    @Column(nullable = false)
    private boolean active = true;
}