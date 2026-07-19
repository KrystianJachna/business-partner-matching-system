package pl.krystian.businesspartnermatching.need;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;

import pl.krystian.businesspartnermatching.catalog.specialization.service.SpecializationResolver;
import pl.krystian.businesspartnermatching.common.cooperation.CooperationType;
import pl.krystian.businesspartnermatching.need.exception.BusinessNeedNotFoundException;
import pl.krystian.businesspartnermatching.catalog.specialization.exception.SpecializationNotFoundException;
import pl.krystian.businesspartnermatching.company.exception.CompanyNotFoundException;
import pl.krystian.businesspartnermatching.catalog.industry.model.entity.Industry;
import pl.krystian.businesspartnermatching.catalog.specialization.model.entity.Specialization;
import pl.krystian.businesspartnermatching.common.money.CurrencyCode;
import pl.krystian.businesspartnermatching.company.model.entity.Company;
import pl.krystian.businesspartnermatching.company.repository.CompanyRepository;
import pl.krystian.businesspartnermatching.need.model.dto.BusinessNeedResponse;
import pl.krystian.businesspartnermatching.need.model.dto.CreateBusinessNeedRequest;
import pl.krystian.businesspartnermatching.common.time.dto.DateRangeRequest;
import pl.krystian.businesspartnermatching.common.money.dto.MoneyRangeRequest;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.need.repository.BusinessNeedRepository;
import pl.krystian.businesspartnermatching.need.service.BusinessNeedService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusinessNeedServiceTest {

    @Mock
    private BusinessNeedRepository businessNeedRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private SpecializationResolver specializationResolver;

    private BusinessNeedService businessNeedService;

    @BeforeEach
    void setUp() {
        businessNeedService = new BusinessNeedService(
                businessNeedRepository,
                companyRepository,
                specializationResolver
        );
    }

    @Test
    void shouldCreateBusinessNeed() {
        Industry industry = new Industry(
                "IT",
                "Information Technology"
        );
        ReflectionTestUtils.setField(industry, "id", 100L);

        Specialization specialization = new Specialization(
                industry,
                "JAVA",
                "Java Development"
        );
        ReflectionTestUtils.setField(specialization, "id", 10L);

        Company company = new Company(
                "Example Company",
                "Example description",
                industry,
                Set.of(specialization),
                "Poland",
                "Krakow",
                LocalDate.of(2020, 1, 1),
                "Software development"
        );
        ReflectionTestUtils.setField(company, "id", 1L);

        CreateBusinessNeedRequest request =
                new CreateBusinessNeedRequest(
                        "Java development partner",
                        "Partner needed for a backend project",
                        CooperationType.OUTSOURCING,
                        Set.of(10L),
                        new MoneyRangeRequest(
                                new BigDecimal("20000.00"),
                                new BigDecimal("40000.00"),
                                CurrencyCode.PLN
                        ),
                        new DateRangeRequest(
                                LocalDate.of(2026, 8, 1),
                                LocalDate.of(2026, 12, 31)
                        ),
                        200,
                        3,
                        2
                );

        when(companyRepository.findById(1L))
                .thenReturn(Optional.of(company));

        when(specializationResolver.resolveActive(
                Set.of(10L)
        )).thenReturn(Set.of(specialization));

        when(businessNeedRepository.save(any(BusinessNeed.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BusinessNeedResponse response =
                businessNeedService.createBusinessNeed(1L, request);

        ArgumentCaptor<BusinessNeed> captor =
                ArgumentCaptor.forClass(BusinessNeed.class);

        verify(businessNeedRepository).save(captor.capture());

        BusinessNeed savedNeed = captor.getValue();

        assertThat(savedNeed.getCompany()).isSameAs(company);
        assertThat(savedNeed.getTitle())
                .isEqualTo("Java development partner");
        assertThat(savedNeed.getRequiredSpecializations())
                .containsExactly(specialization);
        assertThat(savedNeed.getBudget().getMin())
                .isEqualByComparingTo("20000.00");
        assertThat(savedNeed.getBudget().getMax())
                .isEqualByComparingTo("40000.00");
        assertThat(savedNeed.getBudget().getCurrency())
                .isEqualTo(CurrencyCode.PLN);
        assertThat(savedNeed.getRequiredPeriod().getFrom())
                .isEqualTo(LocalDate.of(2026, 8, 1));
        assertThat(savedNeed.getMaxPartners()).isEqualTo(2);

        assertThat(response.title())
                .isEqualTo("Java development partner");
    }

    @Test
    void shouldThrowExceptionWhenCompanyDoesNotExist() {
        CreateBusinessNeedRequest request =
                new CreateBusinessNeedRequest(
                        "Java development partner",
                        null,
                        CooperationType.OUTSOURCING,
                        Set.of(10L),
                        null,
                        null,
                        null,
                        null,
                        1
                );

        when(companyRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                businessNeedService.createBusinessNeed(1L, request)
        )
                .isInstanceOf(CompanyNotFoundException.class)
                .hasMessageContaining("1");

        verify(specializationResolver, never())
                .resolveActive(any());

        verify(businessNeedRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenSpecializationDoesNotExist() {
        Industry industry = new Industry(
                "IT",
                "Information Technology"
        );

        Company company = new Company(
                "Example Company",
                "Example description",
                industry,
                Set.of(),
                "Poland",
                "Krakow",
                LocalDate.of(2020, 1, 1),
                "Software development"
        );

        CreateBusinessNeedRequest request =
                new CreateBusinessNeedRequest(
                        "Java development partner",
                        null,
                        CooperationType.OUTSOURCING,
                        Set.of(10L, 20L),
                        null,
                        null,
                        null,
                        null,
                        1
                );

        when(companyRepository.findById(1L))
                .thenReturn(Optional.of(company));

        when(specializationResolver.resolveActive(
                Set.of(10L, 20L)
        )).thenThrow(
                new SpecializationNotFoundException(
                        Set.of(10L, 20L)
                )
        );

        assertThatThrownBy(() ->
                businessNeedService.createBusinessNeed(1L, request)
        )
                .isInstanceOf(SpecializationNotFoundException.class);

        verify(businessNeedRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenBusinessNeedDoesNotExist() {
        when(businessNeedRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                businessNeedService.getBusinessNeedById(100L)
        )
                .isInstanceOf(BusinessNeedNotFoundException.class)
                .hasMessageContaining("100");
    }

    @Test
    void shouldReturnBusinessNeedById() {
        Industry industry = new Industry(
                "IT",
                "Information Technology"
        );

        Specialization specialization = new Specialization(
                industry,
                "JAVA",
                "Java Development"
        );

        Company company = new Company(
                "Example Company",
                "Example description",
                industry,
                Set.of(specialization),
                "Poland",
                "Krakow",
                LocalDate.of(2020, 1, 1),
                "Software development"
        );

        ReflectionTestUtils.setField(company, "id", 1L);
        ReflectionTestUtils.setField(specialization, "id", 10L);

        BusinessNeed businessNeed = new BusinessNeed(
                company,
                "Java development partner",
                "Partner needed for a backend project",
                CooperationType.OUTSOURCING,
                Set.of(specialization),
                null,
                null,
                null,
                null,
                1
        );

        ReflectionTestUtils.setField(businessNeed, "id", 100L);

        when(businessNeedRepository.findById(100L))
                .thenReturn(Optional.of(businessNeed));

        BusinessNeedResponse response =
                businessNeedService.getBusinessNeedById(100L);

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.companyId()).isEqualTo(1L);
        assertThat(response.title())
                .isEqualTo("Java development partner");
        assertThat(response.requiredSpecializations())
                .hasSize(1);
    }
}
