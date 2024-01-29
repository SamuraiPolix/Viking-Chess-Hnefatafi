import java.util.ArrayList;

public class Position {
    private ArrayList<ConcretePiece> visitedPieces;
    private int row;
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
        visitedPieces = new ArrayList<>();
    }

    public boolean isCorner() {
        return (this.row == 10 && this.col == 10) ||
                (this.row == 0 && this.col == 0) ||
                (this.row == 0 && this.col == 10) ||
                (this.row == 10 && this.col == 0);
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    // adds a piece that visited the position to a list, only if the piece doesn't already exist in the list
    public void addVisit(ConcretePiece piece) {
        if (piece != null && !visitedPieces.contains(piece))
            visitedPieces.add(piece);
    }

    // gets the last piece that was on this position
    public ConcretePiece getLastVisit() {
        return this.visitedPieces.get(visitedPieces.size() - 1);
    }

    public void removeVisit() {
        this.visitedPieces.remove(visitedPieces.size()-1);
    }

    public int getNumberOfVisits() {
        return this.visitedPieces.size();
    }

    public Position getLeft() {
        return new Position(row, col-1);
    }

    public Position getRight() {
        return new Position(row, col+1);
    }

    public Position getUp() {
        return new Position(row+1, col);
    }

    public Position getDown() {
        return new Position(row-1, col);
    }

    // checks if position exceeds the border
    public boolean isValid(int boardSize){
        return row < boardSize && col < boardSize && row >= 0 && col >= 0;
    }

    // Returns the distance from 'this' to dest
    public int getDistanceFrom(Position dest) {
        return Math.abs(this.row - dest.getRow()) + Math.abs(this.col - dest.getCol());
    }

    public String toString() {
        return "(" + this.row + ", " + this.col + ")";
    }

    public void printVisits() {
        System.out.println(this + "" + getNumberOfVisits() + " pieces");
    }
}
