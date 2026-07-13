package pl.krystian.businesspartnermatching.catalog;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndustryService {

    private final IndustryRepository industryRepository;

    public IndustryService(IndustryRepository industryRepository) {
        this.industryRepository = industryRepository;
    }

    public List<Industry> getAllActiveIndustries() {
        return industryRepository.findAllByActiveTrueOrderByNameAsc();
    }
}