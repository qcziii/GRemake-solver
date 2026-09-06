package org.qczi.io;

import org.qczi.model.PuzzleConfig;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InteractiveConfigWizardTest {

    @Test
    void shouldBuildConfigFromConsoleInput() {
        String input = String.join("\n",
                "2",      // rowCount
                "1",      // row 0 start
                "3",      // row 1 start
                "",       // maxVisitedStates -> default
                "-1 0",   // row 0 left / deltas
                "0 -1"    // row 1 left / deltas
        ) + "\n";

        PuzzleConfig config = InteractiveConfigWizard.readFromConsole(new Scanner(input));

        assertEquals(2, config.rowCount());
        assertArrayEquals(new int[]{7, 7}, config.rowSizes());
        assertArrayEquals(new int[]{1, 3}, config.startPositions());
        assertArrayEquals(new int[]{3, 3}, config.targetPositions());
        assertEquals(1_000_000, config.maxVisitedStates());
    }
}

