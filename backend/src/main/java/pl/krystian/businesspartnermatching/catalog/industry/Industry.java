package pl.krystian.businesspartnermatching.catalog.industry;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.krystian.businesspartnermatching.common.persistance.ActivatableEntity;

@Entity
@Table(
        name = "industries",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_industries_code",
                        columnNames = "code"
                ),
                @UniqueConstraint(
                        name = "uk_industries_name",
                        columnNames = "name"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Industry extends ActivatableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    public Industry(
            String code,
            String name
    ) {
        this.code = code;
        this.name = name;
    }
}
