package pl.krystian.businesspartnermatching.catalog.industry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndustryService {

    private final IndustryRepository industryRepository;

    public List<Industry> getAllActiveIndustries() {
        return industryRepository.findAllByActiveTrueOrderByNameAsc();
    }
}