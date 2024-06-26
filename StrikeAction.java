package thedrake;

import java.util.ArrayList;
import java.util.List;

public class StrikeAction extends TroopAction {


    public StrikeAction(Offset2D offset) {
        super(offset);
    }
    public StrikeAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }
    //captureOnly generator
    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> result = new ArrayList<>();
        TilePos target = origin.stepByPlayingSide(offset(), side);

        if (state.canCapture(origin, target))
            result.add(new CaptureOnly(origin, (BoardPos) target));
        return result;
    }
}
    /*
    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> result = new ArrayList<>();
        TilePos target = origin.stepByPlayingSide(offset(), side);

    OR NOT??
        if(state.canStep(origin, target)) {
            result.add(new StepOnly(origin, (BoardPos)target));
        } else if(state.canCapture(origin, target)) {
            result.add(new StepAndCapture(origin, (BoardPos)target));
        }
  ???
*/