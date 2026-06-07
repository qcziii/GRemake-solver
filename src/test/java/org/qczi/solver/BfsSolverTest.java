package org.qczi.solver;

import org.qczi.model.PuzzleConfig;
import org.qczi.model.RowInfluence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BfsSolverTest {

    @Test
    void shouldFindShortestPathForSimpleIndependentRows() {
        PuzzleConfig config = new PuzzleConfig(
                2,
                new int[]{3, 3},
                new int[]{1, 2},
                new int[]{0, 0},
                new RowInfluence[]{
                        new RowInfluence(new int[]{-1, 0}, new int[]{1, 0}),
                        new RowInfluence(new int[]{0, -1}, new int[]{0, 1})
                },
                10_000
        );

        SolveResult result = new BfsSolver().solve(config);

        assertTrue(result.solved());
        assertEquals(2, result.moves().size());
    }

    @Test
    void shouldReturnSolvedForAlreadyCenteredState() {
        PuzzleConfig config = new PuzzleConfig(
                1,
                new int[]{5},
                new int[]{0},
                new int[]{0},
                new RowInfluence[]{
                        new RowInfluence(new int[]{-1}, new int[]{1})
                },
                100
        );

        SolveResult result = new BfsSolver().solve(config);

        assertTrue(result.solved());
        assertEquals(0, result.moves().size());
    }
}

