package pl.krystian.businesspartnermatching.matching.preference.generation;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.matching.preference.model.ParticipantPreferences;
import pl.krystian.businesspartnermatching.matching.preference.model.Preference;

import java.util.List;
import java.util.Objects;

@Component
public class ParticipantPreferencesGenerator {

    public <P, C> ParticipantPreferences<P, C> generate(
            P participant,
            List<Preference<C>> ranking
    ) {
        Objects.requireNonNull(
                participant,
                "Preference participant cannot be null"
        );

        Objects.requireNonNull(
                ranking,
                "Preference ranking cannot be null"
        );

        if (ranking.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(
                    "Preference ranking cannot contain null"
            );
        }

        List<C> preferredCandidates = ranking
                .stream()
                .map(Preference::candidate)
                .toList();

        return new ParticipantPreferences<>(
                participant,
                preferredCandidates
        );
    }
}
