package pl.krystian.businesspartnermatching.company.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.krystian.businesspartnermatching.catalog.industry.model.entity.Industry;
import pl.krystian.businesspartnermatching.common.persistence.ActivatableEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "companies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends ActivatableEntity {

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

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false, length = 150)
    private String city;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "established_at")
    private LocalDate establishedAt;

    @Column(columnDefinition = "TEXT")
    private String capabilities;

    public Company(
            String name,
            String description,
            Industry industry,
            String country,
            String city,
            BigDecimal latitude,
            BigDecimal longitude,
            LocalDate establishedAt,
            String capabilities
    ) {
        this.name = name;
        this.description = description;
        this.industry = industry;
        this.country = country;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.establishedAt = establishedAt;
        this.capabilities = capabilities;
    }
}
