package org.qczi.io;

import org.qczi.model.PuzzleConfig;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InputParserTest {

    @Test
    void shouldUseFixedRowSizeAndMiddleTargetWhenMissingInJson() throws Exception {
        Path file = Files.createTempFile("gremake-config", ".json");
        Files.writeString(file, """
                {
                  "rowCount": 2,
                  "startPositions": [1, 5],
                  "influences": [
                    { "deltas": [-1, 0] },
                    { "deltas": [0, -1] }
                  ]
                }
                """);

        PuzzleConfig config = InputParser.parse(file);

        assertEquals(2, config.rowCount());
        assertArrayEquals(new int[]{7, 7}, config.rowSizes());
        assertArrayEquals(new int[]{3, 3}, config.targetPositions());
    }

    @Test
    void shouldRejectInfluenceGreaterThanOne() throws Exception {
        Path file = Files.createTempFile("gremake-config-invalid", ".json");
        Files.writeString(file, """
                {
                  "rowCount": 1,
                  "startPositions": [1],
                  "influences": [
                    { "deltas": [2] }
                  ]
                }
                """);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> InputParser.parse(file));
        assertEquals(
                "Wplyw dla ruchu w lewo dla rzedu od dolu 1 moze zmienic rzad od dolu 1 maksymalnie o 1. Podano: 2",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectLegacyRightWhenItIsNotOppositeToLeft() throws Exception {
        Path file = Files.createTempFile("gremake-config-invalid-symmetry", ".json");
        Files.writeString(file, """
                {
                  "rowCount": 1,
                  "startPositions": [1],
                  "influences": [
                    { "left": [-1], "right": [-1] }
                  ]
                }
                """);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> InputParser.parse(file));
        assertEquals(
                "Pole right dla rzedu od dolu 1 musi byc dokladnym przeciwienstwem left/deltas",
                exception.getMessage()
        );
    }
}

