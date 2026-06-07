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

        int rowCount = readInt(
                scanner,
                "Liczba rzedow: ",
                value -> value > 0,
                "Podaj liczbe calkowita > 0."
        );

        int[] rowSizes = new int[rowCount];
        for (int i = 0; i < rowCount; i++) {
            final int row = i;
            rowSizes[i] = readInt(
                    scanner,
                    "Liczba pozycji (modulo) dla rzedu " + i + ": ",
                    value -> value > 1,
                    "Rzad " + row + " musi miec co najmniej 2 pozycje."
            );
        }

        int[] startPositions = new int[rowCount];
        for (int i = 0; i < rowCount; i++) {
            int rowSize = rowSizes[i];
            int max = rowSizes[i] - 1;
            startPositions[i] = readInt(
                    scanner,
                    "Startowa pozycja rzedu " + i + " (0-" + max + "): ",
                    value -> value >= 0 && value < rowSize,
                    "Pozycja musi byc w zakresie 0-" + max + "."
            );
        }

        int[] targetPositions = new int[rowCount];
        boolean customTarget = readYesNo(scanner, "Czy chcesz podac pozycje docelowe (t/n)? ");
        if (customTarget) {
            for (int i = 0; i < rowCount; i++) {
                int rowSize = rowSizes[i];
                int max = rowSizes[i] - 1;
                targetPositions[i] = readInt(
                        scanner,
                        "Docelowa pozycja rzedu " + i + " (0-" + max + "): ",
                        value -> value >= 0 && value < rowSize,
                        "Pozycja musi byc w zakresie 0-" + max + "."
                );
            }
        }

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
            System.out.println("Rzad " + movedRow + " - wpisz delty dla wszystkich rzedow oddzielone spacjami.");
            int[] left = readIntArray(
                    scanner,
                    "  LEFT  (" + rowCount + " liczb): ",
                    rowCount
            );
            int[] right = readIntArray(
                    scanner,
                    "  RIGHT (" + rowCount + " liczb): ",
                    rowCount
            );
            influences[movedRow] = new RowInfluence(left, right);
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

    private static boolean readYesNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim().toLowerCase();
            if (line.equals("t") || line.equals("tak") || line.equals("y") || line.equals("yes")) {
                return true;
            }
            if (line.equals("n") || line.equals("nie") || line.equals("no")) {
                return false;
            }
            System.out.println("Wpisz 't' lub 'n'.");
        }
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
}


