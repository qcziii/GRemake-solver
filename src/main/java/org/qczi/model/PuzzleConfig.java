package org.qczi.model;

import java.util.Arrays;

public final class PuzzleConfig {
    private final int rowCount;
    private final int[] rowSizes;
    private final int[] startPositions;
    private final int[] targetPositions;
    private final RowInfluence[] influences;
    private final int maxVisitedStates;

    public PuzzleConfig(
            int rowCount,
            int[] rowSizes,
            int[] startPositions,
            int[] targetPositions,
            RowInfluence[] influences,
            int maxVisitedStates
    ) {
        this.rowCount = rowCount;
        this.rowSizes = Arrays.copyOf(rowSizes, rowSizes.length);
        this.startPositions = Arrays.copyOf(startPositions, startPositions.length);
        this.targetPositions = Arrays.copyOf(targetPositions, targetPositions.length);
        this.influences = Arrays.copyOf(influences, influences.length);
        this.maxVisitedStates = maxVisitedStates;
    }

    public int rowCount() {
        return rowCount;
    }

    public int[] rowSizes() {
        return Arrays.copyOf(rowSizes, rowSizes.length);
    }

    public int[] startPositions() {
        return Arrays.copyOf(startPositions, startPositions.length);
    }

    public int[] targetPositions() {
        return Arrays.copyOf(targetPositions, targetPositions.length);
    }

    public RowInfluence[] influences() {
        return Arrays.copyOf(influences, influences.length);
    }

    public int maxVisitedStates() {
        return maxVisitedStates;
    }
}

