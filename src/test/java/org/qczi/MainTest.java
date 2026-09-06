package org.qczi;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    @Test
    void shouldPrintExpectedSolutionForProvidedInteractiveExample() {
        String input = String.join("\n",
                "5",
                "6",
                "0",
                "1",
                "1",
                "4",
                "",
                "-1 0 0 0 1",
                "-1 -1 1 0 1",
                "0 1 -1 0 0",
                "-1 1 0 -1 0",
                "0 0 0 1 -1"
        ) + "\n";

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        try (
                PrintStream capturedOut = new PrintStream(stdout, true, StandardCharsets.UTF_8);
                PrintStream capturedErr = new PrintStream(stderr, true, StandardCharsets.UTF_8)
        ) {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(capturedOut);
            System.setErr(capturedErr);

            Main.main(new String[0]);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        String output = stdout.toString(StandardCharsets.UTF_8);
        String errorOutput = stderr.toString(StandardCharsets.UTF_8);

        assertEquals("", errorOutput);
        assertTrue(output.contains("=== Kreator konfiguracji puzzla ==="));
        assertTrue(output.contains("Rzedy podawaj od dolu do gory."));
        assertTrue(output.contains("Kazdy rzad ma 7 pozycji, a celem jest srodek (index 3)."));
        assertTrue(output.contains("Brak rozwiazania. Nie znaleziono sciezki do stanu docelowego."));
        assertTrue(output.contains("Odwiedzone stany: 516"));
    }
}


