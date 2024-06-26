package thedrake;

import java.util.Optional;
import java.io.PrintWriter;
import thedrake.JSONSerializable;

public class GameState implements JSONSerializable {
    private final Army blueArmy;
    private final Army orangeArmy;
    private final GameResult result;
    private final Board board;
    private final PlayingSide sideOnTurn;


    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy) {
        this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
    }

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy,
            PlayingSide sideOnTurn,
            GameResult result) {
        this.board = board;
        this.sideOnTurn = sideOnTurn;
        this.blueArmy = blueArmy;
        this.orangeArmy = orangeArmy;
        this.result = result;
    }

    public Board board() {
        return board;
    }

    public PlayingSide sideOnTurn() {
        return sideOnTurn;
    }

    public GameResult result() {
        return result;
    }

    public Army army(PlayingSide side) {
        if (side == PlayingSide.BLUE) {
            return blueArmy;
        }
        return orangeArmy;
    }

    public Army armyOnTurn() {
        return army(sideOnTurn);
    }

    public Army armyNotOnTurn() {
        if (sideOnTurn == PlayingSide.BLUE)
            return orangeArmy;

        return blueArmy;
    }

    // Vrátí dlaždici, která se nachází na hrací desce na pozici pos.
    // Musí tedy zkontrolovat, jestli na této pozici není jednotka z
    // armády nějakého hráče a pokud ne, vrátí dlaždici z objektu board
    /*public Tile tileAt(TilePos pos) {
        // Place for your code
        if(armyNotOnTurn().boardTroops().at(pos).isPresent()) {
            return armyNotOnTurn().boardTroops().at(pos).get();
        }
        ---
    }*/
    public Tile tileAt(TilePos pos) {
        if (blueArmy.boardTroops().at(pos).isPresent()) {
            return blueArmy.boardTroops().at(pos).get();
        } else if (orangeArmy.boardTroops().at(pos).isPresent()) {
            return orangeArmy.boardTroops().at(pos).get();
        } else {
            return board.at(pos);
        }
    }

    /*
 Vrátí true, pokud je možné ze zadané pozice začít tah nějakou
 jednotkou. Vrací false, pokud stav hry není IN_PLAY, pokud
 na dané pozici nestojí žádná jednotka nebo pokud na pozici
 stojí jednotka hráče, který zrovna není na tahu.
 Při implementaci vemte v úvahu zahájení hry. Dokud nejsou
 postaveny stráže, žádné pohyby jednotek po desce nejsou možné.
*/
    private boolean canStepFrom(TilePos origin) {
        if (!origin.equals(TilePos.OFF_BOARD)
                && result.equals(GameResult.IN_PLAY)
                && armyOnTurn().boardTroops().at(board.positionFactory().pos(origin.i(), origin.j())).isPresent()
                && !blueArmy.boardTroops().isPlacingGuards()
                && !orangeArmy.boardTroops().isPlacingGuards()) {
            return true;
        }
        return false;
    }


    // Vrátí true, pokud je možné na zadanou pozici dokončit tah nějakou
    // jednotkou. Vrací false, pokud stav hry není IN_PLAY nebo pokud
    // na zadanou dlaždici nelze vstoupit (metoda Tile.canStepOn).
    private boolean canStepTo(TilePos target) {
        if (result != GameResult.IN_PLAY || target.equals(TilePos.OFF_BOARD) || !tileAt(target).canStepOn()) {
            //(!result.equals(GameResult.IN_PLAY) || target.equals(TilePos.OFF_BOARD) || !tileAt(target).canStepOn()) {
            return false;
        }
        return true;
    }


    // Vrátí true, pokud je možné na zadané pozici vyhodit soupeřovu jednotku.
    // Vrací false, pokud stav hry není IN_PLAY nebo pokud
    // na zadané pozici nestojí jednotka hráče, který zrovna není na tahu.
    private boolean canCaptureOn(TilePos target) {
        if (!result.equals(GameResult.IN_PLAY) || target.equals(TilePos.OFF_BOARD)|| !armyNotOnTurn().boardTroops().at(target).isPresent()) //|| armyOnTurn().boardTroops().at(target).isPresent())
            return false;
        return true;
    }

    public boolean canStep(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canStepTo(target);
    }

    public boolean canCapture(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canCaptureOn(target);
    }

    /*
 Vrátí true, pokud je možné na zadanou pozici položit jednotku ze
 zásobníku.. Vrací false, pokud stav hry není IN_PLAY, pokud je zásobník
 armády, která je zrovna na tahu prázdný, pokud není možné na danou
 dlaždici vstoupit. Při implementaci vemte v úvahu zahájení hry, kdy
 se vkládání jednotek řídí jinými pravidly než ve střední hře.
*/
    public boolean canPlaceFromStack(TilePos target) {
        if (armyOnTurn().stack().isEmpty() || !canStepTo(target))
            return false;

        if (!armyOnTurn().boardTroops().isLeaderPlaced())
            return (armyOnTurn() == blueArmy)
                    ? target.row() == 1
                    : target.row() == board.dimension();

        if (armyOnTurn().boardTroops().isPlacingGuards())
            return target.isNextTo(armyOnTurn().boardTroops().leaderPosition());

        for (BoardPos pos : armyOnTurn().boardTroops().troopPositions())
            if (target.isNextTo(pos)) {
                return true;
            }
        return false;
    }

    public GameState stepOnly(BoardPos origin, BoardPos target) {
        if (canStep(origin, target))
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);
        throw new IllegalArgumentException();
    }


    public GameState stepAndCapture(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopStep(origin, target).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }



    public GameState captureOnly(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopFlip(origin).capture(captured), newResult);
        }
        throw new IllegalArgumentException();
    }

    public GameState placeFromStack(BoardPos target) {
        if (canPlaceFromStack(target)) {
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().placeFromStack(target),
                    GameResult.IN_PLAY);
        }

        throw new IllegalArgumentException();
    }


    public GameState resign() {
        return createNewGameState(
                armyNotOnTurn(),
                armyOnTurn(),
                GameResult.VICTORY);
    }

    public GameState draw() {
        return createNewGameState(
                armyOnTurn(),
                armyNotOnTurn(),
                GameResult.DRAW);
    }

    private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
        if (armyOnTurn.side() == PlayingSide.BLUE) {
            return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
        }
        return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
    }


    @Override
    public void toJSON(PrintWriter writer) {

        writer.print("{");
        writer.print("\"result\":");
        result.toJSON(writer);
        writer.print(",");
        writer.print("\"board\":");
        board.toJSON(writer);
        writer.print(",");
        writer.print("\"blueArmy\":");
        blueArmy.toJSON(writer);
        writer.print(",");
        writer.print("\"orangeArmy\":");
        orangeArmy.toJSON(writer);
        writer.print("}");

    }
}
