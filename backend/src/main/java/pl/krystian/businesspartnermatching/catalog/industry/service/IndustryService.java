package pl.krystian.businesspartnermatching.catalog.industry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.krystian.businesspartnermatching.catalog.industry.repository.IndustryRepository;
import pl.krystian.businesspartnermatching.catalog.industry.model.dto.IndustryResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndustryService {

    private final IndustryRepository industryRepository;

    public List<IndustryResponse> getAllActiveIndustries() {
        return industryRepository
                .findAllByActiveTrueOrderByNameAsc()
                .stream()
                .map(IndustryResponse::from)
                .toList();
    }
}
