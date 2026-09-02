package pl.krystian.businesspartnermatching.company;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krystian.businesspartnermatching.catalog.industry.model.entity.Industry;
import pl.krystian.businesspartnermatching.catalog.industry.repository.IndustryRepository;
import pl.krystian.businesspartnermatching.catalog.industry.exception.IndustryNotFoundException;
import pl.krystian.businesspartnermatching.company.model.dto.CompanyResponse;
import pl.krystian.businesspartnermatching.company.model.dto.CreateCompanyRequest;
import pl.krystian.businesspartnermatching.company.model.entity.Company;
import pl.krystian.businesspartnermatching.company.repository.CompanyRepository;
import pl.krystian.businesspartnermatching.company.service.CompanyService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

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

    @InjectMocks
    private CompanyService companyService;

    @Test
    void createCompany_shouldCreateCompany() {
        CreateCompanyRequest request = new CreateCompanyRequest(
                "SoftCraft",
                "Firma tworząca systemy informatyczne",
                1L,
                "Poland",
                "Kraków",
                new BigDecimal("50.064650"),
                new BigDecimal("19.944980"),
                LocalDate.of(2018, 4, 10),
                "Zespół programistów Java i Spring"
        );

        Industry industry = mock(Industry.class);

        when(industry.getId()).thenReturn(1L);
        when(industry.getCode()).thenReturn("INFORMATION_TECHNOLOGY");
        when(industry.getName()).thenReturn("Technologie informatyczne");

        when(industryRepository.findByIdAndActiveTrue(1L))
                .thenReturn(Optional.of(industry));
        when(companyRepository.save(any(Company.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CompanyResponse response = companyService.createCompany(request);
        assertThat(response.name()).isEqualTo("SoftCraft");
        assertThat(response.description())
                .isEqualTo("Firma tworząca systemy informatyczne");
        assertThat(response.industry().code())
                .isEqualTo("INFORMATION_TECHNOLOGY");
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
                "Poland",
                "Kraków",
                new BigDecimal("50.064650"),
                new BigDecimal("19.944980"),
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
