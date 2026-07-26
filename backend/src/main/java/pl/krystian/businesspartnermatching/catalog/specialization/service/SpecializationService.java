package pl.krystian.businesspartnermatching.catalog.specialization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krystian.businesspartnermatching.catalog.industry.repository.IndustryRepository;
import pl.krystian.businesspartnermatching.catalog.industry.exception.IndustryNotFoundException;
import pl.krystian.businesspartnermatching.catalog.specialization.model.dto.SpecializationGroupResponse;
import pl.krystian.businesspartnermatching.catalog.specialization.model.dto.SpecializationResponse;
import pl.krystian.businesspartnermatching.catalog.specialization.model.entity.Specialization;
import pl.krystian.businesspartnermatching.catalog.specialization.repository.SpecializationRepository;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional(readOnly = true)
    public List<SpecializationGroupResponse>
    getAllActiveSpecializationsGroupedByIndustry() {
        List<Specialization> specializations =
                specializationRepository
                        .findAllByActiveTrueOrderByIndustryNameAscNameAsc();

        Map<Long, IndustrySpecializations> groupedSpecializations =
                new LinkedHashMap<>();

        for (Specialization specialization : specializations) {
            Long industryId =
                    specialization.getIndustry().getId();

            IndustrySpecializations group =
                    groupedSpecializations.computeIfAbsent(
                            industryId,
                            ignored -> new IndustrySpecializations(
                                    industryId,
                                    specialization
                                            .getIndustry()
                                            .getName()
                            )
                    );

            group.specializations().add(
                    SpecializationResponse.from(
                            specialization
                    )
            );
        }

        return groupedSpecializations
                .values()
                .stream()
                .map(group -> new SpecializationGroupResponse(
                        group.industryId(),
                        group.industryName(),
                        group.specializations()
                ))
                .toList();
    }

    private record IndustrySpecializations(
            Long industryId,
            String industryName,
            List<SpecializationResponse> specializations
    ) {

        private IndustrySpecializations(
                Long industryId,
                String industryName
        ) {
            this(
                    industryId,
                    industryName,
                    new ArrayList<>()
            );
        }
    }
}
