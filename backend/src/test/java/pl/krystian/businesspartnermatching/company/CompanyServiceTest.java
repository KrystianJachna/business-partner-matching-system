package pl.krystian.businesspartnermatching.company;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krystian.businesspartnermatching.catalog.industry.Industry;
import pl.krystian.businesspartnermatching.catalog.industry.IndustryRepository;
import pl.krystian.businesspartnermatching.catalog.industry.exception.IndustryNotFoundException;
import pl.krystian.businesspartnermatching.catalog.specialization.Specialization;
import pl.krystian.businesspartnermatching.catalog.specialization.SpecializationRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {
    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private IndustryRepository industryRepository;

    @Mock
    private SpecializationRepository specializationRepository;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void createCompany_shouldCreateCompany() {
        CreateCompanyRequest request = new CreateCompanyRequest(
                "SoftCraft",
                "Firma tworząca systemy informatyczne",
                1L,
                Set.of(1L),
                "Poland",
                "Kraków",
                LocalDate.of(2018, 4, 10),
                "Zespół programistów Java i Spring"
        );

        Industry industry = mock(Industry.class);

        when(industry.getId()).thenReturn(1L);
        when(industry.getCode()).thenReturn("INFORMATION_TECHNOLOGY");
        when(industry.getName()).thenReturn("Technologie informatyczne");

        Specialization specialization = mock(Specialization.class);

        when(specialization.getId()).thenReturn(1L);
        when(specialization.getCode()).thenReturn("SOFTWARE_DEVELOPMENT");
        when(specialization.getName()).thenReturn("Tworzenie oprogramowania");
        when(specialization.getIndustry()).thenReturn(industry);

        when(industryRepository.findByIdAndActiveTrue(1L))
                .thenReturn(Optional.of(industry));
        when(specializationRepository.findAllByIdInAndActiveTrue(Set.of(1L)))
                .thenReturn(Set.of(specialization));
        when(companyRepository.save(any(Company.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CompanyResponse response = companyService.createCompany(request);
        assertThat(response.name()).isEqualTo("SoftCraft");
        assertThat(response.description())
                .isEqualTo("Firma tworząca systemy informatyczne");
        assertThat(response.industry().code())
                .isEqualTo("INFORMATION_TECHNOLOGY");
        assertThat(response.specializations())
                .hasSize(1);
        assertThat(response.city()).isEqualTo("Kraków");
        assertThat(response.active()).isTrue();

        verify(companyRepository).save(any(Company.class));
    }

    @Test
    void createCompany_shouldThrowIndustryNotFoundException() {
        CreateCompanyRequest request = new CreateCompanyRequest(
                "SoftCraft",
                "Firma tworząca systemy informatyczne",
                999L,
                Set.of(1L),
                "Poland",
                "Kraków",
                LocalDate.of(2018, 4, 10),
                "Zespół programistów Java i Spring"
        );

        when(industryRepository.findByIdAndActiveTrue(999L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.createCompany(request))
                .isInstanceOf(IndustryNotFoundException.class)
                .hasMessage("Industry with id 999 does not exist");

        verify(companyRepository, never()).save(any(Company.class));
    }
}