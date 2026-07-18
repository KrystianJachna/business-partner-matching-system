package pl.krystian.businesspartnermatching.offer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pl.krystian.businesspartnermatching.catalog.industry.Industry;
import pl.krystian.businesspartnermatching.catalog.specialization.Specialization;
import pl.krystian.businesspartnermatching.catalog.specialization.SpecializationRepository;
import pl.krystian.businesspartnermatching.catalog.specialization.exception.SpecializationNotFoundException;
import pl.krystian.businesspartnermatching.common.cooperation.CooperationType;
import pl.krystian.businesspartnermatching.common.money.CurrencyCode;
import pl.krystian.businesspartnermatching.common.money.dto.MoneyRangeRequest;
import pl.krystian.businesspartnermatching.common.time.dto.DateRangeRequest;
import pl.krystian.businesspartnermatching.company.Company;
import pl.krystian.businesspartnermatching.company.CompanyRepository;
import pl.krystian.businesspartnermatching.company.exception.CompanyNotFoundException;
import pl.krystian.businesspartnermatching.offer.dto.BusinessOfferResponse;
import pl.krystian.businesspartnermatching.offer.dto.CreateBusinessOfferRequest;
import pl.krystian.businesspartnermatching.offer.exception.BusinessOfferNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusinessOfferServiceTest {

    @Mock
    private BusinessOfferRepository businessOfferRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private SpecializationRepository specializationRepository;

    private BusinessOfferService businessOfferService;

    @BeforeEach
    void setUp() {
        businessOfferService = new BusinessOfferService(
                businessOfferRepository,
                companyRepository,
                specializationRepository
        );
    }

    @Test
    void shouldCreateBusinessOffer() {
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

        CreateBusinessOfferRequest request =
                new CreateBusinessOfferRequest(
                        "Tworzenie aplikacji mobilnych",
                        "Oferujemy kompleksową realizację aplikacji.",
                        CooperationType.OUTSOURCING,
                        Set.of(10L),
                        new MoneyRangeRequest(
                                new BigDecimal("25000.00"),
                                new BigDecimal("50000.00"),
                                CurrencyCode.PLN
                        ),
                        new DateRangeRequest(
                                LocalDate.of(2026, 8, 1),
                                LocalDate.of(2027, 3, 31)
                        ),
                        300,
                        3
                );

        when(companyRepository.findById(1L))
                .thenReturn(Optional.of(company));

        when(
                specializationRepository
                        .findAllByIdInAndActiveTrue(Set.of(10L))
        ).thenReturn(Set.of(specialization));

        when(
                businessOfferRepository.save(
                        any(BusinessOffer.class)
                )
        ).thenAnswer(invocation -> invocation.getArgument(0));

        BusinessOfferResponse response =
                businessOfferService.createBusinessOffer(
                        1L,
                        request
                );

        ArgumentCaptor<BusinessOffer> captor =
                ArgumentCaptor.forClass(BusinessOffer.class);

        verify(businessOfferRepository).save(captor.capture());

        BusinessOffer savedOffer = captor.getValue();

        assertThat(savedOffer.getCompany())
                .isSameAs(company);

        assertThat(savedOffer.getTitle())
                .isEqualTo("Tworzenie aplikacji mobilnych");

        assertThat(savedOffer.getOfferedSpecializations())
                .containsExactly(specialization);

        assertThat(savedOffer.getPriceRange().getMin())
                .isEqualByComparingTo("25000.00");

        assertThat(savedOffer.getPriceRange().getMax())
                .isEqualByComparingTo("50000.00");

        assertThat(savedOffer.getPriceRange().getCurrency())
                .isEqualTo(CurrencyCode.PLN);

        assertThat(
                savedOffer.getAvailabilityPeriod().getFrom()
        ).isEqualTo(LocalDate.of(2026, 8, 1));

        assertThat(
                savedOffer.getAvailabilityPeriod().getUntil()
        ).isEqualTo(LocalDate.of(2027, 3, 31));

        assertThat(savedOffer.getServiceRadiusKm())
                .isEqualTo(300);

        assertThat(savedOffer.getMaxPartners())
                .isEqualTo(3);

        assertThat(savedOffer.isActive())
                .isTrue();

        assertThat(response.title())
                .isEqualTo("Tworzenie aplikacji mobilnych");

        assertThat(response.priceCurrency())
                .isEqualTo(CurrencyCode.PLN);
    }

    @Test
    void shouldThrowExceptionWhenCompanyDoesNotExist() {
        CreateBusinessOfferRequest request =
                new CreateBusinessOfferRequest(
                        "Tworzenie aplikacji mobilnych",
                        null,
                        CooperationType.OUTSOURCING,
                        Set.of(10L),
                        null,
                        null,
                        null,
                        1
                );

        when(companyRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> businessOfferService.createBusinessOffer(
                        1L,
                        request
                )
        )
                .isInstanceOf(CompanyNotFoundException.class)
                .hasMessageContaining("1");

        verify(specializationRepository, never())
                .findAllByIdInAndActiveTrue(any());

        verify(businessOfferRepository, never())
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

        CreateBusinessOfferRequest request =
                new CreateBusinessOfferRequest(
                        "Tworzenie aplikacji mobilnych",
                        null,
                        CooperationType.OUTSOURCING,
                        Set.of(10L, 20L),
                        null,
                        null,
                        null,
                        1
                );

        when(companyRepository.findById(1L))
                .thenReturn(Optional.of(company));

        when(
                specializationRepository
                        .findAllByIdInAndActiveTrue(
                                Set.of(10L, 20L)
                        )
        ).thenReturn(Set.of());

        assertThatThrownBy(
                () -> businessOfferService.createBusinessOffer(
                        1L,
                        request
                )
        ).isInstanceOf(
                SpecializationNotFoundException.class
        );

        verify(businessOfferRepository, never())
                .save(any());
    }

    @Test
    void shouldReturnBusinessOfferById() {
        Industry industry = new Industry(
                "IT",
                "Information Technology"
        );

        Specialization specialization = new Specialization(
                industry,
                "JAVA",
                "Java Development"
        );
        ReflectionTestUtils.setField(
                specialization,
                "id",
                10L
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

        BusinessOffer businessOffer = new BusinessOffer(
                company,
                "Tworzenie aplikacji mobilnych",
                "Realizacja aplikacji mobilnych.",
                CooperationType.OUTSOURCING,
                Set.of(specialization),
                null,
                null,
                null,
                1
        );
        ReflectionTestUtils.setField(
                businessOffer,
                "id",
                100L
        );

        when(businessOfferRepository.findById(100L))
                .thenReturn(Optional.of(businessOffer));

        BusinessOfferResponse response =
                businessOfferService.getBusinessOfferById(100L);

        assertThat(response.id())
                .isEqualTo(100L);

        assertThat(response.companyId())
                .isEqualTo(1L);

        assertThat(response.title())
                .isEqualTo("Tworzenie aplikacji mobilnych");

        assertThat(response.offeredSpecializations())
                .hasSize(1);
    }

    @Test
    void shouldThrowExceptionWhenBusinessOfferDoesNotExist() {
        when(businessOfferRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> businessOfferService
                        .getBusinessOfferById(100L)
        )
                .isInstanceOf(
                        BusinessOfferNotFoundException.class
                )
                .hasMessageContaining("100");
    }
}
