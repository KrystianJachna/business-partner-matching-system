package pl.krystian.businesspartnermatching.catalog.specialization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.catalog.specialization.exception.SpecializationNotFoundException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SpecializationResolver {

    private final SpecializationRepository specializationRepository;

    public Set<Specialization> resolveActive(Set<Long> requestedIds) {
        Set<Specialization> foundSpecializations =
                specializationRepository.findAllByIdInAndActiveTrue(
                        requestedIds
                );

        Set<Long> foundIds = foundSpecializations.stream()
                .map(Specialization::getId)
                .collect(Collectors.toSet());

        Set<Long> missingIds = new HashSet<>(requestedIds);
        missingIds.removeAll(foundIds);

        if (!missingIds.isEmpty()) {
            throw new SpecializationNotFoundException(missingIds);
        }

        return foundSpecializations;
    }
}
