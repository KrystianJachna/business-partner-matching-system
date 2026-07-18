package pl.krystian.businesspartnermatching.catalog.specialization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pl.krystian.businesspartnermatching.catalog.industry.Industry;
import pl.krystian.businesspartnermatching.catalog.specialization.exception.SpecializationNotFoundException;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpecializationResolverTest {

    @Mock
    private SpecializationRepository specializationRepository;

    private SpecializationResolver specializationResolver;

    @BeforeEach
    void setUp() {
        specializationResolver = new SpecializationResolver(
                specializationRepository
        );
    }

    @Test
    void shouldResolveAllActiveSpecializations() {
        Industry industry = new Industry(
                "IT",
                "Information Technology"
        );

        Specialization java = new Specialization(
                industry,
                "JAVA",
                "Java Development"
        );
        ReflectionTestUtils.setField(java, "id", 10L);

        Specialization cloud = new Specialization(
                industry,
                "CLOUD",
                "Cloud Computing"
        );
        ReflectionTestUtils.setField(cloud, "id", 20L);

        Set<Long> requestedIds = Set.of(10L, 20L);

        when(
                specializationRepository
                        .findAllByIdInAndActiveTrue(requestedIds)
        ).thenReturn(Set.of(java, cloud));

        Set<Specialization> result =
                specializationResolver.resolveActive(requestedIds);

        assertThat(result)
                .containsExactlyInAnyOrder(java, cloud);

        verify(specializationRepository)
                .findAllByIdInAndActiveTrue(requestedIds);
    }

    @Test
    void shouldThrowExceptionWhenSomeSpecializationsDoNotExist() {
        Industry industry = new Industry(
                "IT",
                "Information Technology"
        );

        Specialization java = new Specialization(
                industry,
                "JAVA",
                "Java Development"
        );
        ReflectionTestUtils.setField(java, "id", 10L);

        Set<Long> requestedIds = Set.of(10L, 20L);

        when(
                specializationRepository
                        .findAllByIdInAndActiveTrue(requestedIds)
        ).thenReturn(Set.of(java));

        assertThatThrownBy(
                () -> specializationResolver.resolveActive(
                        requestedIds
                )
        )
                .isInstanceOf(
                        SpecializationNotFoundException.class
                )
                .hasMessageContaining("20");
    }

    @Test
    void shouldThrowExceptionWhenNoSpecializationsExist() {
        Set<Long> requestedIds = Set.of(10L, 20L);

        when(
                specializationRepository
                        .findAllByIdInAndActiveTrue(requestedIds)
        ).thenReturn(Set.of());

        assertThatThrownBy(
                () -> specializationResolver.resolveActive(
                        requestedIds
                )
        )
                .isInstanceOf(
                        SpecializationNotFoundException.class
                )
                .hasMessageContaining("10")
                .hasMessageContaining("20");
    }
}
