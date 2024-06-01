package thedrake;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.io.PrintWriter;
import java.util.TreeSet;

public class BoardTroops implements JSONSerializable {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        // Place for your code
        //could be also this.
        this.playingSide = playingSide;
        this.troopMap = Collections.EMPTY_MAP;

        this.leaderPosition = TilePos.OFF_BOARD;
        this.guards = 0;
    }

    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards) {
        // Place for your code
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    public Optional<TroopTile> at(TilePos pos) {
        // Place for your code
        if (troopMap.containsKey(pos)) return Optional.of(troopMap.get(pos));
        return Optional.empty();
    }
    /*
        if (pos == TilePos.OFF_BOARD || !troopMap.containsKey(pos))
            return Optional.empty();
        return Optional.of(troopMap.get(pos));
    }*/

    public PlayingSide playingSide() {
        // Place for your code
        return playingSide;
    }

    public TilePos leaderPosition() {
        // Place for your code
        return leaderPosition;
    }

    public int guards() {
        // Place for your code
        return guards;
    }

    public boolean isLeaderPlaced() {
        // Place for your code
        return leaderPosition != TilePos.OFF_BOARD;
    }

    public boolean isPlacingGuards() {
        // Place for your code
        return isLeaderPlaced() && guards < 2;
    }

    public Set<BoardPos> troopPositions() {
        // Place for your code
        return troopMap.keySet();
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        // Place for your code

        if (at(target).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        newTroops.put(target, new TroopTile(troop, playingSide(), TroopFace.AVERS));

        return new BoardTroops(
                playingSide,
                newTroops,
                isLeaderPlaced() ? leaderPosition : target,
                isPlacingGuards() ? guards + 1 : guards);
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        // Place for your code
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent() || at(target).isPresent()) throw new IllegalArgumentException();

        TilePos newLeaderPosition = leaderPosition;
        if (leaderPosition.equals(origin)) newLeaderPosition = target;

        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        TroopTile tile = newTroopMap.remove(origin);
        newTroopMap.put(target, tile.flipped());

        return new BoardTroops(playingSide(), newTroopMap, newLeaderPosition, guards);
    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops removeTroop(BoardPos target) {
        // Place for your code
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (at(target).isEmpty())
            throw new IllegalArgumentException();


        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        newTroops.remove(target);

        return new BoardTroops( playingSide,
                newTroops,
                target.equals(leaderPosition) ? TilePos.OFF_BOARD : leaderPosition,
                guards);
        //playingSide, newTroopMap, newLeaderPosition, newGuards);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{\"side\":");
        playingSide.toJSON(writer);
        writer.print(",\"leaderPosition\":");
        leaderPosition.toJSON(writer);
        writer.printf(",\"guards\":%d,\"troopMap\":{", guards);
        int counter = 0;
        for (BoardPos x:new TreeSet<>(troopMap.keySet())) {
            x.toJSON(writer);
            writer.print(":");
            troopMap.get(x).toJSON(writer);
            counter++;
            if (counter < troopMap.size())
                writer.print(",");
        }
        writer.print("}}");
    }

}