package pl.krystian.businesspartnermatching.matching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.common.cooperation.CooperationType;
import pl.krystian.businesspartnermatching.company.model.entity.Company;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityChecker;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityFailureReason;
import pl.krystian.businesspartnermatching.matching.compatibility.CompatibilityResult;
import pl.krystian.businesspartnermatching.matching.compatibility.rules.CooperationTypeRule;
import pl.krystian.businesspartnermatching.matching.compatibility.rules.DifferentCompanyRule;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CompatibilityCheckerTest {

    private CompatibilityChecker compatibilityChecker;

    @BeforeEach
    void setUp() {
        compatibilityChecker = new CompatibilityChecker(
                List.of(
                        new DifferentCompanyRule(),
                        new CooperationTypeRule()
                )
        );
    }

    @Test
    void shouldBeCompatibleWhenAllHardConditionsAreSatisfied() {
        // given
        Company needCompany = createCompany("Need company");
        Company offerCompany = createCompany("Offer company");

        CooperationType cooperationType = firstCooperationType();

        BusinessNeed need = createNeed(
                needCompany,
                cooperationType
        );

        BusinessOffer offer = createOffer(
                offerCompany,
                cooperationType
        );

        // when
        CompatibilityResult result =
                compatibilityChecker.check(need, offer);

        // then
        assertThat(result.compatible()).isTrue();
        assertThat(result.failureReasons()).isEmpty();
    }

    @Test
    void shouldRejectMatchWhenNeedAndOfferBelongToSameCompany() {
        // given
        Company company = createCompany("Same company");

        CooperationType cooperationType = firstCooperationType();

        BusinessNeed need = createNeed(
                company,
                cooperationType
        );

        BusinessOffer offer = createOffer(
                company,
                cooperationType
        );

        // when
        CompatibilityResult result =
                compatibilityChecker.check(need, offer);

        // then
        assertThat(result.compatible()).isFalse();

        assertThat(result.failureReasons())
                .containsExactly(
                        CompatibilityFailureReason.SAME_COMPANY
                );
    }

    @Test
    void shouldRejectMatchWhenCooperationTypesAreDifferent() {
        // given
        Company needCompany = createCompany("Need company");
        Company offerCompany = createCompany("Offer company");

        BusinessNeed need = createNeed(
                needCompany,
                firstCooperationType()
        );

        BusinessOffer offer = createOffer(
                offerCompany,
                secondCooperationType()
        );

        // when
        CompatibilityResult result =
                compatibilityChecker.check(need, offer);

        // then
        assertThat(result.compatible()).isFalse();

        assertThat(result.failureReasons())
                .containsExactly(
                        CompatibilityFailureReason
                                .INCOMPATIBLE_COOPERATION_TYPE
                );
    }

    @Test
    void shouldReturnAllFailureReasons() {
        // given
        Company company = createCompany("Same company");

        BusinessNeed need = createNeed(
                company,
                firstCooperationType()
        );

        BusinessOffer offer = createOffer(
                company,
                secondCooperationType()
        );

        // when
        CompatibilityResult result =
                compatibilityChecker.check(need, offer);

        // then
        assertThat(result.compatible()).isFalse();

        assertThat(result.failureReasons())
                .containsExactly(
                        CompatibilityFailureReason.SAME_COMPANY,
                        CompatibilityFailureReason
                                .INCOMPATIBLE_COOPERATION_TYPE
                );
    }

    private Company createCompany(String name) {
        return new Company(
                name,
                "Company description",
                null,
                Set.of(),
                "Poland",
                "Kraków",
                null,
                "Company capabilities"
        );
    }

    private BusinessNeed createNeed(
            Company company,
            CooperationType cooperationType
    ) {
        return new BusinessNeed(
                company,
                "Business need",
                "Business need description",
                cooperationType,
                Set.of(),
                null,
                null,
                null,
                null,
                1
        );
    }

    private BusinessOffer createOffer(
            Company company,
            CooperationType cooperationType
    ) {
        return new BusinessOffer(
                company,
                "Business offer",
                "Business offer description",
                cooperationType,
                Set.of(),
                null,
                null,
                null,
                1
        );
    }

    private CooperationType firstCooperationType() {
        return CooperationType.values()[0];
    }

    private CooperationType secondCooperationType() {
        return CooperationType.values()[1];
    }
}
