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
                "3",      // row 0 size
                "4",      // row 1 size
                "1",      // row 0 start
                "3",      // row 1 start
                "n",      // target positions -> default 0
                "",       // maxVisitedStates -> default
                "-1 0",   // row 0 left
                "1 0",    // row 0 right
                "0 -1",   // row 1 left
                "0 1"     // row 1 right
        ) + "\n";

        PuzzleConfig config = InteractiveConfigWizard.readFromConsole(new Scanner(input));

        assertEquals(2, config.rowCount());
        assertArrayEquals(new int[]{3, 4}, config.rowSizes());
        assertArrayEquals(new int[]{1, 3}, config.startPositions());
        assertArrayEquals(new int[]{0, 0}, config.targetPositions());
        assertEquals(1_000_000, config.maxVisitedStates());
    }
}

