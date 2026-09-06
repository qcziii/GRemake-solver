package org.qczi.solver;

import org.qczi.model.Direction;
import org.qczi.model.Move;
import org.qczi.model.PuzzleConfig;
import org.qczi.model.RowInfluence;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public final class BfsSolver {
    public SolveResult solve(PuzzleConfig config) {
        int[] start = config.startPositions();
        int[] target = config.targetPositions();

        String startKey = encode(start);
        String targetKey = encode(target);

        if (startKey.equals(targetKey)) {
            return new SolveResult(true, List.of(), 1, "Stan startowy juz jest rozwiazaniem.");
        }

        Queue<int[]> queue = new ArrayDeque<>();
        Map<String, String> parents = new HashMap<>();
        Map<String, Move> usedMoves = new HashMap<>();

        queue.add(start);
        parents.put(startKey, null);

        int visited = 0;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            String currentKey = encode(current);
            visited++;

            for (int row = 0; row < config.rowCount(); row++) {
                for (Direction direction : Direction.values()) {
                    int[] next = applyMove(config, current, row, direction);
                    if (next == null) {
                        continue;
                    }

                    String nextKey = encode(next);
                    if (parents.containsKey(nextKey)) {
                        continue;
                    }

                    parents.put(nextKey, currentKey);
                    usedMoves.put(nextKey, new Move(row, direction));

                    if (nextKey.equals(targetKey)) {
                        List<Move> path = buildPath(startKey, targetKey, parents, usedMoves);
                        return new SolveResult(true, path, visited, "OK");
                    }

                    if (parents.size() > config.maxVisitedStates()) {
                        return new SolveResult(
                                false,
                                List.of(),
                                visited,
                                "Przekroczono limit maxVisitedStates=" + config.maxVisitedStates()
                        );
                    }

                    queue.add(next);
                }
            }
        }

        return new SolveResult(false, List.of(), visited, "Nie znaleziono sciezki do stanu docelowego.");
    }

    private static List<Move> buildPath(
            String startKey,
            String targetKey,
            Map<String, String> parents,
            Map<String, Move> usedMoves
    ) {
        List<Move> path = new ArrayList<>();
        String cursor = targetKey;

        while (!cursor.equals(startKey)) {
            Move move = usedMoves.get(cursor);
            path.add(move);
            cursor = parents.get(cursor);
        }

        java.util.Collections.reverse(path);
        return path;
    }

    private static int[] applyMove(PuzzleConfig config, int[] current, int movedRow, Direction direction) {
        int[] next = Arrays.copyOf(current, current.length);
        RowInfluence influence = config.influences()[movedRow];
        int[] deltas = influence.deltasFor(direction);
        int[] rowSizes = config.rowSizes();

        for (int i = 0; i < next.length; i++) {
            int candidate = next[i] + deltas[i];
            if (candidate < 0 || candidate >= rowSizes[i]) {
                return null;
            }
            next[i] = candidate;
        }

        return next;
    }


    private static String encode(int[] state) {
        StringBuilder sb = new StringBuilder(state.length * 3);
        for (int i = 0; i < state.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(state[i]);
        }
        return sb.toString();
    }
}

