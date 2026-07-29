package pl.krystian.businesspartnermatching.matching.algorithm.gale;

import org.springframework.stereotype.Component;
import pl.krystian.businesspartnermatching.matching.algorithm.MatchingAlgorithm;
import pl.krystian.businesspartnermatching.matching.algorithm.model.Match;
import pl.krystian.businesspartnermatching.matching.algorithm.model.MatchingProblem;
import pl.krystian.businesspartnermatching.matching.algorithm.model.PopularMatchingResult;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Generalized two-level Gale-Shapley algorithm from the supplied paper.
 *
 * <p>The algorithm works on two copies of every left participant. Level 0
 * proposes first. Once it cannot fill its residual capacity, level 1 is
 * activated. Right participants prefer every level-1 copy to every level-0
 * copy, while preserving their original order within each level.</p>
 */
@Component("twoLevelPopularMatchingAlgorithm")
public class TwoLevelPopularMatchingAlgorithm<L, R>
        implements MatchingAlgorithm<L, R> {

    private static final int LEVEL_ZERO = 0;
    private static final int LEVEL_ONE = 1;

    @Override
    public PopularMatchingResult<L, R> match(
            MatchingProblem<L, R> problem
    ) {
        Objects.requireNonNull(
                problem,
                "Matching problem cannot be null"
        );

        List<LeftCopy<L>> copies = createCopies(problem);
        Map<L, Integer> residualCapacity = initializeResidualCapacity(problem);
        Map<LeftCopy<L>, List<R>> candidateLists =
                createCandidateLists(problem);
        Map<LeftCopy<L>, Integer> nextProposalPositions =
                initializeProposalPositions(copies);
        Map<R, Set<LeftCopy<L>>> matchesByRight =
                initializeMatchesByRight(problem);
        Map<R, Map<LeftCopy<L>, Integer>> rightRanks =
                createRightRanks(problem, candidateLists);
        Set<Edge<L, R>> removedEdges = new HashSet<>();
        Deque<LeftCopy<L>> queue = new ArrayDeque<>();

        for (L participant : problem.leftPreferences().participants()) {
            LeftCopy<L> levelZeroCopy =
                    new LeftCopy<>(participant, LEVEL_ZERO);

            if (hasUnproposedCandidate(
                    levelZeroCopy,
                    candidateLists,
                    nextProposalPositions,
                    removedEdges
            )) {
                queue.addLast(levelZeroCopy);
            }
        }

        while (!queue.isEmpty()) {
            LeftCopy<L> copy = queue.removeFirst();

            processProposals(
                    copy,
                    problem,
                    candidateLists,
                    nextProposalPositions,
                    residualCapacity,
                    matchesByRight,
                    rightRanks,
                    removedEdges,
                    queue,
                    copies
            );
        }

        return createResult(matchesByRight);
    }

    private void processProposals(
            LeftCopy<L> copy,
            MatchingProblem<L, R> problem,
            Map<LeftCopy<L>, List<R>> candidateLists,
            Map<LeftCopy<L>, Integer> nextProposalPositions,
            Map<L, Integer> residualCapacity,
            Map<R, Set<LeftCopy<L>>> matchesByRight,
            Map<R, Map<LeftCopy<L>, Integer>> rightRanks,
            Set<Edge<L, R>> removedEdges,
            Deque<LeftCopy<L>> queue,
            List<LeftCopy<L>> allCopies
    ) {
        while (
                residualCapacity.get(copy.participant()) > 0
                        && hasUnproposedCandidate(
                        copy,
                        candidateLists,
                        nextProposalPositions,
                        removedEdges
                )
        ) {
            R rightParticipant = nextCandidate(
                    copy,
                    candidateLists,
                    nextProposalPositions,
                    removedEdges
            );

            processProposal(
                    copy,
                    rightParticipant,
                    problem,
                    candidateLists,
                    nextProposalPositions,
                    residualCapacity,
                    matchesByRight,
                    rightRanks,
                    removedEdges,
                    queue,
                    allCopies
            );
        }

        if (
                copy.level() == LEVEL_ZERO
                        && residualCapacity.get(copy.participant()) > 0
        ) {
            LeftCopy<L> levelOneCopy =
                    new LeftCopy<>(copy.participant(), LEVEL_ONE);

            if (!queue.contains(levelOneCopy)) {
                queue.addLast(levelOneCopy);
            }
        }
    }

    private void processProposal(
            LeftCopy<L> copy,
            R rightParticipant,
            MatchingProblem<L, R> problem,
            Map<LeftCopy<L>, List<R>> candidateLists,
            Map<LeftCopy<L>, Integer> nextProposalPositions,
            Map<L, Integer> residualCapacity,
            Map<R, Set<LeftCopy<L>>> matchesByRight,
            Map<R, Map<LeftCopy<L>, Integer>> rightRanks,
            Set<Edge<L, R>> removedEdges,
            Deque<LeftCopy<L>> queue,
            List<LeftCopy<L>> allCopies
    ) {
        Set<LeftCopy<L>> currentMatches =
                matchesByRight.get(rightParticipant);

        if (copy.level() == LEVEL_ONE) {
            LeftCopy<L> levelZeroCopy =
                    new LeftCopy<>(copy.participant(), LEVEL_ZERO);

            if (!currentMatches.remove(levelZeroCopy)) {
                residualCapacity.merge(copy.participant(), -1, Integer::sum);
            }

            currentMatches.add(copy);
        } else {
            residualCapacity.merge(copy.participant(), -1, Integer::sum);
            currentMatches.add(copy);
        }

        int rightCapacity =
                problem.rightCapacities().capacityOf(rightParticipant);

        if (currentMatches.size() > rightCapacity) {
            LeftCopy<L> rejectedCopy = findWorstMatch(
                    currentMatches,
                    rightRanks.get(rightParticipant)
            );

            currentMatches.remove(rejectedCopy);
            residualCapacity.merge(
                    rejectedCopy.participant(),
                    1,
                    Integer::sum
            );

            enqueueIfPossible(
                    rejectedCopy,
                    queue,
                    residualCapacity,
                    candidateLists,
                    nextProposalPositions,
                    removedEdges
            );
        }

        if (currentMatches.size() == rightCapacity) {
            LeftCopy<L> worstMatch = findWorstMatch(
                    currentMatches,
                    rightRanks.get(rightParticipant)
            );
            int worstRank = rightRanks
                    .get(rightParticipant)
                    .get(worstMatch);

            for (LeftCopy<L> candidate : allCopies) {
                Integer candidateRank = rightRanks
                        .get(rightParticipant)
                        .get(candidate);

                if (candidateRank != null && candidateRank > worstRank) {
                    removedEdges.add(
                            new Edge<>(candidate.participant(), rightParticipant)
                    );
                }
            }
        }
    }

    private void enqueueIfPossible(
            LeftCopy<L> copy,
            Deque<LeftCopy<L>> queue,
            Map<L, Integer> residualCapacity,
            Map<LeftCopy<L>, List<R>> candidateLists,
            Map<LeftCopy<L>, Integer> nextProposalPositions,
            Set<Edge<L, R>> removedEdges
    ) {
        if (queue.contains(copy)) {
            return;
        }

        if (residualCapacity.get(copy.participant()) <= 0) {
            return;
        }

        boolean hasRemainingCandidate = hasUnproposedCandidate(
                copy,
                candidateLists,
                nextProposalPositions,
                removedEdges
        );

        if (hasRemainingCandidate) {
            queue.addLast(copy);
        }
    }

    private LeftCopy<L> findWorstMatch(
            Set<LeftCopy<L>> currentMatches,
            Map<LeftCopy<L>, Integer> ranks
    ) {
        return currentMatches.stream()
                .max(Comparator.comparingInt(ranks::get))
                .orElseThrow(() -> new IllegalStateException(
                        "Cannot find the worst match in an empty match set"
                ));
    }

    private R nextCandidate(
            LeftCopy<L> copy,
            Map<LeftCopy<L>, List<R>> candidateLists,
            Map<LeftCopy<L>, Integer> nextProposalPositions,
            Set<Edge<L, R>> removedEdges
    ) {
        List<R> candidates = candidateLists.get(copy);
        int position = nextProposalPositions.get(copy);

        while (position < candidates.size()) {
            R candidate = candidates.get(position++);
            nextProposalPositions.put(copy, position);

            if (!removedEdges.contains(
                    new Edge<>(copy.participant(), candidate)
            )) {
                return candidate;
            }
        }

        nextProposalPositions.put(copy, position);
        throw new IllegalStateException(
                "No unproposed candidate is available"
        );
    }

    private boolean hasUnproposedCandidate(
            LeftCopy<L> copy,
            Map<LeftCopy<L>, List<R>> candidateLists,
            Map<LeftCopy<L>, Integer> nextProposalPositions,
            Set<Edge<L, R>> removedEdges
    ) {
        List<R> candidates = candidateLists.get(copy);
        int position = nextProposalPositions.get(copy);

        while (position < candidates.size()) {
            R candidate = candidates.get(position);

            if (!removedEdges.contains(
                    new Edge<>(copy.participant(), candidate)
            )) {
                return true;
            }

            position++;
            nextProposalPositions.put(copy, position);
        }

        return false;
    }

    private List<LeftCopy<L>> createCopies(
            MatchingProblem<L, R> problem
    ) {
        List<LeftCopy<L>> copies = new ArrayList<>();

        for (L participant : problem.leftPreferences().participants()) {
            copies.add(new LeftCopy<>(participant, LEVEL_ZERO));
            copies.add(new LeftCopy<>(participant, LEVEL_ONE));
        }

        return copies;
    }

    private Map<L, Integer> initializeResidualCapacity(
            MatchingProblem<L, R> problem
    ) {
        Map<L, Integer> residualCapacity = new LinkedHashMap<>();

        for (L participant : problem.leftPreferences().participants()) {
            residualCapacity.put(
                    participant,
                    problem.leftCapacities().capacityOf(participant)
            );
        }

        return residualCapacity;
    }

    private Map<LeftCopy<L>, List<R>> createCandidateLists(
            MatchingProblem<L, R> problem
    ) {
        Map<LeftCopy<L>, List<R>> candidateLists = new LinkedHashMap<>();

        for (L leftParticipant : problem.leftPreferences().participants()) {
            List<R> candidates = problem.leftPreferences()
                    .getFor(leftParticipant)
                    .preferredCandidates()
                    .stream()
                    .filter(rightParticipant ->
                            problem.rightPreferences()
                                    .containsParticipant(rightParticipant)
                                    && problem.rightPreferences()
                                    .getFor(rightParticipant)
                                    .contains(leftParticipant)
                    )
                    .toList();

            candidateLists.put(
                    new LeftCopy<>(leftParticipant, LEVEL_ZERO),
                    candidates
            );
            candidateLists.put(
                    new LeftCopy<>(leftParticipant, LEVEL_ONE),
                    candidates
            );
        }

        return candidateLists;
    }

    private Map<LeftCopy<L>, Integer> initializeProposalPositions(
            Collection<LeftCopy<L>> copies
    ) {
        Map<LeftCopy<L>, Integer> positions = new HashMap<>();

        for (LeftCopy<L> copy : copies) {
            positions.put(copy, 0);
        }

        return positions;
    }

    private Map<R, Set<LeftCopy<L>>> initializeMatchesByRight(
            MatchingProblem<L, R> problem
    ) {
        Map<R, Set<LeftCopy<L>>> matchesByRight = new LinkedHashMap<>();

        for (R rightParticipant : problem.rightPreferences().participants()) {
            matchesByRight.put(rightParticipant, new LinkedHashSet<>());
        }

        return matchesByRight;
    }

    private Map<R, Map<LeftCopy<L>, Integer>> createRightRanks(
            MatchingProblem<L, R> problem,
            Map<LeftCopy<L>, List<R>> candidateLists
    ) {
        Map<R, Map<LeftCopy<L>, Integer>> rightRanks = new LinkedHashMap<>();

        for (R rightParticipant : problem.rightPreferences().participants()) {
            Map<LeftCopy<L>, Integer> ranks = new HashMap<>();
            int rank = 0;

            for (int level : List.of(LEVEL_ONE, LEVEL_ZERO)) {
                for (L leftParticipant : problem.rightPreferences()
                        .getFor(rightParticipant)
                        .preferredCandidates()) {
                    LeftCopy<L> copy = new LeftCopy<>(leftParticipant, level);

                    if (candidateLists.containsKey(copy)
                            && candidateLists.get(copy).contains(rightParticipant)) {
                        ranks.put(copy, rank++);
                    }
                }
            }

            rightRanks.put(rightParticipant, ranks);
        }

        return rightRanks;
    }

    private PopularMatchingResult<L, R> createResult(
            Map<R, Set<LeftCopy<L>>> matchesByRight
    ) {
        Set<Match<L, R>> matches = new LinkedHashSet<>();

        for (Map.Entry<R, Set<LeftCopy<L>>> entry
                : matchesByRight.entrySet()) {
            for (LeftCopy<L> copy : entry.getValue()) {
                matches.add(
                        new Match<>(
                                copy.participant(),
                                entry.getKey()
                        )
                );
            }
        }

        return new PopularMatchingResult<>(List.copyOf(matches));
    }

    private record LeftCopy<L>(L participant, int level) {
    }

    private record Edge<L, R>(L leftParticipant, R rightParticipant) {
    }
}
