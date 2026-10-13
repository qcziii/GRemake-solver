package org.qczi.request;

import java.util.List;

public class AppRequest {

    int rowCount;
    List<Integer> startPositions;
    int maxVisitedStates;
    List<List<Integer>> influences;

    public AppRequest(int rowCount, List<Integer> startPositions, List<List<Integer>> influences, int maxVisitedStates) {
        this.rowCount = rowCount;
        this.startPositions = startPositions;
        this.influences = influences;
        this.maxVisitedStates = maxVisitedStates;
    }

    public int getRowCount() {
        return rowCount;
    }

    public List<Integer> getStartPositions() {
        return startPositions;
    }

    public int getMaxVisitedStates() {
        return maxVisitedStates;
    }

    public List<List<Integer>> getInfluences() {
        return influences;
    }
}
