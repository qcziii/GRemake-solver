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

        int[] startPositions = Objects.requireNonNull(readRequiredIntArray(root, "startPositions"));
        int rowCount = root.path("rowCount").asInt(startPositions.length);

        int[] rowSizes = readOptionalIntArray(root, "rowSizes");
        if (rowSizes == null) {
            rowSizes = PuzzleConfig.defaultRowSizes(rowCount);
        }

        int[] targetPositions = readOptionalIntArray(root, "targetPositions");
        if (targetPositions == null) {
            targetPositions = PuzzleConfig.defaultTargetPositions(rowCount);
        }

        JsonNode influencesNode = root.get("influences");
        if (influencesNode == null || !influencesNode.isArray()) {
            throw new IllegalArgumentException("Pole influences musi byc tablica");
        }

        RowInfluence[] influences = new RowInfluence[influencesNode.size()];
        for (int i = 0; i < influencesNode.size(); i++) {
            JsonNode influenceNode = influencesNode.get(i);
            int[] left = readInfluenceArray(influenceNode);
            int[] right = readOptionalIntArray(influenceNode, "right");
            if (right != null) {
                validateSymmetricRight(left, right, i);
            }
            influences[i] = new RowInfluence(left);
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

    private static int[] readInfluenceArray(JsonNode influenceNode) {
        int[] deltas = readOptionalIntArray(influenceNode, "deltas");
        int[] left = readOptionalIntArray(influenceNode, "left");

        if (deltas != null && left != null && !java.util.Arrays.equals(deltas, left)) {
            throw new IllegalArgumentException("Pola deltas i left nie moga zawierac roznych wartosci");
        }

        if (deltas != null) {
            return deltas;
        }

        if (left != null) {
            return left;
        }

        throw new IllegalArgumentException("Brak wymaganego pola: deltas lub left");
    }

    private static void validateSymmetricRight(int[] left, int[] right, int rowIndex) {
        if (left.length != right.length) {
            throw new IllegalArgumentException("Pola left/right musza miec taka sama dlugosc dla rzedu od dolu " + (rowIndex + 1));
        }

        for (int i = 0; i < left.length; i++) {
            if (right[i] != -left[i]) {
                throw new IllegalArgumentException(
                        "Pole right dla rzedu od dolu " + (rowIndex + 1) + " musi byc dokladnym przeciwienstwem left/deltas"
                );
            }
        }
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

