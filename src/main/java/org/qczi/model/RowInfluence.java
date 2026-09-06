package org.qczi.model;

import java.util.Arrays;

public record RowInfluence(int[] leftDeltas) {
    public RowInfluence {
        leftDeltas = Arrays.copyOf(leftDeltas, leftDeltas.length);
    }

    public RowInfluence(int[] leftDeltas, int[] rightDeltas) {
        this(validateSymmetry(leftDeltas, rightDeltas));
    }

    @Override
    public int[] leftDeltas() {
        return Arrays.copyOf(leftDeltas, leftDeltas.length);
    }

    public int[] deltasFor(Direction direction) {
        int multiplier = direction == Direction.LEFT ? 1 : -1;
        int[] result = new int[leftDeltas.length];
        for (int i = 0; i < leftDeltas.length; i++) {
            result[i] = leftDeltas[i] * multiplier;
        }
        return result;
    }

    private static int[] validateSymmetry(int[] leftDeltas, int[] rightDeltas) {
        if (leftDeltas.length != rightDeltas.length) {
            throw new IllegalArgumentException("left/right musza miec te sama dlugosc");
        }

        for (int i = 0; i < leftDeltas.length; i++) {
            if (rightDeltas[i] != -leftDeltas[i]) {
                throw new IllegalArgumentException(
                        "Dla symetrycznego influence right musi byc przeciwienstwem left. Blad pod indexem " + i
                );
            }
        }

        return leftDeltas;
    }
}

