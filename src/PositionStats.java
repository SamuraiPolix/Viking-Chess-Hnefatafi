import java.util.ArrayList;

public class PositionStats {
    private Position pos;
    private ArrayList<Piece> visitedPieces;

    public PositionStats(Position pos) {
        this.pos = pos;
        this.visitedPieces = new ArrayList<Piece>();
    }

    public void addVisit(Piece piece) {
        if (piece != null && !visitedPieces.contains(piece))
            visitedPieces.add(piece);
    }

    public int getNumberOfVisits() {
        return this.visitedPieces.size();
    }

    public void printVisits() {
        System.out.println(pos + "" + getNumberOfVisits() + " pieces");
    }

    public int getPosRow() {
        return this.pos.getRow();
    }
    public int getPosCol() {
        return this.pos.getCol();
    }

}
