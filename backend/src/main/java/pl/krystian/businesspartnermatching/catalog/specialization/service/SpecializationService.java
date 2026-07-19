package pl.krystian.businesspartnermatching.catalog.specialization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.krystian.businesspartnermatching.catalog.industry.repository.IndustryRepository;
import pl.krystian.businesspartnermatching.catalog.industry.exception.IndustryNotFoundException;
import pl.krystian.businesspartnermatching.catalog.specialization.model.dto.SpecializationResponse;
import pl.krystian.businesspartnermatching.catalog.specialization.repository.SpecializationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecializationService {

    private final SpecializationRepository specializationRepository;
    private final IndustryRepository industryRepository;

    public List<SpecializationResponse> getActiveSpecializationsByIndustry(Long industryId) {
        if (!industryRepository.existsById(industryId)) {
            throw new IndustryNotFoundException(industryId);
        }

        return specializationRepository
                .findAllByIndustryIdAndActiveTrueOrderByNameAsc(industryId)
                .stream()
                .map(SpecializationResponse::from)
                .toList();
    }
}
