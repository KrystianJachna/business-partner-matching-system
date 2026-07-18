package pl.krystian.businesspartnermatching.offer;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.krystian.businesspartnermatching.catalog.specialization.Specialization;
import pl.krystian.businesspartnermatching.common.money.MoneyRange;
import pl.krystian.businesspartnermatching.common.time.DateRange;
import pl.krystian.businesspartnermatching.company.Company;
import pl.krystian.businesspartnermatching.common.cooperation.CooperationType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "business_offers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessOffer {

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
            name = "business_offer_offered_specializations",
            joinColumns = @JoinColumn(name = "business_offer_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private Set<Specialization> offeredSpecializations =
            new HashSet<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "min",
                    column = @Column(
                            name = "price_min",
                            precision = 15,
                            scale = 2
                    )
            ),
            @AttributeOverride(
                    name = "max",
                    column = @Column(
                            name = "price_max",
                            precision = 15,
                            scale = 2
                    )
            ),
            @AttributeOverride(
                    name = "currency",
                    column = @Column(
                            name = "price_currency",
                            length = 3
                    )
            )
    })
    private MoneyRange priceRange;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "from",
                    column = @Column(name = "available_from")
            ),
            @AttributeOverride(
                    name = "until",
                    column = @Column(name = "available_until")
            )
    })
    private DateRange availabilityPeriod;

    @Column(name = "service_radius_km")
    private Integer serviceRadiusKm;

    @Column(name = "max_partners", nullable = false)
    private Integer maxPartners;

    @Column(nullable = false)
    private boolean active = true;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public BusinessOffer(
            Company company,
            String title,
            String description,
            CooperationType cooperationType,
            Set<Specialization> offeredSpecializations,
            MoneyRange priceRange,
            DateRange availabilityPeriod,
            Integer serviceRadiusKm,
            Integer maxPartners
    ) {
        this.company = company;
        this.title = title;
        this.description = description;
        this.cooperationType = cooperationType;
        this.offeredSpecializations =
                new HashSet<>(offeredSpecializations);
        this.priceRange = priceRange;
        this.availabilityPeriod = availabilityPeriod;
        this.serviceRadiusKm = serviceRadiusKm;
        this.maxPartners = maxPartners;
    }
}
