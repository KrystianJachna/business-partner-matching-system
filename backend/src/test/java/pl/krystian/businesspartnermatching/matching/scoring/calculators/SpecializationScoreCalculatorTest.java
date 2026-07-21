package pl.krystian.businesspartnermatching.matching.scoring.calculators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.catalog.specialization.model.entity.Specialization;
import pl.krystian.businesspartnermatching.matching.scoring.MatchingCriterion;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpecializationScoreCalculatorTest {

    private SpecializationScoreCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new SpecializationScoreCalculator();
    }

    @Test
    void shouldReturnSpecializationCriterion() {
        assertThat(calculator.criterion())
                .isEqualTo(MatchingCriterion.SPECIALIZATION);
    }

    @Test
    void shouldReturnOneWhenAllRequiredSpecializationsAreOffered() {
        // given
        Specialization javaRequired = specialization("JAVA");
        Specialization springRequired = specialization("SPRING");

        Specialization javaOffered = specialization("JAVA");
        Specialization springOffered = specialization("SPRING");

        BusinessNeed need = needWithSpecializations(
                Set.of(javaRequired, springRequired)
        );

        BusinessOffer offer = offerWithSpecializations(
                Set.of(javaOffered, springOffered)
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo("1.0000");
    }

    @Test
    void shouldReturnPartialScoreWhenOnlySomeRequiredSpecializationsAreOffered() {
        // given
        BusinessNeed need = needWithSpecializations(
                Set.of(
                        specialization("JAVA"),
                        specialization("SPRING"),
                        specialization("POSTGRESQL")
                )
        );

        BusinessOffer offer = offerWithSpecializations(
                Set.of(
                        specialization("JAVA"),
                        specialization("SPRING")
                )
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo("0.6667");
    }

    @Test
    void shouldReturnZeroWhenNoRequiredSpecializationIsOffered() {
        // given
        BusinessNeed need = needWithSpecializations(
                Set.of(
                        specialization("JAVA"),
                        specialization("SPRING")
                )
        );

        BusinessOffer offer = offerWithSpecializations(
                Set.of(
                        specialization("PYTHON"),
                        specialization("DJANGO")
                )
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo("0.0000");
    }

    @Test
    void shouldReturnOneWhenNeedHasNoRequiredSpecializations() {
        // given
        BusinessNeed need = needWithSpecializations(Set.of());

        BusinessOffer offer = offerWithSpecializations(
                Set.of(
                        specialization("JAVA"),
                        specialization("SPRING")
                )
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void shouldIgnoreAdditionalOfferedSpecializations() {
        // given
        BusinessNeed need = needWithSpecializations(
                Set.of(
                        specialization("JAVA"),
                        specialization("SPRING")
                )
        );

        BusinessOffer offer = offerWithSpecializations(
                Set.of(
                        specialization("JAVA"),
                        specialization("SPRING"),
                        specialization("DOCKER"),
                        specialization("KUBERNETES")
                )
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo("1.0000");
    }

    @Test
    void shouldMatchDifferentSpecializationInstancesWithSameCode() {
        // given
        Specialization requiredSpecialization =
                specialization("JAVA");

        Specialization offeredSpecialization =
                specialization("JAVA");

        assertThat(requiredSpecialization)
                .isNotSameAs(offeredSpecialization);

        BusinessNeed need = needWithSpecializations(
                Set.of(requiredSpecialization)
        );

        BusinessOffer offer = offerWithSpecializations(
                Set.of(offeredSpecialization)
        );

        // when
        BigDecimal score = calculator.calculateScore(need, offer);

        // then
        assertThat(score).isEqualByComparingTo("1.0000");
    }

    private BusinessNeed needWithSpecializations(
            Set<Specialization> specializations
    ) {
        BusinessNeed need = mock(BusinessNeed.class);

        when(need.getRequiredSpecializations())
                .thenReturn(specializations);

        return need;
    }

    private BusinessOffer offerWithSpecializations(
            Set<Specialization> specializations
    ) {
        BusinessOffer offer = mock(BusinessOffer.class);

        when(offer.getOfferedSpecializations())
                .thenReturn(specializations);

        return offer;
    }

    private Specialization specialization(String code) {
        Specialization specialization =
                mock(Specialization.class);

        when(specialization.getCode()).thenReturn(code);

        return specialization;
    }
}
