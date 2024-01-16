import java.util.ArrayList;

public class PieceStats {

    private Piece piece;
    private ArrayList<Position> moves;
    private int kills;

    private String id;

    private int travelLength;

    public PieceStats(Piece piece, String id, Position firstPos) {
        this.piece = piece;
        this.moves = new ArrayList<Position>();
        this.moves.add(firstPos);
        this.travelLength = 0;
        this.kills = 0;
        this.id = id;
    }

    public void addMove(Position newPos) {
        this.moves.add(newPos);
        if (this.moves.size() >= 2)
            this.travelLength += getLastTwoDistance();
    }

    public void addKill() {
        this.kills++;
    }

    public int getKills() {
        return this.kills;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public String getId() {
        return this.id;
    }

    public int getNumberOfMoves() {
        return this.moves.size();
    }

    // Returns the distance between the last 2 moves
    private int getLastTwoDistance() {
        return Math.abs(this.moves.get(moves.size() - 1).getLengthFrom(this.moves.get(moves.size() - 2)));
    }

    public int getTravelLength() {
        return this.travelLength;
    }

    public void printMoves() {
        System.out.println (id + ": " + moves);
    }

    public void printKills() {
        System.out.println(id + ": " + this.kills + " kills");
    }

    public void printSteps() {
        System.out.println(id + ": " + this.travelLength + " squares");
    }

}
