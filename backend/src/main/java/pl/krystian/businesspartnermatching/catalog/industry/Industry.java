package pl.krystian.businesspartnermatching.catalog.industry;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "industries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Industry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    @Column(nullable = false)
    private boolean active = true;

    public Industry(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
