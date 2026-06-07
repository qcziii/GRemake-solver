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
            if (rowSizes[i] <= 1) {
                throw new IllegalArgumentException("Kazdy rzad musi miec co najmniej 2 pozycje. Blad dla rzedu " + i);
            }

            if (start[i] < 0 || start[i] >= rowSizes[i]) {
                throw new IllegalArgumentException("Pozycja startowa poza zakresem dla rzedu " + i);
            }

            if (target[i] < 0 || target[i] >= rowSizes[i]) {
                throw new IllegalArgumentException("Pozycja docelowa poza zakresem dla rzedu " + i);
            }

            RowInfluence influence = influences[i];
            if (influence == null) {
                throw new IllegalArgumentException("Brak influence dla rzedu " + i);
            }

            if (influence.leftDeltas().length != n || influence.rightDeltas().length != n) {
                throw new IllegalArgumentException("leftDeltas/rightDeltas musza miec dlugosc rowCount dla rzedu " + i);
            }
        }
    }
}

