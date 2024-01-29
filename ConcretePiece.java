import java.util.ArrayList;

public abstract class ConcretePiece implements Piece{
    private ArrayList<Position> moves;
    protected Player owner;
    protected String id;
    protected int distanceTraveled;


    public ConcretePiece(Player owner, String id) {
        this.owner = owner;
        this.distanceTraveled = 0;
        this.id = id;
        this.moves = new ArrayList<>();
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public abstract String getType();

    public void addMove(Position newPos) {
        this.moves.add(newPos);
        if (this.moves.size() >= 2)
            this.distanceTraveled += getLastTwoDistance();
    }

    public void removeMove() {
        if (this.moves.size() >= 2)
            this.distanceTraveled -= getLastTwoDistance();
        this.moves.remove(this.moves.size() - 1);
    }

    private int getLastTwoDistance() {
        return Math.abs(this.moves.get(moves.size() - 1).getDistanceFrom(this.moves.get(moves.size() - 2)));
    }

    public int getDistanceTraveled() {
        return this.distanceTraveled;
    }

    public String getId() {
        return this.id;
    }

    public int getNumberOfMoves() {
        return this.moves.size();
    }

    public String toString() {
        return this.id + ": ";
    }

    public void printMoves() {
        System.out.println (this + "" + moves);
    }

    public void printSteps() {
        System.out.println(this + "" + this.distanceTraveled + " squares");
    }

    public Position getPos() {
        return this.moves.get(moves.size()-1);
    }

}
