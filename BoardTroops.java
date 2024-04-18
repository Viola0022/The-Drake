package thedrake;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BoardTroops {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        // Place for your code
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
        if (isLeaderPlaced() && guards < 2) return true;
        else return false;
    }

    public Set<BoardPos> troopPositions() {
        // Place for your code
        return troopMap.keySet();
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        // Place for your code
        if(at(target).isPresent()) throw new IllegalArgumentException();

        TilePos newleaderPosition = leaderPosition;
        int newGuards = guards;

        if(!isLeaderPlaced()) newleaderPosition = target;
        if(isPlacingGuards()) newGuards = newGuards + 1;

        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        TroopTile newTroopTile = new TroopTile(troop, playingSide, TroopFace.AVERS);
        newTroopMap.put(target, newTroopTile);
        return new BoardTroops(playingSide, newTroopMap, newleaderPosition, newGuards);
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        // Place for your code
        if(!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if(isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if(!at(origin).isPresent() || at(target).isPresent()) throw new IllegalArgumentException();

        TilePos newLeaderPosition = leaderPosition;
        if(leaderPosition.equals(origin)) newLeaderPosition = target;

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
        if(!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if(isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if(!at(target).isPresent())
            throw new IllegalArgumentException();

        TilePos newLeaderPosition = leaderPosition;
        int newGuards = guards;
        if(leaderPosition.equals(target)) newLeaderPosition = TilePos.OFF_BOARD;
        else newGuards = newGuards - 1;

        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        newTroopMap.remove(target);

        return new BoardTroops(playingSide, newTroopMap, newLeaderPosition, newGuards);
    }
}
