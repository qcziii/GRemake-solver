package org.qczi.model;

public final class ConfigValidator {
    private ConfigValidator() {
    }

    public static void validate(PuzzleConfig config) {
        int n = config.rowCount();
        if (n <= 0) {
            throw new IllegalArgumentException("rowCount musi byc > 0");
        }

        int[] rowSizes = config.rowSizes();
        int[] start = config.startPositions();
        int[] target = config.targetPositions();
        RowInfluence[] influences = config.influences();

        if (rowSizes.length != n || start.length != n || target.length != n || influences.length != n) {
            throw new IllegalArgumentException("Dlugosci tablic musza byc rowCount");
        }

        if (config.maxVisitedStates() <= 0) {
            throw new IllegalArgumentException("maxVisitedStates musi byc > 0");
        }

        for (int i = 0; i < n; i++) {
            if (rowSizes[i] != PuzzleConfig.FIXED_ROW_SIZE) {
                throw new IllegalArgumentException(
                        "Kazdy rzad musi miec dokladnie " + PuzzleConfig.FIXED_ROW_SIZE + " pozycji. Blad dla rzedu od dolu " + (i + 1)
                );
            }

            if (start[i] < 0 || start[i] >= rowSizes[i]) {
                throw new IllegalArgumentException("Pozycja startowa poza zakresem dla rzedu od dolu " + (i + 1));
            }

            if (target[i] < 0 || target[i] >= rowSizes[i]) {
                throw new IllegalArgumentException("Pozycja docelowa poza zakresem dla rzedu od dolu " + (i + 1));
            }

            if (target[i] != PuzzleConfig.TARGET_POSITION) {
                throw new IllegalArgumentException(
                        "Kazdy rzad musi miec pozycje docelowa ustawiona na srodek (index "
                                + PuzzleConfig.TARGET_POSITION
                                + "). Blad dla rzedu od dolu "
                                + (i + 1)
                );
            }

            RowInfluence influence = influences[i];
            if (influence == null) {
                throw new IllegalArgumentException("Brak influence dla rzedu od dolu " + (i + 1));
            }

            if (influence.leftDeltas().length != n) {
                throw new IllegalArgumentException("leftDeltas musza miec dlugosc rowCount dla rzedu od dolu " + (i + 1));
            }

            validateInfluenceValues(influence.leftDeltas(), i);
        }
    }

    private static void validateInfluenceValues(int[] deltas, int rowIndex) {
        for (int i = 0; i < deltas.length; i++) {
            if (Math.abs(deltas[i]) > 1) {
                throw new IllegalArgumentException(
                        "Wplyw dla ruchu w lewo dla rzedu od dolu "
                                + (rowIndex + 1)
                                + " moze zmienic rzad od dolu "
                                + (i + 1)
                                + " maksymalnie o 1. Podano: "
                                + deltas[i]
                );
            }
        }
    }
}

