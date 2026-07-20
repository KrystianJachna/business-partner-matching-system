package pl.krystian.businesspartnermatching.matching.compatibility;

import org.junit.jupiter.api.Test;
import pl.krystian.businesspartnermatching.matching.compatibility.rules.CompatibilityRule;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CompatibilityCheckerTest {

    @Test
    void shouldReturnCompatibleResultWhenAllRulesAreSatisfied() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        CompatibilityRule firstRule = mock(CompatibilityRule.class);
        CompatibilityRule secondRule = mock(CompatibilityRule.class);

        when(firstRule.isSatisfied(need, offer))
                .thenReturn(true);

        when(secondRule.isSatisfied(need, offer))
                .thenReturn(true);

        CompatibilityChecker checker = new CompatibilityChecker(
                List.of(firstRule, secondRule)
        );

        // when
        CompatibilityResult result = checker.check(need, offer);

        // then
        assertThat(result.compatible()).isTrue();
        assertThat(result.failureReasons()).isEmpty();
    }

    @Test
    void shouldCollectFailureReasonFromUnsatisfiedRule() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        CompatibilityRule satisfiedRule =
                mock(CompatibilityRule.class);

        CompatibilityRule failedRule =
                mock(CompatibilityRule.class);

        when(satisfiedRule.isSatisfied(need, offer))
                .thenReturn(true);

        when(failedRule.isSatisfied(need, offer))
                .thenReturn(false);

        when(failedRule.failureReason())
                .thenReturn(
                        CompatibilityFailureReason.SAME_COMPANY
                );

        CompatibilityChecker checker = new CompatibilityChecker(
                List.of(satisfiedRule, failedRule)
        );

        // when
        CompatibilityResult result = checker.check(need, offer);

        // then
        assertThat(result.compatible()).isFalse();

        assertThat(result.failureReasons())
                .containsExactly(
                        CompatibilityFailureReason.SAME_COMPANY
                );
    }

    @Test
    void shouldCollectAllFailureReasons() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        CompatibilityRule firstFailedRule =
                mock(CompatibilityRule.class);

        CompatibilityRule secondFailedRule =
                mock(CompatibilityRule.class);

        when(firstFailedRule.isSatisfied(need, offer))
                .thenReturn(false);

        when(firstFailedRule.failureReason())
                .thenReturn(
                        CompatibilityFailureReason.SAME_COMPANY
                );

        when(secondFailedRule.isSatisfied(need, offer))
                .thenReturn(false);

        when(secondFailedRule.failureReason())
                .thenReturn(
                        CompatibilityFailureReason
                                .INCOMPATIBLE_COOPERATION_TYPE
                );

        CompatibilityChecker checker = new CompatibilityChecker(
                List.of(firstFailedRule, secondFailedRule)
        );

        // when
        CompatibilityResult result = checker.check(need, offer);

        // then
        assertThat(result.compatible()).isFalse();

        assertThat(result.failureReasons())
                .containsExactly(
                        CompatibilityFailureReason.SAME_COMPANY,
                        CompatibilityFailureReason
                                .INCOMPATIBLE_COOPERATION_TYPE
                );
    }

    @Test
    void shouldReturnBooleanCompatibility() {
        // given
        BusinessNeed need = mock(BusinessNeed.class);
        BusinessOffer offer = mock(BusinessOffer.class);

        CompatibilityRule rule = mock(CompatibilityRule.class);

        when(rule.isSatisfied(need, offer))
                .thenReturn(true);

        CompatibilityChecker checker = new CompatibilityChecker(
                List.of(rule)
        );

        // when
        boolean compatible = checker.isCompatible(need, offer);

        // then
        assertThat(compatible).isTrue();
    }
}
