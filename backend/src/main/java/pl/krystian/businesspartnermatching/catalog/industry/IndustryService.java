package pl.krystian.businesspartnermatching.catalog.industry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.krystian.businesspartnermatching.catalog.industry.dto.IndustryResponse;

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