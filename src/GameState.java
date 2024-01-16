import java.util.ArrayList;

public class GameState {
    private ArrayList<ConcretePiece[][]> boardPieces;

    private ArrayList<PieceStats[][]> pieceStats;

    private ArrayList<PositionStats[][]> positionStats;

    private int numberOfStates;

    public GameState(){
        boardPieces = new ArrayList<>();
        pieceStats = new ArrayList<>();
        positionStats = new ArrayList<>();
        numberOfStates = 0;
    }

    public void addState(ConcretePiece[][] boardPieces1, PieceStats[][] pieceStats1, PositionStats[][] positionStats1) {
        boardPieces.add(deepCopy(boardPieces1));
        pieceStats.add(deepCopy(pieceStats1));
        positionStats.add(deepCopy(positionStats1));
        numberOfStates++;
    }

    public void removeState() {
        boardPieces.remove(boardPieces.size()-1);
        pieceStats.remove(pieceStats.size()-1);
        positionStats.remove(positionStats.size()-1);
        numberOfStates--;
    }

    public ConcretePiece[][] getLast_boardPieces() {
        return deepCopy(boardPieces.get(boardPieces.size()-1));
    }

    public PieceStats[][] getLast_pieceStats() {
        return deepCopy(pieceStats.get(pieceStats.size()-1));
    }

    public PositionStats[][] getLast_positionStats() {
        return deepCopy(positionStats.get(positionStats.size()-1));
    }

    public int getNumberOfStates() {
        return numberOfStates;
    }

    public static ConcretePiece[][] deepCopy(ConcretePiece[][] concretePiece) {
        ConcretePiece[][] clone = new ConcretePiece[concretePiece.length][];

        for (int i = 0; i < clone.length; i++) {
            clone[i] = concretePiece[i].clone();
        }

        return clone;
    }

    public static PieceStats[][] deepCopy(PieceStats[][] pieceStats1) {
        PieceStats[][] clone = new PieceStats[pieceStats1.length][];

        for (int i = 0; i < clone.length; i++) {
            clone[i] = pieceStats1[i].clone();
        }

        return clone;
    }

    public static PositionStats[][] deepCopy(PositionStats[][] positionStats) {
        PositionStats[][] clone = new PositionStats[positionStats.length][];

        for (int i = 0; i < clone.length; i++) {
            clone[i] = positionStats[i].clone();
        }

        return clone;
    }
}
