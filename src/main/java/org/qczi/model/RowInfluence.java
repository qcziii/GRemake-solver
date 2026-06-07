package org.qczi.model;

import java.util.Arrays;

public record RowInfluence(int[] leftDeltas, int[] rightDeltas) {
    public RowInfluence {
        leftDeltas = Arrays.copyOf(leftDeltas, leftDeltas.length);
        rightDeltas = Arrays.copyOf(rightDeltas, rightDeltas.length);
    }

    @Override
    public int[] leftDeltas() {
        return Arrays.copyOf(leftDeltas, leftDeltas.length);
    }

    @Override
    public int[] rightDeltas() {
        return Arrays.copyOf(rightDeltas, rightDeltas.length);
    }
}

