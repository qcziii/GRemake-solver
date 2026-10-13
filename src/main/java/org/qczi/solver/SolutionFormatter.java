package org.qczi.solver;

import org.qczi.model.Direction;
import org.qczi.model.Move;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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

    public SolveResult formatDirection(SolveResult result) {
        return new SolveResult(result.solved(), makeAllMovesOppositeDirectionAndChangeIndex(result.moves()), result.visitedStates(), result.message());
    }

    private List<Move> makeAllMovesOppositeDirectionAndChangeIndex(List<Move> moves) {
        return moves.stream().map(s -> new Move(s.rowIndex() + 1, getOppositeDirection(s.direction()))).toList();
    }

    private Direction getOppositeDirection(Direction direction) {
        if (direction == Direction.RIGHT) return Direction.LEFT;
        return Direction.RIGHT;
    }
}

