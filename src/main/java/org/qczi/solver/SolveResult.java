package org.qczi.solver;

import org.qczi.model.Move;

import java.util.List;

public record SolveResult(
        boolean solved,
        List<Move> moves,
        int visitedStates,
        String message
) {
}

