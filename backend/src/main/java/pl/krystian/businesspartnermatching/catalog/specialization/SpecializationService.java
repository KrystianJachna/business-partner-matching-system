package pl.krystian.businesspartnermatching.catalog.specialization;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.krystian.businesspartnermatching.catalog.industry.IndustryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecializationService {

    private final SpecializationRepository specializationRepository;
    private final IndustryRepository industryRepository;

    public List<SpecializationResponse> getActiveSpecializationsByIndustry(Long industryId) {
        if (!industryRepository.existsById(industryId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Industry with id " + industryId + " does not exist"
            );
        }

        return specializationRepository
                .findAllByIndustryIdAndActiveTrueOrderByNameAsc(industryId)
                .stream()
                .map(SpecializationResponse::from)
                .toList();
    }
}
