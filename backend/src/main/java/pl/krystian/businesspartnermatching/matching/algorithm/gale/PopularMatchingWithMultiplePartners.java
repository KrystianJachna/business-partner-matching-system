package pl.krystian.businesspartnermatching.matching.algorithm.gale;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.krystian.businesspartnermatching.matching.algorithm.MatchingAlgorithm;
import pl.krystian.businesspartnermatching.matching.algorithm.model.Match;
import pl.krystian.businesspartnermatching.matching.algorithm.model.MatchingProblem;
import pl.krystian.businesspartnermatching.matching.algorithm.model.PopularMatchingResult;
import pl.krystian.businesspartnermatching.need.model.entity.BusinessNeed;
import pl.krystian.businesspartnermatching.offer.model.entity.BusinessOffer;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PopularMatchingWithMultiplePartners<L, R> implements MatchingAlgorithm<L, R> {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PopularMatchingWithMultiplePartners.class);

    @Override
    public PopularMatchingResult<L, R> match(MatchingProblem<L, R> problem) {
        Map<LeftCopy<L>, List<R>> N_H = initializeN_H(problem); //set of neighbors for each a^i copy in H
        Map<R, Set<LeftCopy<L>>> M = initializeM(problem);
        Deque<LeftCopy<L>> Q = initializeQ(problem); // queue of active copies
        Map<LeftCopy<L>, Set<R>> P = initializeP(problem); // previous proposal recipient of copy a^i
        Map<L, Integer> residual = initializeResidual(problem);
        Map<L, String> leftGraphLabels = initializeGraphLabels(
                problem.leftPreferences().participants(),
                "a"
        );
        Map<R, String> rightGraphLabels = initializeGraphLabels(
                problem.rightPreferences().participants(),
                "b"
        );

        while (!Q.isEmpty()) {
            LeftCopy<L> ai = Q.removeFirst();

            while (residual.get(ai.participant()) > 0 && hasUnproposedNeighbor(ai, N_H, P)) {
                R b = getMostPreferredUnproposedNeighbor(ai, N_H, P);
                P.get(ai).add(b);
                M.get(b).add(ai);

                if (ai.level() == 1 && M.get(b).contains(new LeftCopy<>(ai.participant(), 0))) {
                    M.get(b).remove(new LeftCopy<>(ai.participant(), 0));
                } else {
                    residual.put(ai.participant(), residual.get(ai.participant()) - 1);
                    if (M.get(b).size() > problem.rightCapacities().capacityOf(b)) {
                        LeftCopy<L> vj = findWorstPartner(b, M, problem);
                        M.get(b).remove(vj);
                        residual.put(vj.participant(), residual.get(vj.participant()) + 1);
                        if (!Q.contains(vj)) {
                            Q.addLast(vj);
                        }
                    }
                }
                if (M.get(b).size() == problem.rightCapacities().capacityOf(b)) {
                    removeEdgesWorseThanWorstPartner(b, M, N_H, problem);
                }
            }
            if (residual.get(ai.participant()) > 0 && ai.level() == 0) {
                Q.addLast(new LeftCopy<>(ai.participant(), 1));
            }
        }
        return createResult(problem, M, leftGraphLabels, rightGraphLabels);
    }

    private PopularMatchingResult<L, R> createResult(
            MatchingProblem<L, R> problem,
            Map<R, Set<LeftCopy<L>>> M,
            Map<L, String> leftGraphLabels,
            Map<R, String> rightGraphLabels
    ) {
        Set<Match<L, R>> matches = new LinkedHashSet<>();

        for (Map.Entry<R, Set<LeftCopy<L>>> entry : M.entrySet()) {
            R b = entry.getKey();

            for (LeftCopy<L> copy : entry.getValue()) {
                matches.add(new Match<>(copy.participant(), b));
            }
        }

        LOGGER.info(
                "\n{}",
                formatGraphReport(
                        problem,
                        M,
                        leftGraphLabels,
                        rightGraphLabels
                )
        );

        return new PopularMatchingResult<>(List.copyOf(matches));
    }

    private String formatMatching(
            Map<R, Set<LeftCopy<L>>> M,
            Map<L, String> leftGraphLabels,
            Map<R, String> rightGraphLabels
    ) {
        StringJoiner pairs = new StringJoiner(
                ", ",
                "M = {",
                "}"
        );

        for (Map.Entry<R, Set<LeftCopy<L>>> entry : M.entrySet()) {
            for (LeftCopy<L> copy : entry.getValue()) {
                pairs.add(
                        "("
                                + leftGraphLabels.get(copy.participant())
                                + ", "
                                + rightGraphLabels.get(entry.getKey())
                                + ")"
                );
            }
        }

        return pairs.toString();
    }

    private <P> Map<P, String> initializeGraphLabels(
            Collection<P> participants,
            String prefix
    ) {
        Map<P, String> labels = new LinkedHashMap<>();
        int index = 1;

        for (P participant : participants) {
            String label = prefix + "_" + index;
            labels.put(participant, label);

            LOGGER.debug(
                    "Graph vertex {} represents {}",
                    label,
                    participant
            );

            index++;
        }

        return labels;
    }

    private String formatGraphReport(
            MatchingProblem<L, R> problem,
            Map<R, Set<LeftCopy<L>>> M,
            Map<L, String> leftGraphLabels,
            Map<R, String> rightGraphLabels
    ) {
        StringBuilder report = new StringBuilder();
        report.append("=== Matching report ===\n\n");

        report.append("Graph vertices:\n");
        report.append("  A (needs):\n");

        for (L a : problem.leftPreferences().participants()) {
            report.append("    ")
                    .append(leftGraphLabels.get(a))
                    .append(" = ")
                    .append(describeParticipant(a))
                    .append('\n');
        }

        report.append("  B (offers):\n");

        for (R b : problem.rightPreferences().participants()) {
            report.append("    ")
                    .append(rightGraphLabels.get(b))
                    .append(" = ")
                    .append(describeParticipant(b))
                    .append('\n');
        }

        report.append("\nPreference lists:\n");
        report.append("  A:\n");

        for (L a : problem.leftPreferences().participants()) {
            String preferredOffers = problem.leftPreferences()
                    .getFor(a)
                    .preferredCandidates()
                    .stream()
                    .map(rightGraphLabels::get)
                    .collect(Collectors.joining(" ≻ "));

            report.append("    ")
                    .append(leftGraphLabels.get(a))
                    .append(": ")
                    .append(preferredOffers.isEmpty() ? "-" : preferredOffers)
                    .append('\n');
        }

        report.append("  B:\n");

        for (R b : problem.rightPreferences().participants()) {
            String preferredNeeds = problem.rightPreferences()
                    .getFor(b)
                    .preferredCandidates()
                    .stream()
                    .map(leftGraphLabels::get)
                    .collect(Collectors.joining(" ≻ "));

            report.append("    ")
                    .append(rightGraphLabels.get(b))
                    .append(": ")
                    .append(preferredNeeds.isEmpty() ? "-" : preferredNeeds)
                    .append('\n');
        }

        int matchedPairs = M.values()
                .stream()
                .mapToInt(Set::size)
                .sum();

        report.append("\nFinal matching:\n")
                .append("  ")
                .append(formatMatching(M, leftGraphLabels, rightGraphLabels))
                .append('\n')
                .append("  Total matched pairs: ")
                .append(matchedPairs);

        return report.toString();
    }

    private String describeParticipant(Object participant) {
        if (participant instanceof BusinessNeed need) {
            String companyName = need.getCompany() == null
                    ? "unknown"
                    : need.getCompany().getName();

            return "BusinessNeed{id=%s, title='%s', company='%s'}"
                    .formatted(
                            need.getId(),
                            need.getTitle(),
                            companyName
                    );
        }

        if (participant instanceof BusinessOffer offer) {
            String companyName = offer.getCompany() == null
                    ? "unknown"
                    : offer.getCompany().getName();

            return "BusinessOffer{id=%s, title='%s', company='%s'}"
                    .formatted(
                            offer.getId(),
                            offer.getTitle(),
                            companyName
                    );
        }

        return String.valueOf(participant);
    }

    private void removeEdgesWorseThanWorstPartner(
            R b,
            Map<R, Set<LeftCopy<L>>> M,
            Map<LeftCopy<L>, List<R>> N_H,
            MatchingProblem<L, R> problem
    ) {
        LeftCopy<L> worstPartner = findWorstPartner(b, M, problem);
        int worstRank = rankForRight(b, worstPartner, problem);

        for (LeftCopy<L> copy : N_H.keySet()) {
            int candidateRank = rankForRight(b, copy, problem);

            if (candidateRank > worstRank) {
                N_H.get(copy).remove(b);
            }
        }
    }

    private int rankForRight(R b, LeftCopy<L> copy, MatchingProblem<L, R> problem) {
        List<L> preferences = problem.rightPreferences()
                .getFor(b)
                .preferredCandidates();

        int participantRank = preferences.indexOf(copy.participant());

        //Level-1 copies are preferred over all level-0 copies.
        if (copy.level() == 1) {
            return participantRank;
        }

        //This shifts level-0 copies below all level-1 copies.
        return preferences.size() + participantRank;
    }

    private LeftCopy<L> findWorstPartner(
            R b,
            Map<R, Set<LeftCopy<L>>> M,
            MatchingProblem<L, R> problem
    ) {
        return M.get(b)
                .stream()
                .max(Comparator.comparingInt(
                        copy -> rankForRight(b, copy, problem)
                ))
                .orElseThrow(() -> new IllegalStateException("Cannot find worst partner"));
    }

    private R getMostPreferredUnproposedNeighbor(
            LeftCopy<L> ai,
            Map<LeftCopy<L>, List<R>> N_H,
            Map<LeftCopy<L>, Set<R>> P
    ) {
        List<R> neighbors = N_H.get(ai);
        Set<R> proposed = P.get(ai);

        for (R b : neighbors) {
            if (!proposed.contains(b)) {
                return b;
            }
        }

        throw new IllegalStateException(
                "No unproposed neighbor is available"
        );
    }

    private boolean hasUnproposedNeighbor(
            LeftCopy<L> ai,
            Map<LeftCopy<L>, List<R>> N_H,
            Map<LeftCopy<L>, Set<R>> P
    ) {
        List<R> neighbors = N_H.get(ai);
        Set<R> proposed = P.get(ai);

        for (R b : neighbors) {
            if (!proposed.contains(b)) {
                return true;
            }
        }

        return false;
    }

    private Map<LeftCopy<L>, List<R>> initializeN_H(MatchingProblem<L, R> problem) {
        Map<LeftCopy<L>, List<R>> N_H = new HashMap<>();

        for (L a : problem.leftPreferences().participants()) {
            List<R> neighbors = new ArrayList<>();

            for (R b : problem.leftPreferences()
                    .getFor(a)
                    .preferredCandidates()) {

                boolean mutuallyAcceptable =
                        problem.rightPreferences().containsParticipant(b)
                                && problem.rightPreferences()
                                .getFor(b)
                                .contains(a);

                if (mutuallyAcceptable) {
                    neighbors.add(b);
                }
            }

            N_H.put(new LeftCopy<>(a, 0), new ArrayList<>(neighbors));
            N_H.put(new LeftCopy<>(a, 1), new ArrayList<>(neighbors));
        }
        return N_H;
    }

    private Deque<LeftCopy<L>> initializeQ(MatchingProblem<L, R> problem) {
        Deque<LeftCopy<L>> Q = new ArrayDeque<>();

        for (L a : problem.leftPreferences().participants()) {
            Q.addLast(new LeftCopy<>(a, 0));
        }

        return Q;
    }

    private Map<R, Set<LeftCopy<L>>> initializeM(MatchingProblem<L, R> problem) {
        Map<R, Set<LeftCopy<L>>> M = new LinkedHashMap<>();

        for (R b : problem.rightPreferences().participants()) {
            M.put(b, new LinkedHashSet<>());
        }

        return M;
    }

    private Map<LeftCopy<L>, Set<R>> initializeP(MatchingProblem<L, R> problem) {
        Map<LeftCopy<L>, Set<R>> P = new HashMap<>();

        for (L a : problem.leftPreferences().participants()) {
            P.put(
                    new LeftCopy<>(a, 0),
                    new HashSet<>()
            );

            P.put(
                    new LeftCopy<>(a, 1),
                    new HashSet<>()
            );
        }

        return P;
    }

    private Map<L, Integer> initializeResidual(MatchingProblem<L, R> problem) {
        Map<L, Integer> residual = new HashMap<>();

        for (L a : problem.leftPreferences().participants()) {
            residual.put(
                    a,
                    problem.leftCapacities().capacityOf(a)
            );
        }

        return residual;
    }

    private record LeftCopy<L>(
            L participant,
            int level
    ) {
    }
}
