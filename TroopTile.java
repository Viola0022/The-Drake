package thedrake;

import java.util.List;
import java.util.ArrayList;

public class TroopTile implements Tile {

    private final Troop troop;
    private final PlayingSide side;
    private final TroopFace face;

    // Konstruktor
    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    // Vrací barvu, za kterou hraje jednotka na této dlaždici
    public PlayingSide side() {
        return side;
    }

    // Vrací stranu, na kterou je jednotka otočena
    public TroopFace face() {
        return face;
    }

    // Jednotka, která stojí na této dlaždici
    public Troop troop() {
        return troop;
    }

    // Vrací False, protože na dlaždici s jednotkou se nedá vstoupit
    @Override
    public boolean canStepOn() {
        return false;
    }

    // Vrací True
    @Override
    public boolean hasTroop() {
        return true;
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {
        // Initialize an empty list to store the possible moves
        List<Move> possibleMoves = new ArrayList<>();

        // Get the troop's actions based on its current face
        List<TroopAction> actions = troop.actions(face);

        // Loop through each action and generate the corresponding moves
       /*for (TroopAction action : actions) {
            // Get the target positions for the action
            List<Move> targetPositions = action.movesFrom(pos, side(), state);

          // Create moves for each target position
            for (Move targetPos : targetPositions) {
                possibleMoves.add(new StepOnly(pos, targetPos.target()));
            }
        }*/
        for (TroopAction action : actions) {
            possibleMoves.addAll(action.movesFrom(pos, side, state));
        }

        return possibleMoves;
    }


    // Vytvoří novou dlaždici, s jednotkou otočenou na opačnou stranu
    // (z rubu na líc nebo z líce na rub)
    public TroopTile flipped() {
        return face() == TroopFace.AVERS ? new TroopTile(troop, side, TroopFace.REVERS) : new TroopTile(troop, side, TroopFace.AVERS);
    }
}
