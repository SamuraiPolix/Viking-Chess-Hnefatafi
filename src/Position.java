public class Position {
    private int row;
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
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

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Position getLeft() {
        return new Position(row, col-1);
    }

    public Position getRight() {
        return new Position(row, col+1);
    }

    public Position getUp() {
        return new Position(row-1, col);
    }

    public Position getDown() {
        return new Position(row+1, col);
    }

    public boolean isValid(int boardSize){
        return row < boardSize && col < boardSize && row >= 0 && col >= 0;
    }

    // Returns the length from 'this' to dest
    public int getLengthFrom(Position dest) {
        return Math.abs(this.row - dest.getRow()) + Math.abs(this.col - dest.getCol());
    }

    public String toString() {
        return "(" + this.row + ", " + this.col + ")";
    }
}
