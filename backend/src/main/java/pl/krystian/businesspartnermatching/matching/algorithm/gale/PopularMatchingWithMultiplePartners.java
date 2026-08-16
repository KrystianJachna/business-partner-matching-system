package pl.krystian.businesspartnermatching.matching.algorithm.gale;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.matching.algorithm.MatchingAlgorithm;
import pl.krystian.businesspartnermatching.matching.algorithm.model.Match;
import pl.krystian.businesspartnermatching.matching.algorithm.model.MatchingProblem;
import pl.krystian.businesspartnermatching.matching.algorithm.model.PopularMatchingResult;

import java.util.*;

@Component
public class PopularMatchingWithMultiplePartners<L, R> implements MatchingAlgorithm<L, R> {

    @Override
    public PopularMatchingResult<L, R> match(MatchingProblem<L, R> problem) {
        Map<LeftCopy<L>, List<R>> N_H = initializeN_H(problem); //set of neighbors for each a^i copy in H
        Map<R, Set<LeftCopy<L>>> M = initializeM(problem);
        Deque<LeftCopy<L>> Q = initializeQ(problem); // queue of active copies
        Map<LeftCopy<L>, Set<R>> P = initializeP(problem); // previous proposal recipient of copy a^i
        Map<L, Integer> residual = initializeResidual(problem);


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
        return createResult(M);
    }

    private PopularMatchingResult<L, R> createResult(Map<R, Set<LeftCopy<L>>> M) {
        Set<Match<L, R>> matches = new LinkedHashSet<>();

        for (Map.Entry<R, Set<LeftCopy<L>>> entry : M.entrySet()) {
            R b = entry.getKey();

            for (LeftCopy<L> copy : entry.getValue()) {
                matches.add(new Match<>(copy.participant(), b));
            }
        }

        return new PopularMatchingResult<>(List.copyOf(matches));
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
        Map<R, Set<LeftCopy<L>>> M = new HashMap<>();

        for (R b : problem.rightPreferences().participants()) {
            M.put(b, new HashSet<>());
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
