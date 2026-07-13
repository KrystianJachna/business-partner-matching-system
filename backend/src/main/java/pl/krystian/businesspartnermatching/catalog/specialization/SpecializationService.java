package pl.krystian.businesspartnermatching.catalog.specialization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecializationService {

    final SpecializationRepository specializationRepository;

    public List<Specialization> getActiveSpecializationsByIndustry(Long industryId) {
        return specializationRepository.findAllByIndustryIdAndActiveTrueOrderByNameAsc(industryId);
    }
}
