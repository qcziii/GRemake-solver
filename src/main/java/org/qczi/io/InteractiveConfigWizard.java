package org.qczi.io;

import org.qczi.model.ConfigValidator;
import org.qczi.model.PuzzleConfig;
import org.qczi.model.RowInfluence;

import java.util.Scanner;
import java.util.function.IntPredicate;

public final class InteractiveConfigWizard {
    private static final int DEFAULT_MAX_VISITED = 1_000_000;

    private InteractiveConfigWizard() {
    }

    public static PuzzleConfig readFromConsole(Scanner scanner) {
        System.out.println("=== Kreator konfiguracji puzzla ===");
        System.out.println("Rzedy podawaj od dolu do gory.");
        System.out.println("Kazdy rzad ma " + PuzzleConfig.FIXED_ROW_SIZE + " pozycji, a celem jest srodek (index " + PuzzleConfig.TARGET_POSITION + ").");
        System.out.println("LEWO zwieksza indeks pozycji (np. 3 -> 4), a PRAWO go zmniejsza (np. 3 -> 2).");
        System.out.println("Sposob wpisywania delt zostaje bez zmian: podajesz bazowy ruch techniczny 3 -> 2, a przeciwny kierunek wyliczy sie automatycznie.");

        int rowCount = readInt(
                scanner,
                "Liczba rzedow: ",
                value -> value > 0,
                "Podaj liczbe calkowita > 0."
        );

        int[] rowSizes = PuzzleConfig.defaultRowSizes(rowCount);

        int[] startPositions = new int[rowCount];
        for (int i = 0; i < rowCount; i++) {
            startPositions[i] = readInt(
                    scanner,
                    "Startowa pozycja rzedu od dolu " + displayRowNumber(i) + " (0-" + (PuzzleConfig.FIXED_ROW_SIZE - 1) + "): ",
                    value -> value >= 0 && value < PuzzleConfig.FIXED_ROW_SIZE,
                    "Pozycja musi byc w zakresie 0-" + (PuzzleConfig.FIXED_ROW_SIZE - 1) + "."
            );
        }

        int[] targetPositions = PuzzleConfig.defaultTargetPositions(rowCount);

        int maxVisited = readInt(
                scanner,
                "maxVisitedStates (ENTER = " + DEFAULT_MAX_VISITED + "): ",
                value -> value > 0,
                "Podaj liczbe calkowita > 0.",
                true,
                DEFAULT_MAX_VISITED
        );

        RowInfluence[] influences = new RowInfluence[rowCount];
        for (int movedRow = 0; movedRow < rowCount; movedRow++) {
            System.out.println(
                    "Rzad od dolu " + displayRowNumber(movedRow)
                            + " - wpisz delty dla bazowego ruchu technicznego 3 -> 2 dla wszystkich rzedow od dolu do gory (tylko -1, 0, 1)."
            );
            int[] left = readInfluenceArray(
                    scanner,
                    "  DELTAS (" + rowCount + " liczb, przeciwny kierunek wyliczy sie automatycznie): ",
                    rowCount
            );
            influences[movedRow] = new RowInfluence(left);
        }

        PuzzleConfig config = new PuzzleConfig(
                rowCount,
                rowSizes,
                startPositions,
                targetPositions,
                influences,
                maxVisited
        );

        ConfigValidator.validate(config);
        return config;
    }

    private static int readInt(
            Scanner scanner,
            String prompt,
            IntPredicate validator,
            String validationError
    ) {
        return readInt(scanner, prompt, validator, validationError, false, 0);
    }

    private static int readInt(
            Scanner scanner,
            String prompt,
            IntPredicate validator,
            String validationError,
            boolean allowEmpty,
            int defaultValue
    ) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();

            if (allowEmpty && line.isEmpty()) {
                return defaultValue;
            }

            int value;
            try {
                value = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("To nie jest poprawna liczba calkowita.");
                continue;
            }

            if (!validator.test(value)) {
                System.out.println(validationError);
                continue;
            }

            return value;
        }
    }

    private static int[] readIntArray(Scanner scanner, String prompt, int expectedLength) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("Podaj " + expectedLength + " liczb.");
                continue;
            }

            String[] parts = line.split("[,\\s]+");
            if (parts.length != expectedLength) {
                System.out.println("Podaj dokladnie " + expectedLength + " liczb.");
                continue;
            }

            int[] values = new int[expectedLength];
            boolean ok = true;
            for (int i = 0; i < expectedLength; i++) {
                try {
                    values[i] = Integer.parseInt(parts[i]);
                } catch (NumberFormatException e) {
                    ok = false;
                    break;
                }
            }

            if (!ok) {
                System.out.println("Wszystkie wartosci musza byc liczbami calkowitymi.");
                continue;
            }

            return values;
        }
    }

    private static int[] readInfluenceArray(Scanner scanner, String prompt, int expectedLength) {
        while (true) {
            int[] values = readIntArray(scanner, prompt, expectedLength);
            boolean valid = true;
            for (int value : values) {
                if (Math.abs(value) > 1) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                return values;
            }

            System.out.println("Kazda delta musi byc rowna -1, 0 lub 1.");
        }
    }

    private static int displayRowNumber(int rowIndex) {
        return rowIndex + 1;
    }
}


