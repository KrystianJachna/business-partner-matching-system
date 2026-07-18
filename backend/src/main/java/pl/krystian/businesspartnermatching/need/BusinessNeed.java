package pl.krystian.businesspartnermatching.need;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.krystian.businesspartnermatching.catalog.specialization.Specialization;
import pl.krystian.businesspartnermatching.common.cooperation.CooperationType;
import pl.krystian.businesspartnermatching.common.money.MoneyRange;
import pl.krystian.businesspartnermatching.common.persistence.ActivatableAuditableEntity;
import pl.krystian.businesspartnermatching.common.time.DateRange;
import pl.krystian.businesspartnermatching.company.Company;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "business_needs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessNeed extends ActivatableAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "cooperation_type", nullable = false, length = 50)
    private CooperationType cooperationType;

    @ManyToMany
    @JoinTable(
            name = "business_need_required_specializations",
            joinColumns = @JoinColumn(name = "business_need_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private Set<Specialization> requiredSpecializations = new HashSet<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "min",
                    column = @Column(
                            name = "budget_min",
                            precision = 15,
                            scale = 2
                    )
            ),
            @AttributeOverride(
                    name = "max",
                    column = @Column(
                            name = "budget_max",
                            precision = 15,
                            scale = 2
                    )
            ),
            @AttributeOverride(
                    name = "currency",
                    column = @Column(
                            name = "budget_currency",
                            length = 3
                    )
            )
    })
    private MoneyRange budget;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "from",
                    column = @Column(name = "required_from")
            ),
            @AttributeOverride(
                    name = "until",
                    column = @Column(name = "required_until")
            )
    })
    private DateRange requiredPeriod;

    @Column(name = "max_distance_km")
    private Integer maxDistanceKm;

    @Column(name = "min_partner_experience_years")
    private Integer minPartnerExperienceYears;

    @Column(name = "max_partners", nullable = false)
    private Integer maxPartners;

    public BusinessNeed(
            Company company,
            String title,
            String description,
            CooperationType cooperationType,
            Set<Specialization> requiredSpecializations,
            MoneyRange budget,
            DateRange requiredPeriod,
            Integer maxDistanceKm,
            Integer minPartnerExperienceYears,
            Integer maxPartners
    ) {
        this.company = company;
        this.title = title;
        this.description = description;
        this.cooperationType = cooperationType;
        this.requiredSpecializations =
                new HashSet<>(requiredSpecializations);
        this.budget = budget;
        this.requiredPeriod = requiredPeriod;
        this.maxDistanceKm = maxDistanceKm;
        this.minPartnerExperienceYears =
                minPartnerExperienceYears;
        this.maxPartners = maxPartners;
    }
}
