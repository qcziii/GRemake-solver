package org.qczi.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.qczi.model.ConfigValidator;
import org.qczi.model.PuzzleConfig;
import org.qczi.model.RowInfluence;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public final class InputParser {
    private static final int DEFAULT_MAX_VISITED = 1_000_000;

    private InputParser() {
    }

    public static PuzzleConfig parse(Path path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(path.toFile());

        int[] rowSizes = Objects.requireNonNull(readRequiredIntArray(root, "rowSizes"));
        int rowCount = root.path("rowCount").asInt(rowSizes.length);

        int[] startPositions = Objects.requireNonNull(readRequiredIntArray(root, "startPositions"));
        int[] targetPositions = readOptionalIntArray(root, "targetPositions");
        if (targetPositions == null) {
            targetPositions = new int[rowCount];
        }

        JsonNode influencesNode = root.get("influences");
        if (influencesNode == null || !influencesNode.isArray()) {
            throw new IllegalArgumentException("Pole influences musi byc tablica");
        }

        RowInfluence[] influences = new RowInfluence[influencesNode.size()];
        for (int i = 0; i < influencesNode.size(); i++) {
            JsonNode influenceNode = influencesNode.get(i);
            int[] left = Objects.requireNonNull(readRequiredIntArray(influenceNode, "left"));
            int[] right = Objects.requireNonNull(readRequiredIntArray(influenceNode, "right"));
            influences[i] = new RowInfluence(left, right);
        }

        int maxVisited = root.path("maxVisitedStates").asInt(DEFAULT_MAX_VISITED);

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

    private static int[] readRequiredIntArray(JsonNode parent, String fieldName) {
        int[] data = readOptionalIntArray(parent, fieldName);
        if (data == null) {
            throw new IllegalArgumentException("Brak wymaganego pola: " + fieldName);
        }
        return data;
    }

    private static int[] readOptionalIntArray(JsonNode parent, String fieldName) {
        JsonNode node = parent.get(fieldName);
        if (node == null || node.isNull()) {
            return null;
        }

        if (!node.isArray()) {
            throw new IllegalArgumentException("Pole " + fieldName + " musi byc tablica");
        }

        int[] result = new int[node.size()];
        for (int i = 0; i < node.size(); i++) {
            JsonNode item = node.get(i);
            if (!item.canConvertToInt()) {
                throw new IllegalArgumentException("Pole " + fieldName + " zawiera nie-liczbe pod indexem " + i);
            }
            result[i] = item.asInt();
        }
        return result;
    }
}

