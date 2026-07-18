package pl.krystian.businesspartnermatching.common.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class ActivatableEntity {

    @Column(nullable = false)
    private boolean active = true;
}
