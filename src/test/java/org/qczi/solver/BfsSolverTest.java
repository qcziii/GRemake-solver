package org.qczi.solver;

import org.qczi.model.PuzzleConfig;
import org.qczi.model.RowInfluence;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BfsSolverTest {

    @Test
    void shouldFindShortestPathToMiddlePosition() {
        PuzzleConfig config = new PuzzleConfig(
                2,
                new int[]{7, 7},
                new int[]{1, 2},
                new int[]{3, 3},
                new RowInfluence[]{
                        new RowInfluence(new int[]{-1, 0}),
                        new RowInfluence(new int[]{0, -1})
                },
                10_000
        );

        SolveResult result = new BfsSolver().solve(config);

        assertTrue(result.solved());
        assertEquals(3, result.moves().size());
    }

    @Test
    void shouldReturnSolvedForAlreadyCenteredState() {
        PuzzleConfig config = new PuzzleConfig(
                1,
                new int[]{7},
                new int[]{3},
                new int[]{3},
                new RowInfluence[]{
                        new RowInfluence(new int[]{-1})
                },
                100
        );

        SolveResult result = new BfsSolver().solve(config);

        assertTrue(result.solved());
        assertEquals(0, result.moves().size());
    }

    @Test
    void shouldTreatMoveAsInvalidWhenAnyRowWouldLeaveAllowedRange() {
        PuzzleConfig config = new PuzzleConfig(
                2,
                new int[]{7, 7},
                new int[]{6, 0},
                new int[]{3, 3},
                new RowInfluence[]{
                        new RowInfluence(new int[]{1, 1}),
                        new RowInfluence(new int[]{0, 0})
                },
                1_000
        );

        SolveResult result = new BfsSolver().solve(config);

        assertFalse(result.solved());
        assertEquals("Nie znaleziono sciezki do stanu docelowego.", result.message());
    }

    @Test
    void shouldReturnNoSolutionForProvidedFiveRowExampleUnderSymmetricRules() {
        PuzzleConfig config = new PuzzleConfig(
                5,
                new int[]{7, 7, 7, 7, 7},
                new int[]{6, 0, 1, 1, 4},
                new int[]{3, 3, 3, 3, 3},
                new RowInfluence[]{
                        new RowInfluence(new int[]{-1, 0, 0, 0, 1}),
                        new RowInfluence(new int[]{-1, -1, 1, 0, 1}),
                        new RowInfluence(new int[]{0, 1, -1, 0, 0}),
                        new RowInfluence(new int[]{-1, 1, 0, -1, 0}),
                        new RowInfluence(new int[]{0, 0, 0, 1, -1})
                },
                1_000_000
        );

        SolveResult result = new BfsSolver().solve(config);

        assertFalse(result.solved());
        assertEquals(516, result.visitedStates());
        assertEquals("Nie znaleziono sciezki do stanu docelowego.", result.message());
        assertEquals(List.of(), result.moves());
    }
}

