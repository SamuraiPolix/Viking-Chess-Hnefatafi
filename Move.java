import java.util.ArrayList;

public class Move {
    // Represents a move in the game, used to 'undo' move in gameLogic
    private ArrayList<ConcretePiece> killed;        // pieces killed in this move
    private ConcretePiece killer;                   // killer if exists
    private Position from;                      // move from
    private Position to;                        // move to

    public Move(Position from, Position to) {
        this.from = from;
        this.to = to;
        this.killer = null;
        this.killed = new ArrayList<>();
    }

    public void addKill(ConcretePiece killer, ConcretePiece killed) {
        // there can only be one killer in a move so there is no problem resetting the killer if he killed twice
        this.killer = killer;
        this.killed.add(killed);
    }

    public Position getFrom() {
        return this.from;
    }

    public Position getTo() {
        return this.to;
    }

    public ArrayList<ConcretePiece> getKilledList() {
        return this.killed;
    }

    public Pawn getKiller() {
        return (Pawn)this.killer;
    }

}
