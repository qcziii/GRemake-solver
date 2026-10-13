package org.qczi.io;

import org.qczi.model.ConfigValidator;
import org.qczi.model.PuzzleConfig;
import org.qczi.model.RowInfluence;
import org.qczi.request.AppRequest;
import org.springframework.stereotype.Component;

@Component
public class JsonConfigWizard {

    public PuzzleConfig readFromJson(AppRequest appRequest) {

        int rowCount = appRequest.getRowCount();
        int[] rowSizes = PuzzleConfig.defaultRowSizes(rowCount);

        int[] startPositions = appRequest.getStartPositions().stream()
                .mapToInt(s -> s)
                .toArray();

        int[] targetPositions = PuzzleConfig.defaultTargetPositions(rowCount);

        int maxVisited = appRequest.getMaxVisitedStates();

        RowInfluence[] influences = new RowInfluence[rowCount];
        for (int i = 0; i < influences.length; i++) {
            influences[i] = new RowInfluence(appRequest.getInfluences().get(i).stream().mapToInt(s -> s).toArray());
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

}
