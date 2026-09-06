package org.qczi.solver;

import org.qczi.model.Direction;
import org.qczi.model.Move;

import java.util.List;

public final class SolutionFormatter {
    private SolutionFormatter() {
    }

    public static String format(SolveResult result) {
        StringBuilder sb = new StringBuilder();

        if (!result.solved()) {
            sb.append("Brak rozwiazania. ").append(result.message());
            sb.append(System.lineSeparator()).append("Odwiedzone stany: ").append(result.visitedStates());
            return sb.toString();
        }

        List<Move> moves = result.moves();
        sb.append("Znaleziono rozwiazanie.");
        sb.append(System.lineSeparator()).append("Liczba ruchow: ").append(moves.size());
        sb.append(System.lineSeparator()).append("Odwiedzone stany: ").append(result.visitedStates());
        sb.append(System.lineSeparator()).append("Sekwencja:");

        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            sb.append(System.lineSeparator())
                    .append(i + 1)
                    .append(". Rzad od dolu ")
                    .append(move.rowIndex() + 1)
                    .append(" -> ")
                    .append(move.direction() == Direction.LEFT ? "PRAWO" : "LEWO");
        }

        return sb.toString();
    }
}

