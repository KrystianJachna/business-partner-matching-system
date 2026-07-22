package pl.krystian.businesspartnermatching.matching.algorithm.gale;

import pl.krystian.businesspartnermatching.matching.algorithm.PopularMatchingAlgorithm;
import pl.krystian.businesspartnermatching.matching.algorithm.model.Match;
import pl.krystian.businesspartnermatching.matching.algorithm.model.MatchingProblem;
import pl.krystian.businesspartnermatching.matching.algorithm.model.PopularMatchingResult;
import pl.krystian.businesspartnermatching.matching.preference.model.ParticipantPreferences;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ManyToManyGaleShapleyAlgorithm<L, R>
        implements PopularMatchingAlgorithm<L, R> {

    @Override
    public PopularMatchingResult<L, R> match(
            MatchingProblem<L, R> problem
    ) {
        Objects.requireNonNull(
                problem,
                "Matching problem cannot be null"
        );

        Map<L, Set<R>> matchesByLeftParticipant =
                initializeMatches(problem.leftPreferences().participants());

        Map<R, Set<L>> matchesByRightParticipant =
                initializeMatches(problem.rightPreferences().participants());

        Map<L, Integer> nextProposalPositions =
                initializeProposalPositions(
                        problem.leftPreferences().participants()
                );

        Deque<L> proposingParticipants =
                initializeProposingParticipants(problem);

        while (!proposingParticipants.isEmpty()) {
            L leftParticipant = proposingParticipants.removeFirst();

            processProposals(
                    leftParticipant,
                    problem,
                    matchesByLeftParticipant,
                    matchesByRightParticipant,
                    nextProposalPositions,
                    proposingParticipants
            );
        }

        return createResult(matchesByLeftParticipant);
    }

    private void processProposals(
            L leftParticipant,
            MatchingProblem<L, R> problem,
            Map<L, Set<R>> matchesByLeftParticipant,
            Map<R, Set<L>> matchesByRightParticipant,
            Map<L, Integer> nextProposalPositions,
            Deque<L> proposingParticipants
    ) {
        ParticipantPreferences<L, R> leftParticipantPreferences =
                problem.leftPreferences().getFor(leftParticipant);

        List<R> preferredRightParticipants =
                leftParticipantPreferences.preferredCandidates();

        Set<R> currentLeftMatches =
                matchesByLeftParticipant.get(leftParticipant);

        int leftCapacity =
                problem.leftCapacities().capacityOf(leftParticipant);

        int nextProposalPosition =
                nextProposalPositions.get(leftParticipant);

        while (
                currentLeftMatches.size() < leftCapacity
                        && nextProposalPosition
                        < preferredRightParticipants.size()
        ) {
            R rightParticipant =
                    preferredRightParticipants.get(nextProposalPosition);

            nextProposalPosition++;

            processProposal(
                    leftParticipant,
                    rightParticipant,
                    problem,
                    matchesByLeftParticipant,
                    matchesByRightParticipant,
                    nextProposalPositions,
                    proposingParticipants
            );
        }

        nextProposalPositions.put(
                leftParticipant,
                nextProposalPosition
        );
    }

    private void processProposal(
            L leftParticipant,
            R rightParticipant,
            MatchingProblem<L, R> problem,
            Map<L, Set<R>> matchesByLeftParticipant,
            Map<R, Set<L>> matchesByRightParticipant,
            Map<L, Integer> nextProposalPositions,
            Deque<L> proposingParticipants
    ) {
        if (!problem.rightPreferences().containsParticipant(rightParticipant)) {
            return;
        }

        ParticipantPreferences<R, L> rightParticipantPreferences =
                problem.rightPreferences().getFor(rightParticipant);

        if (!rightParticipantPreferences.contains(leftParticipant)) {
            return;
        }

        Set<L> currentRightMatches =
                matchesByRightParticipant.get(rightParticipant);

        int rightCapacity =
                problem.rightCapacities().capacityOf(rightParticipant);

        if (currentRightMatches.size() < rightCapacity) {
            addMatch(
                    leftParticipant,
                    rightParticipant,
                    matchesByLeftParticipant,
                    matchesByRightParticipant
            );

            return;
        }

        L worstCurrentLeftParticipant =
                findWorstCurrentMatch(
                        currentRightMatches,
                        rightParticipantPreferences
                );

        if (!rightParticipantPreferences.prefers(
                leftParticipant,
                worstCurrentLeftParticipant
        )) {
            return;
        }

        removeMatch(
                worstCurrentLeftParticipant,
                rightParticipant,
                matchesByLeftParticipant,
                matchesByRightParticipant
        );

        addMatch(
                leftParticipant,
                rightParticipant,
                matchesByLeftParticipant,
                matchesByRightParticipant
        );

        addToProposalQueueIfPossible(
                worstCurrentLeftParticipant,
                problem,
                matchesByLeftParticipant,
                nextProposalPositions,
                proposingParticipants
        );
    }

    private L findWorstCurrentMatch(
            Set<L> currentMatches,
            ParticipantPreferences<R, L> preferences
    ) {
        return currentMatches.stream()
                .max((firstParticipant, secondParticipant) ->
                        Integer.compare(
                                preferences.positionOf(firstParticipant),
                                preferences.positionOf(secondParticipant)
                        )
                )
                .orElseThrow(() -> new IllegalStateException(
                        "Cannot find the worst match in an empty match set"
                ));
    }

    private void addMatch(
            L leftParticipant,
            R rightParticipant,
            Map<L, Set<R>> matchesByLeftParticipant,
            Map<R, Set<L>> matchesByRightParticipant
    ) {
        matchesByLeftParticipant
                .get(leftParticipant)
                .add(rightParticipant);

        matchesByRightParticipant
                .get(rightParticipant)
                .add(leftParticipant);
    }

    private void removeMatch(
            L leftParticipant,
            R rightParticipant,
            Map<L, Set<R>> matchesByLeftParticipant,
            Map<R, Set<L>> matchesByRightParticipant
    ) {
        matchesByLeftParticipant
                .get(leftParticipant)
                .remove(rightParticipant);

        matchesByRightParticipant
                .get(rightParticipant)
                .remove(leftParticipant);
    }

    private void addToProposalQueueIfPossible(
            L leftParticipant,
            MatchingProblem<L, R> problem,
            Map<L, Set<R>> matchesByLeftParticipant,
            Map<L, Integer> nextProposalPositions,
            Deque<L> proposingParticipants
    ) {
        int currentMatchCount =
                matchesByLeftParticipant.get(leftParticipant).size();

        int capacity =
                problem.leftCapacities().capacityOf(leftParticipant);

        int nextProposalPosition =
                nextProposalPositions.get(leftParticipant);

        int preferenceCount =
                problem.leftPreferences()
                        .getFor(leftParticipant)
                        .size();

        boolean hasFreeCapacity =
                currentMatchCount < capacity;

        boolean hasRemainingCandidates =
                nextProposalPosition < preferenceCount;

        if (
                hasFreeCapacity
                        && hasRemainingCandidates
                        && !proposingParticipants.contains(leftParticipant)
        ) {
            proposingParticipants.addLast(leftParticipant);
        }
    }

    private Deque<L> initializeProposingParticipants(
            MatchingProblem<L, R> problem
    ) {
        Deque<L> proposingParticipants = new ArrayDeque<>();

        for (L leftParticipant
                : problem.leftPreferences().participants()) {

            if (
                    !problem.leftPreferences()
                            .getFor(leftParticipant)
                            .isEmpty()
            ) {
                proposingParticipants.addLast(leftParticipant);
            }
        }

        return proposingParticipants;
    }

    private <P, C> Map<P, Set<C>> initializeMatches(
            Iterable<P> participants
    ) {
        Map<P, Set<C>> matchesByParticipant = new HashMap<>();

        for (P participant : participants) {
            matchesByParticipant.put(
                    participant,
                    new LinkedHashSet<>()
            );
        }

        return matchesByParticipant;
    }

    private Map<L, Integer> initializeProposalPositions(
            Iterable<L> leftParticipants
    ) {
        Map<L, Integer> proposalPositions = new HashMap<>();

        for (L leftParticipant : leftParticipants) {
            proposalPositions.put(leftParticipant, 0);
        }

        return proposalPositions;
    }

    private PopularMatchingResult<L, R> createResult(
            Map<L, Set<R>> matchesByLeftParticipant
    ) {
        List<Match<L, R>> matches = new ArrayList<>();

        for (Map.Entry<L, Set<R>> entry
                : matchesByLeftParticipant.entrySet()) {

            L leftParticipant = entry.getKey();

            for (R rightParticipant : entry.getValue()) {
                matches.add(
                        new Match<>(
                                leftParticipant,
                                rightParticipant
                        )
                );
            }
        }

        return new PopularMatchingResult<>(matches);
    }
}
