package thedrake;

import java.io.PrintWriter;

public class Board implements JSONSerializable {
    private final int dimension;
    private BoardTile[][] board;

    // Constructor. Creates square game board of given size (dimension = width = height), with all places empty (containing BoardTile.EMPTY)
    public Board(int dimension) {
        // Place for your code
        this.dimension = dimension;
        board = new BoardTile[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board[i][j] = BoardTile.EMPTY;
                }
            }
        }

    // Size of the board
    public int dimension() {
        // Place for your code
        return dimension;
    }

    // Returns a tile on a provided position
    public BoardTile at(TilePos pos) {
        // Place for your code
        return board[pos.i()][pos.j()];
    }

    // Creates new board with new tiles provided by the ats parameter. All the other tiles stay the same
    public Board withTiles(TileAt... ats) {
        // Place for your code
        Board newBoard = new Board(dimension);

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                newBoard.board[i][j] = this.board[i][j];
            }
        }

        for (int i = 0; i < ats.length; i++) {
            newBoard.board[ats[i].pos.i()][ats[i].pos.j()] = ats[i].tile;
        }

        return newBoard;
    }

    // Creates an instance of PositionFactory class for simpler creation of new position objects for this board
    public PositionFactory positionFactory() {
        // Place for your code
        return new PositionFactory(dimension);
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("{\"dimension\":%d,\"tiles\":[", dimension);
        int counter = 0;
        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension; j++){
                board[j][i].toJSON(writer);
                counter++;
                if (counter < dimension * dimension)
                    writer.print(",");
            }
        }
        writer.print("]}");
    }
}

